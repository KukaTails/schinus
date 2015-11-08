package frontend.clang;

import static message.MessageType.TOKEN;
import static message.MessageType.PARSER_SUMMARY;
import static frontend.clang.ClangTokenType.ERROR;
import static frontend.clang.ClangErrorCode.IO_ERROR;

import frontend.Token;
import frontend.EofToken;
import frontend.TokenType;
import frontend.Scanner;
import frontend.Parser;
import message.Message;

import java.io.IOException;

/*
 * <h1>ClangParserTD</h1>
 * <p>
 * <p>The top-down C language parser.</p>
 */
public class ClangParserTD extends Parser {

  /* the error handler of c language handler */
  protected static ClangErrorHandler errorHandler = new ClangErrorHandler();

  /**
   * Constructor.
   *
   * @param scanner the scanner to be used with this parser.
   */
  public ClangParserTD(Scanner scanner) {

    super(scanner);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public ClangParserTD(ClangParserTD parent) {
    super(parent.getScanner());
  }

  @Override
  public void parse() throws Exception {
    Token token;
    long startTime = System.currentTimeMillis();

    try {
      // Loop over each token until the end of file.
      while (!((token = nextToken()) instanceof EofToken)) {
        TokenType tokenType = token.getType();

        if (tokenType != ERROR) {
          // send the message about token to ParserMessageListener
          sendMessage(new Message(TOKEN, new Object[] {token.getLineNumber(), token.getPosition(),
              token.getType(), token.getText(), token.getValue()}));
        } else {
          errorHandler.flag(token, (ClangErrorCode) token.getValue(), this);
        }
      }
      // Send the parser summary message.
      float elapsedTime = (System.currentTimeMillis() - startTime) / 1000f;
      sendMessage(new Message(PARSER_SUMMARY, new Number[] {token.getLineNumber(), getErrorCount(), elapsedTime}));
    } catch(IOException ex) {
      errorHandler.abortTranslation(IO_ERROR, this);
    }
  }

  /**
   * Return the number of syntax errors found by the parser.
   * @return the error count.
   */
  public int getErrorCount() {
    return errorHandler.getErrorCount();
  }
}