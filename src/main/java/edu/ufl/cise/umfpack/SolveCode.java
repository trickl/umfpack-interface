package edu.ufl.cise.umfpack;

public enum SolveCode {

   A(0), /* Ax=b    */
   At(1), /* A'x=b   */
   Aat(2), /* A.'x=b  */
   Pt_L(3), /* P'Lx=b  */
   L(4), /* Lx=b    */
   Lt_P(5), /* L'Px=b  */
   Lat_P(6), /* L.'Px=b */
   Lt(7), /* L'x=b   */
   Lat(8), /* L.'x=b  */
   U_Qt(9), /* UQ'x=b  */
   U(10), /* Ux=b    */
   Q_Ut(11), /* QU'x=b  */
   Q_Uat(12), /* QU.'x=b */
   Ut(13), /* U'x=b   */
   Uat(14);  /* U.'x=b  */

   private int value;

   private SolveCode(int value) {
      this.value = value;
   }

   public int getValue() {
      return value;
   }
}
