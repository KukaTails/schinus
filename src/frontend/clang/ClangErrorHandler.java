package frontend.clang;

import static message.MessageType.SYNTAX_ERROR;
import static frontend.clang.ClangErrorCode.TOO_MANY_ERRORS;

import message.Message;
import frontend.Token;
import frontend.Parser;

/**
 * <h1>ClangErrorHandler</h1>
 * <p>
 * <p>Error codes for C language</p>
 */
public class ClangErrorHandler {

  private static final int MAX_ERRORS = 25;
  private static int errorCount = 0;

  public int getErrorCount() {
    return errorCount;
  }

  /**
   * Sent error token, error code to the listeners of parser
   *
   * @param token     the token that are bad token
   * @param errorCode the code of error which tells what mistake had happened
   * @param parser    the parser which will send the error message to its listeners
   */
  public void flag(Token token, ClangErrorCode errorCode, Parser parser) {
    parser.sendMessage(new Message(SYNTAX_ERROR, new Object[] {token.getLineNumber(), token.getPosition(), token.getText(), errorCode.toString()}));
    if (++errorCount > MAX_ERRORS) abortTranslation(TOO_MANY_ERRORS, parser);
  }

  /**
   * Aboart translation when too many errors happened
   *
   * @param errorCode the error code
   * @param parser the parser which send message to its listeners
   */
  public void abortTranslation(ClangErrorCode errorCode, Parser parser) {
    // Notify the parser's listeners and then abort
    String fatalText = "FATAL ERROR: " + errorCode.toString();
    parser.sendMessage(new Message(SYNTAX_ERROR, new Object[] {0, 0, "", fatalText}));
    System.exit(errorCode.getStatus());
  }
}