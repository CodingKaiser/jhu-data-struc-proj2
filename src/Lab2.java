import java.io.*;

/**
 * Created by falko on 07-07-17.
 */
public class Lab2 {

  public static void main(String[] args) {

    BufferedReader input;
    BufferedWriter output;
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
    lab.parseInputMatrices(input, output);

    try {
      input.close();
      output.close();
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  private void parseInputMatrices(BufferedReader input,
                                  BufferedWriter out) {
    int curr;
    char c;
    ReadMatrixAndCompute rmac = new ReadMatrixAndCompute(input, out);
    try {
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
