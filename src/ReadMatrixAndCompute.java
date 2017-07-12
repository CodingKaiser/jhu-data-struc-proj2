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
  private int currIntValue;
  private int i;
  private int j;
  private boolean parsingDimensions;
  private boolean charIsNegative;
  private boolean prevWasSpace;
  private boolean parsingInt;
  private int maxDimens;
  private boolean prevEncounteredError;

  ReadMatrixAndCompute(BufferedReader in, BufferedWriter out) {
    input = in;
    output = out;
    currIntValue = 0;
    i = 0;
    j = 0;
    parsingDimensions = true;
    charIsNegative = false;
    prevWasSpace = true;
    parsingInt = true;
    maxDimens = 0;
    prevEncounteredError = false;
  }

  void handleCharacter(int chi) {
    Character c = (char) chi;
    if (parsingDimensions) {
      handleDimensionsInput(chi);
    } else {
      try {
        output.write(c);
      } catch (IOException e) {
        System.err.println("Was not able to write to out.");
      }
      if (c == ' ') {
        handleSpace();
      } else if (c == '-') {
        handleNegative();
      } else if (IntParser.isDigit(chi)) {
        handleDigit(chi);
      } else if (c.equals('\r') || (c.equals('\n'))){
        if (parsingInt) {
          insertValueIntoMatrix();
        } else if (prevWasSpace) {
          j--;
        }
        i++;
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
        handleErrors(c);
      }
    }
  }

  private void handleDimensionsInput(int i) {
    if (IntParser.isDigit(i)) {
      int intVal = IntParser.toDigit(i);
      updateCurrIntegerValue(intVal);
    } else if ((char) i == '\r' || (char) i == '\n') {
      maxDimens = currIntValue;
      matrix = new int[maxDimens][maxDimens];
      parsingDimensions = false;
      parsingInt = false;
      currIntValue = 0;
      if ((char) i == '\r') {
        try {
          input.read(); // Windows EOL characters
        } catch (IOException e) {
          System.err.println("\\n did not follow an \\r!");
        }
      }
    } else {
      System.err.println("Positive integers only");
    }
  }

  private void checkIfMatrixCompleteAndCompute(){
    if (i == maxDimens && j + 1 == maxDimens) {
      try {
        output.write("Calculated value: ");
        output.write(ComputeMatrix.determinant(matrix) + "");
        output.newLine();
        output.newLine();
      } catch (IOException e) {
        System.err.println(e);
      }
      reset();
    } else if (i == maxDimens && j < maxDimens) {
      System.err.println("Too short!");
      handleErrors('\r');
    }
    j = 0; // At end of line
  }

  private void updateCurrIntegerValue(int i) {
    if (currIntValue == 0) {
      currIntValue = i;
    } else {
      currIntValue = currIntValue * 10 + i;
    }
  }

  private void handleSpace() {
    if (prevWasSpace) {
      System.err.println("Single spaces only.");
      handleErrors(' ');
    } else {
      insertValueIntoMatrix();
      j++;
    }
    prevWasSpace = true;
    parsingInt = false;
    charIsNegative = false;
    currIntValue = 0;
  }

  private void handleDigit(int c) {
    int val = IntParser.toDigit(c);
    if (j >= 6) {
      System.err.println("The line exceeds the max");
      handleErrors((char) val);
    } else {
      updateCurrIntegerValue(val);
      prevWasSpace = false;
      parsingInt = true;
    }
  }

  private void handleNegative() {
    if (charIsNegative) {
      System.err.println("No consecutive dashes please");
      handleErrors('-');
    } else {
      charIsNegative = true;
      prevWasSpace = false;
    }
  }

  private void reset() {
    currIntValue = 0;
    i = 0;
    j = 0;
    parsingDimensions = true;
    charIsNegative = false;
    prevWasSpace = true;
    parsingInt = true;
    maxDimens = 0;
    prevEncounteredError = false;
  }

  private void insertValueIntoMatrix() {
    if (charIsNegative) {
      matrix[i][j] = 0 - currIntValue;
    } else {
      matrix[i][j] = currIntValue;
    }
  }

  private void handleErrors(Character c) {
    if (maxDimens > 0) {
      // Skip the rest of the lines in the matrix
      int next;
      try {
        output.write(c);
        while (i < maxDimens && (next = input.read()) != -1) {
          output.write(next);
          if ((char) next == '\r' || (char) next == '\n') {
            i++;
            System.err.println("Skipping ");
            if ((char) next == '\r') {
              output.write(input.read()); // Windows EOL
            }
          }
        }
        output.write("Encountered error at character --> ");
      } catch (IOException e) {
        System.err.println(e);
      }
    } else {
      //
    }
    prevEncounteredError = true;
  }
}
