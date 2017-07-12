/**
 * Created by falko on 11-07-17.
 */
class IntParser {
  static boolean isDigit(int c) {
    return (c > 47 && c < 58);
  }

  static int toDigit(int c) {
    return (c - 48);
  }
}
