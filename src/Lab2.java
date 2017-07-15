import java.io.*;

/**
 * Main driver and entry point into the application. Reads the input
 * matrices and calculates the corresponding determinant.
 * Writes the matrix and the calculated value to output.
 * This application must be called
 * from the command line with valid input/output file paths
 * as arguments.
 * @Author Falko Noe
 * @Version 1.0
 */
public class Lab2 {

  /**
   * The main entry point to the class. Will be called when the
   * user runs this program from the command-line.
   * @param args An array holding the two command-line arguments. The
   *             first argument is the input file in text format. The
   *             second argument is the path to the output text file.
   *             Both arguments must be valid paths.
   */
  public static void main(String[] args) {

    BufferedReader input; // will hold the input
    BufferedWriter output; // will hold the output
    Lab2 lab;

    if (args.length != 2) {
      System.err.println("Usage:  java Lab2 [input file pathname]" +
              " [output file pathname]");
      System.exit(1);
    }

    try {
      input = new BufferedReader(new FileReader(args[0]));
      output = new BufferedWriter(new FileWriter(args[1]));
    } catch (IOException e) {
      System.err.println("Make sure the input/output path is correct.");
      return;
    }

    lab = new Lab2();
    lab.parseInputMatrices(input, output); // read input and process

    try {
      /* Close the input and output, writes file output,
       * and exit the application */
      input.close();
      output.close();
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  /**
   * Reads the input file character by character via the BufferedReader
   * and passes the characters to ReadMatrixAndCompute which
   * handles the letters according to the logic delineated therein.
   * Will run until the end of the line is reached.
   * @param input: The input BufferedReader which the input
   *             will be read from
   * @param out: The output BufferedWriter which the output
   *           will be written to.
   */
  private void parseInputMatrices(BufferedReader input,
                                  BufferedWriter out) {
    int curr;
    char c;
    ReadMatrixAndCompute rmac = new ReadMatrixAndCompute(input, out);
    try {
      // Read until end of the file is reached
      while (((curr = input.read()) != -1)) {
        c = (char) curr;
        rmac.handleCharacter(curr);
      }
    } catch (IOException e) {
      System.err.println(e);
      System.err.println("Was not able to read the input file");
    }
  }
}
