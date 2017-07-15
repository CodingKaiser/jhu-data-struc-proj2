/**
 * Class which calculates the determinant. Performs this
 * operation recursively. Is called by ReadMatrixAndCompute,
 * which passes a 2-D array into the determinant method.
 * @Author: Falko Noe
 * @Version: 1.0
 */
class ComputeMatrix {

  /**
   * Calculates the determinant of a matrix, through a recursive call:
   * --> For a 1x1 matrix return the only value
   * --> Otherwise, for every column in the first row of the matrix,
   * calculate the sum of:
   * (1^-1) * matrix[1st row][j] * determinant(minor(matrix[1st row][j]))
   * @param matrix: A 2-D array of signed integers.
   * @return The determinant of the matrix.
   */
  static int determinant(int[][] matrix) {
    if (matrix.length <= 1) {
      return matrix[0][0]; // the only value
    } else {
      int sum = 0;
      for (int j = 0; j < matrix[0].length; j++) {
        /* Can ignore column values of 0, since
         * 0 x (any number) = 0 */
        if (matrix[0][j] != 0) {
          sum += Math.pow(-1, j) * matrix[0][j] *
                  determinant(minor(matrix, 0, j));
        }
      }
      return sum;
    }
  }

  /**
   * Determines the minor of the input matrix based on the x, y
   * coordinates of the parent matrix upon which the determinant
   * is being calculated.
   * @param matr: The input matrix
   * @param x: The x coordinate of the parent matrix from which
   *         the minor will be calculated
   * @param y: The y coordinate of the parent matrix from which
   *         the minor will be calculated
   * @return The minor of the input matrix at coordinates x, y
   * in the form of another 2-D matrix.
   */
  private static int[][] minor(int[][] matr, int x, int y) {
    int newOrder = matr.length - 1; // one less row and column
    int[][] minor = new int[newOrder][newOrder];
    for (int i = 0; i < matr.length; i++) {
      if (i != x) {
        // Only copy values not in row x
        for (int j = 0; j < matr.length; j++) {
          if (j != y) {
            // Only copy values not in row y
            if (i < x && j < y) {
              minor[i][j] = matr[i][j]; // no shift
            } else if (i < x) {
              minor[i][j - 1] = matr[i][j]; // shift left
            } else if (j < y) {
              minor[i - 1][j] = matr[i][j]; // shift up
            } else {
              minor[i - 1][j - 1] = matr[i][j]; // shift left/up
            }
          }
        }
      }
    }
    return minor;
  }
}
