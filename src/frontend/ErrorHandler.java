package frontend;

import static message.MessageType.SYNTAX_ERROR;
import static frontend.ErrorCode.TOO_MANY_ERRORS;

import message.Message;

/**
 * <h1>ErrorHandler</h1>
 * <p>
 * <p>Error handler for parsers</p>
 */
public class ErrorHandler {
  private static final int MAX_ERRORS = 25;    // the max errors which will not abort program
  private static int errorCount = 0;           // the count of parse errors

  /**
   * @return the count of parse errors.
   */
  public int getErrorCount() {
    return errorCount;
  }

  public void clearErrorCount() {
    errorCount = 0;
  }

  /**
   * Sent error token, error code to the listeners of parser
   *
   * @param token     the token that are bad token
   * @param errorCode the code of error which tells what mistake had happened
   * @param parser    the parser which will send the error message to its listeners
   */
  public void flag(Token token, ErrorCode errorCode, Parser parser) {
    parser.sendMessage(new Message(SYNTAX_ERROR, new Object[] {token.getLineNumber(), token.getPosition(),
        token.getText(), errorCode.toString()}));
    if (++errorCount > MAX_ERRORS)
      abortTranslation(TOO_MANY_ERRORS, parser);
  }

  /**
   * Abort translation when too many errors happened
   *
   * @param errorCode the error code
   * @param parser    the parser which send message to its listeners
   */
  public void abortTranslation(ErrorCode errorCode, Parser parser) {
    // Notify the parser's listeners and then abort
    String fatalText = "FATAL ERROR: " + errorCode.toString();
    parser.sendMessage(new Message(SYNTAX_ERROR, new Object[] {0, 0, "", fatalText}));
    System.exit(errorCode.getStatus());
  }
}