/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ufl.cise.umfpack;

import cern.colt.matrix.DoubleMatrix1D;
import edu.ufl.cise.umfpack.bindings.colt.UmfPackSolver;
import edu.ufl.cise.categories.IntegrationTest;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 *
 * @author tgee
 */
public class UmfPackLibraryTest {

    public UmfPackLibraryTest() {
    }

   @BeforeClass
   public static void setUpClass() throws Exception {
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

   /**
    * This is the example calculation provided in the UMFPack Quickstart Guide
    */
   @Category(IntegrationTest.class)
   @Test
   public void testQuickstartExample() {
      double tolerance = 1e-10;
      SparseDoubleMatrix2D A = new SparseDoubleMatrix2D(5, 5);
      A.setQuick(0, 0, 2.);
      A.setQuick(1, 0, 3.);
      A.setQuick(0, 1, 3.);
      A.setQuick(2, 1, -1.);
      A.setQuick(4, 1, 4.);
      A.setQuick(1, 2, 4.);
      A.setQuick(2, 2, -3.);
      A.setQuick(3, 2, 1.);
      A.setQuick(4, 2, 2.);
      A.setQuick(2, 3, 2.);
      A.setQuick(1, 4, 6.);
      A.setQuick(4, 4, 1.);

      DenseDoubleMatrix1D B = new DenseDoubleMatrix1D(5);
      B.setQuick(0, 8.);
      B.setQuick(1, 45.);
      B.setQuick(2, -3.);
      B.setQuick(3, 3.);
      B.setQuick(4, 19.);

      UmfPackSolver solver = new UmfPackSolver();
      DoubleMatrix1D X = solver.solve(A, B);
      Assert.assertEquals(1., X.get(0), tolerance);
      Assert.assertEquals(2., X.get(1), tolerance);
      Assert.assertEquals(3., X.get(2), tolerance);
      Assert.assertEquals(4., X.get(3), tolerance);
      Assert.assertEquals(5., X.get(4), tolerance);
   }
}
