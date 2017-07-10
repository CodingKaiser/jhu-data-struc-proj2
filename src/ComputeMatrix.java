/**
 * Created by falko on 07-07-17.
 */
class ComputeMatrix {

  static int determinant(int[][] matrix) {
    if (matrix.length <= 1) {
      return matrix[0][0];
    } else {
      int sum = 0;
      for (int j = 0; j < matrix[0].length; j++) {
        if (matrix[0][j] != 0) {
          sum += Math.pow(-1, j) * matrix[0][j] * determinant(minor(matrix, 0, j));
        }
      }
      return sum;
    }
  }

  private static int[][] minor(int[][] matr, int x, int y) {
    int newOrder = matr.length - 1;
    int[][] minor = new int[newOrder][newOrder];
    for (int i = 0; i < matr.length; i++) {
      if (i != x) {
        for (int j = 0; j < matr.length; j++) {
          if (j != y) {
            if (i < x && j < y) {
              minor[i][j] = matr[i][j];
            } else if (i < x) {
              minor[i][j - 1] = matr[i][j];
            } else if (j < y) {
              minor[i - 1][j] = matr[i][j];
            } else {
              minor[i - 1][j - 1] = matr[i][j];
            }
          }
        }
      }
    }
    return minor;
  }
}
