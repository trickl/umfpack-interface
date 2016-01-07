package edu.ufl.cise.umfpack;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum StatusCode {

   OK(0),
   /* status > 0 means a warning, but the method was successful anyway. */
   /* A Symbolic or Numeric object was still created. */
   WARNING_SINGULAR_MATRIX(1),
   /* The following warnings were added in *_get_determinant */
   WARNING_DETERMINANT_UNDERFLOW(2),
   WARNING_DETERMINANT_OVERFLOW(3),
   /* status < 0 means an error, and the method was not successful. */
   /* No Symbolic of Numeric object was created. */
   ERROR_OUT_OF_MEMORY(-1),
   ERROR_INVALID_NUMERIC_OBJECT(-3),
   ERROR_INVALID_SYMBOLIC_OBJECT(-4),
   ERROR_ARGUMENT_MISSING(-5),
   ERROR_N_NONPOSITIVE(-6),
   ERROR_INVALID_MATRIX(-8),
   ERROR_DIFFERENT_PATTERN(-11),
   ERROR_INVALID_SYSTEM(-13),
   ERROR_INVALID_PERMUTATION(-15),
   ERROR_INTERNAL_ERROR(-911), /* yes, call me if you get this! */
   ERROR_FILE_IO(-17);
   
   private static final Map<Integer, StatusCode> lookup = new HashMap<Integer, StatusCode>();

   static {
      for (StatusCode s : EnumSet.allOf(StatusCode.class)) {
         lookup.put(s.getValue(), s);
      }
   }
   private int value;

   private StatusCode(int value) {
      this.value = value;
   }

   int getValue() {
      return value;
   }

   public static StatusCode valueOf(int value) {
      return lookup.get(value);
   }
}
