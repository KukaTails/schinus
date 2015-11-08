package frontend.clang;

/**
 * <h1>ClangErrorCode</h1>
 *
 * <p>Error Code for errorHandler</p>
 */
public enum ClangErrorCode {

  INVALID_NUMBER("Invalid number"),                 // parse token of number and the string of number has other characters
  INVALID_CHARACTER("Invalid character"),           // invalid character when parser(unexpected token)
  RANGE_INTEGER("Integer literal out of range"),    // the value of integer literal is too big
  RANGE_REAL("Real literal out of range"),          // the value of float literal is too big
  UNEXPECTED_EOF("Unexpected end of file"),         // error happens when parse token

  IO_ERROR(-101, "Object I/O error"),               // IO exception error when parser
  TOO_MANY_ERRORS(-102, "Too many syntax errors");  // too many errors when parser

  private int status;   // exit status
  private String message;  // error message

  /**
   * Constructor.
   * @param message the error message
   */
  ClangErrorCode(String message) {
    this.status = 0;
    this.message = message;
  }

  /**
   * Constructor.
   * @param status the exit status
   * @param message the error message
   */
  ClangErrorCode(int status, String message) {
    this.status = status;
    this.message = message;
  }

  /**
   * Getter.
   * @return the exit status
   */
  public int getStatus() {
    return status;
  }

  /**
   *
   * @return the message
   */
  public String toString() {
    return message;
  }
}
