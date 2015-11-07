package frontend.clang;

public enum ClangErrorCode {
  INVALID_NUMBER("Invalid number"),
  INVALID_CHARACTER("Invalid character"),
  RANGE_INTEGER("Integer literal out of range"),
  RANGE_REAL("Real literal out of range"),
  TOO_MANY_ERRORS(-102, "Too many syntax errors"),
  UNEXPECTED_EOF("Unexpected end of file");

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
