package edu.ufl.cise.umfpack.bindings.colt;

import cern.colt.list.DoubleArrayList;
import cern.colt.list.IntArrayList;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import com.sun.jna.ptr.PointerByReference;
import edu.ufl.cise.umfpack.SolveCode;
import edu.ufl.cise.umfpack.StatusCode;
import edu.ufl.cise.umfpack.UmfPackLibrary;
import java.nio.DoubleBuffer;

public class UmfPackSolver {
   public DoubleMatrix2D solve(DoubleMatrix2D A, DoubleMatrix2D B) {
      DoubleMatrix2D result = new DenseDoubleMatrix2D(A.columns(), B.columns());
      for (int i = 0; i < B.columns(); ++i) {
         result.viewColumn(i).assign(solve(A, B.viewColumn(i)));
      }
      
      return result;
   }

   public DoubleMatrix1D solve(DoubleMatrix2D A, DoubleMatrix1D B) {

      if (A == null || B == null) {
         throw new NullPointerException();
      }
      if (A.rows() != A.columns()) {
         throw new IllegalArgumentException("Matrix A must be square.");
      }
      else if (B.size() != A.rows()) {
         throw new IllegalArgumentException("Matrix b must have the same number of rows as matrix A.");
      }

      int sys = SolveCode.At.getValue();

      // Bind tranpose(A) {Colt defines sparse matrices in row-major format, UMFPack uses column-major}.
      IntArrayList rowIndexList = new IntArrayList();
      IntArrayList columnIndexList = new IntArrayList();
      DoubleArrayList valueList = new DoubleArrayList();

      A.getNonZeros(rowIndexList, columnIndexList, valueList);
      rowIndexList.trimToSize();
      columnIndexList.trimToSize();
      valueList.trimToSize();

      // Convert the row indices into row offsets
      IntArrayList rowOffsetList = new IntArrayList();
      int previousRow = -1;
      for (int i = 0; i < rowIndexList.size(); ++i) {
         int row = rowIndexList.get(i);
         if (row != previousRow) {
            previousRow = row;
            rowOffsetList.add(i);
         }
      }
      rowOffsetList.add(rowIndexList.size());
      rowOffsetList.trimToSize();

      int[] Ap = rowOffsetList.elements();
      int[] Ai = columnIndexList.elements();
      double[] Ax = valueList.elements();

      // Bind B
      double[] Barray = new double[B.size()];
      for (int i = 0, end = B.size(); i < end; ++i) {
         Barray[i] = B.getQuick(i);
      }

      // Provide space for the result
      DoubleBuffer X = DoubleBuffer.allocate(B.size());

      // This will be populated by the allocation methods
      PointerByReference Numeric = new PointerByReference();
      PointerByReference Symbolic = new PointerByReference();

      // Use default controls
      double[] Control = null;

      // Discard any statistics
      DoubleBuffer Info = null;

      int symbolicResult = UmfPackLibrary.INSTANCE.umfpack_di_symbolic(A.columns(), A.rows(), Ap, Ai, Ax, Symbolic, Control, Info);

      if (StatusCode.valueOf(symbolicResult) != StatusCode.OK) {
         throw new ArithmeticException("Error during native UMFPACK symbolic call : " + StatusCode.valueOf(symbolicResult).toString());
      }

      try {
         int numericResult = UmfPackLibrary.INSTANCE.umfpack_di_numeric(Ap, Ai, Ax, Symbolic.getValue(), Numeric, Control, Info);

         if (StatusCode.valueOf(numericResult) != StatusCode.OK) {
            throw new ArithmeticException("Error during native UMFPACK numeric call : " + StatusCode.valueOf(numericResult).toString());
         }
      }
      finally {
         UmfPackLibrary.INSTANCE.umfpack_di_free_symbolic(Symbolic);  
      }

      try {
         int solveResult = UmfPackLibrary.INSTANCE.umfpack_di_solve(sys, Ap, Ai, Ax, X, Barray, Numeric.getValue(), Control, Info);

         if (StatusCode.valueOf(solveResult) != StatusCode.OK) {
            throw new ArithmeticException("Error during native UMFPACK solve call : " + StatusCode.valueOf(solveResult).toString());
         }
      }
      finally {
         UmfPackLibrary.INSTANCE.umfpack_di_free_numeric(Numeric);
      }

      // Bind the result
      DoubleMatrix1D result = new DenseDoubleMatrix1D(A.columns());
      result.assign(X.array());
      return result;
   }
}
