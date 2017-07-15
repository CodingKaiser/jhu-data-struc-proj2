/**
 * Class which provides helper methods related to detection of
 * and conversion of characters into integers.
 * @Author: Falko Noe
 * @Version: 1.0
 */
class IntParser {

  /**
   * Checks whether an int representation of a char corresponds
   * to a digit, 0-9
   * @param c: The integer corresponding to the character
   * @return A boolean. True if the character is an integer,
   * false otherwise.
   */
  static boolean isDigit(int c) {
    return (c > 47 && c < 58);
  }

  /**
   * Converts the ASCII char in int form to the actual
   * integer value that the char represents, 0-9.
   * @param c: The int representation of a char
   * @return An integer corresponding to the actual int
   * value of the character that was inputted. Conversion
   * based on ASCII 256 codes.
   */
  static int toDigit(int c) {
    return (c - 48);
  }
}
