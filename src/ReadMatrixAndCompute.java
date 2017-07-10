import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by falko on 07-07-17.
 */
class ReadMatrixAndCompute {

  private BufferedReader input;
  private BufferedWriter output;
  private int[][] matrix;
  private int i;
  private int j;
  private boolean parsingFirstLine;
  private boolean charIsNegative;
  private boolean prevWasSpace;
  private int maxDimens;

  ReadMatrixAndCompute(BufferedReader in, BufferedWriter out) {
    input = in;
    output = out;
    matrix = new int[6][6];
    i = 0;
    j = 0;
    parsingFirstLine = true;
    charIsNegative = false;
    prevWasSpace = true;
    maxDimens = 0;
  }

  void handleCharacter(Character c) {
    if (Character.isSpaceChar(c)) {
      handleSpace();
    } else if (Character.getType(c) == Character.DASH_PUNCTUATION) {
      handleNegative();
    } else if (Character.isDigit(c)) {
      handleDigit(c);
    } else if (c.equals('\r') || (c.equals('\n'))){
      if (prevWasSpace) {
        j--;
      }
      if (parsingFirstLine) {
        maxDimens = j + 1;
        parsingFirstLine = false;
        fitSizeOfMatrix();
      }
      i++; // Finished with first row
      if (c.equals('\r')) {
        try {
          input.read(); // Windows EOL characters
        } catch (IOException e) {
          System.err.println("\\n did not follow a \\r!");
        }
      }
      checkIfMatrixCompleteAndCompute();
    } else {
      System.err.println("Invalid character");
    }
  }

  private void checkIfMatrixCompleteAndCompute(){
    if (i == maxDimens && j + 1 == maxDimens) {
      try {
        output.newLine();
        output.write("Calculated value: ");
        output.write(ComputeMatrix.determinant(matrix) + "");
        output.newLine();
      } catch (IOException e) {
        System.err.println(e);
      }
      reset();
    } else if (i == maxDimens && j < maxDimens) {
      System.err.println("Too short!");
    }
    j = 0; // At end of line
  }

  private void handleSpace() {
    if (charIsNegative) {
      System.err.println("Dash preceded space");
    } else if (prevWasSpace) {
      System.err.println("Single spaces only.");
    } else {
      j++;
    }
    prevWasSpace = true;
  }

  private void handleDigit(Character c) {
    if (j >= 6) {
      System.err.println("The line exceeds the max");
    } else if (charIsNegative) {
      matrix[i][j] = 0 - Integer.parseInt(c.toString());
    } else {
      matrix[i][j] = Integer.parseInt(c.toString());
    }
    prevWasSpace = false;
    charIsNegative = false;
  }

  private void handleNegative() {
    if (charIsNegative) {
      System.err.println("No consecutive dashes please");
    }
    charIsNegative = true;
    prevWasSpace = false;
  }

  private void reset() {
    matrix = new int[6][6];
    i = 0;
    j = 0;
    parsingFirstLine = true;
    charIsNegative = false;
    prevWasSpace = true;
    maxDimens = 0;
  }

  private void fitSizeOfMatrix() {
    if (maxDimens < 6) {
      int[][] temp = new int[maxDimens][maxDimens];
      for (int y = 0; y < maxDimens; y++) {
        temp[i][y] = matrix[i][y];
      }
      matrix = temp;
    }
  }
}
