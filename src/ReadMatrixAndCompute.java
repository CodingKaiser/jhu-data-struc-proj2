import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
/**
 * Handles the logic of every character that is loaded in from
 * the input. The character is checked for validity, i.e.
 * if it is an integer or negative sign. Will also echo the input
 * for purposes of generating a neat output file, which will display
 * the calculated determinant for each matrix, as well as any
 * errors that may have been encountered while parsing the file.
 * @Author: Falko Noe
 * @Version: 1.0
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

  /**
   * Constructor for this class. Instantiates all necessary variables,
   * and taking in
   * @param in: The BufferedReader that holds the input. Need this in
   *          order to read in extra input characters for formatting
   *          of the output.
   * @param out: The BufferedWriter that holds the output. This class
   *           will directly write the output to the file that this
   *           BufferedWriter has been instantiated with.
   */
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
  }

  /**
   * Handles top-level syphoning of the character read in from the
   * input into the appropriate streams of logic. Handles character
   * according to two main conditions: The input character is being
   * parsed in is an integer that will define the order of the matrix;
   * the input character is part of the matrix itself.
   * @param chi: The character in the form of an integer as it
   *           was read in from the input file.
   */
  void handleCharacter(int chi) {
    Character c = (char) chi;
    try {
      output.write(c);
    } catch (IOException e) {
      System.err.println("Was not able to write to out.");
    }
    if (parsingDimensions) {
      handleDimensionsInput(chi);
    } else {
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
        handleErrors("Invalid character " + c, i + 1, j);
      }
    }
  }

  /**
   * Handles logic if we are currently parsing the order for the
   * subsequent matrix. Only accepts:
   * ***Integer values.
   * Does not accept:
   * ***White space, negative signs, non-numeric characters
   * @param i: The integer representation of the input character.
   */
  private void handleDimensionsInput(int i) {
    if (IntParser.isDigit(i)) {
      // Logic for handling digits
      int intVal = IntParser.toDigit(i);
      updateCurrIntegerValue(intVal);
    } else if ((char) i == '-') {
      // Logic for handling negative signs (err)
      System.err.println("Positive integers only");
      handleDimensionsError("Positive integer values only", i);
    } else if ((char) i == '\r' || (char) i == '\n') {
      // If EOL is reached without errors, instantiate matrix with order
      maxDimens = currIntValue;
      matrix = new int[maxDimens][maxDimens];
      parsingDimensions = false;
      parsingInt = false;
      currIntValue = 0;
      if ((char) i == '\r') {
        try {
          input.read(); // Special Windows EOL characters
        } catch (IOException e) {
          System.err.println("\\n did not follow an \\r!");
        }
      }
    } else {
      System.err.println("Invalid character");
      handleDimensionsError("Invalid character '" + (char) i + "'", i);
    }
  }

  /**
   * Handle errors associated with invalid input characters being parsed
   * in while we are parsing in the input char. Will
   * @param errMsg: The error messag
   * @param i
   */
  private void handleDimensionsError(String errMsg, int i) {
    try {
      // Print rest of the characters to out for more informative err msg
      int next;
      while ((next = input.read()) != -1 &&
              ((char) next != '\r' || (char) next == '\n')) {
        output.write(next);
      }
      if ((char) next == '\r') {
        output.write(input.read()); // Speical Windows EOL
      }
      // Write the error message to out
      output.write("Encountered error during matrix order parsing --> " +
                      errMsg);
      output.newLine();
      output.newLine();
    } catch (IOException e) {
      System.err.println("Could not write to output");
    }
    reset();
  }

  /**
   * Checks to see whether the matrix currently being processed is
   * complete. If it is not, reset j. If we have reached the end
   * of the matrix, passes it to the determinant method and
   * prints out the resulting number to stdout. Since this method
   * should only get called when we encounter a newline character,
   * if the current line being parsed was too short or too long,
   * we declare an error and pass the logic to handleErrors method.
   */
  private void checkIfMatrixCompleteAndCompute(){
    if (i == maxDimens && j + 1 == maxDimens) {
      try {
        // Start formatting output for calculated value
        output.write("Calculated value: ");
        long startTime = System.nanoTime();
        // Calc the determinant of the input matrix
        output.write(ComputeMatrix.determinant(matrix) + "");
        System.out.println("Time elapsed: " +
                (System.nanoTime() - startTime));
        output.newLine();
        output.newLine();
      } catch (IOException e) {
        System.err.println(e);
      }
      reset();
    } else if (j + 1 < maxDimens) {
      System.err.println("Too short!");
      handleErrors("The line was too short", i, j);
    } else if (j + 1 > maxDimens) {
      System.err.println("Too long!");
      handleErrors("The line was too long", i, j);
    }
    j = 0; // At end of line
  }

  /**
   * Updates the current integer value..
   * @param i: The integer value read in from stdin.
   */
  private void updateCurrIntegerValue(int i) {
    if (currIntValue == 0) {
      currIntValue = i;
    } else {
      currIntValue = currIntValue * 10 + i;
    }
  }

  /**
   * Performs logic required to handle space characters.
   * Precondition:
   * ***The previous character was not a space
   * ***The previous character was an integer
   * Takes the current integer value and inserts it into the matrix
   * Resets the variables involved in parsing an integer.
   */
  private void handleSpace() {
    if (prevWasSpace) {
      System.err.println("Single spaces only.");
      handleErrors("More than one space ' ' consecutively", i + 1, j);
    } else if (!parsingInt) {
      // Gets called if a negative is by itself w/o int to negate.
      System.err.println("Negative sign without following integer.");
      handleErrors("Integer did not follow '-' sign", i + 1, j);
    } else {
      insertValueIntoMatrix();
      j++;
    }
    // Reset instance variables involved in
    prevWasSpace = true;
    parsingInt = false;
    charIsNegative = false;
    currIntValue = 0;
  }

  /**
   * Converts the ASCII int code representing the current digit
   * into the actual digit value. Handles error if
   * @param c: The ASCII int code of the digit currently being read in
   */
  private void handleDigit(int c) {
    int val = IntParser.toDigit(c);
    // Error handling for lines longer than the matrix order
    if (j >= maxDimens) {
      System.err.println("The line exceeds the max");
      handleErrors("The line exceeds the max allowable length",
                    i + 1, j);
    } else {
      updateCurrIntegerValue(val);
      prevWasSpace = false;
      parsingInt = true;
    }
  }

  /**
   * Logic for handling the character '-', representing a negative sign.
   * Precondition: The previous character was not a negative sign
   */
  private void handleNegative() {
    if (charIsNegative) {
      System.err.println("No consecutive dashes please");
      handleErrors("Consecutive dashes '-' present", i + 1, j);
    } else {
      charIsNegative = true;
      prevWasSpace = false;
    }
  }

  /**
   * Resets the instance variables to prime the program
   * for the next matrix, if there is one.
   */
  private void reset() {
    currIntValue = 0;
    i = 0;
    j = 0;
    parsingDimensions = true;
    charIsNegative = false;
    prevWasSpace = true;
    parsingInt = true;
    maxDimens = 0;
  }

  /**
   * Takes the value stored thus far and inserts it
   * into the matrix at the position in the matrix
   * currently being parsed. Gets called if EOL or space
   * is encountered.
   */
  private void insertValueIntoMatrix() {
    if (charIsNegative) {
      matrix[i][j] = 0 - currIntValue;
    } else {
      matrix[i][j] = currIntValue;
    }
  }

  /**
   * Handles the logic associated with errors that occur within a matrix.
   * Will also progress the input being read in to the next matrix,
   * such that cascading of any further errors is minimized.
   * @param errMessage: The message passed down detailing the specific
   *                  error that resulted in this method to be called.
   * @param errI: The line number in the matrix at which the error
   *           occurred
   * @param errJ: The column number in the matrix at which the error
   *           occurred
   */
  private void handleErrors(String errMessage, int errI, int errJ) {
    // Skip the rest of the lines in the matrix
    int next;
    try {
      while (i < maxDimens && (next = input.read()) != -1) {
        output.write(next);
        if ((char) next == '\r' || (char) next == '\n') {
          i++;
          if ((char) next == '\r') {
            output.write(input.read()); // Windows EOL
          }
        }
      }
      // Format the output to display the corresponding err message
      output.write("Encountered error on line " + errI +
                    " due to --> '" + errMessage + "'");
      output.newLine();
      output.newLine();
      reset();
    } catch (IOException e) {
      System.err.println(e);
    }
  }
}
