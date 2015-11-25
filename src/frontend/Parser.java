package frontend;

import static frontend.ErrorCode.IO_ERROR;
import static frontend.ErrorCode.UNEXPECTED_TOKEN;

import frontend.tokens.EofToken;
import message.Message;
import message.MessageHandler;
import message.MessageListener;
import message.MessageProducer;
import intermediate.ICodeNode;
import frontend.parsers.StatementParser;

import java.io.IOException;
import java.util.EnumSet;

/**
 * <h1>Parser</h1>
 * <p>
 * <p>the parser is the base class of all the parser class</p>
 */
public class Parser implements MessageProducer {
  protected static MessageHandler messageHandler;  // message handler delegate
  protected static ErrorHandler errorHandler;      // errorHandler

  static {
    messageHandler = new MessageHandler();
    errorHandler = new ErrorHandler();
  }

  protected Scanner scanner;

  /**
   * @param scanner the scanner to be used with this parser.
   */
  protected Parser(Scanner scanner) {
    this.scanner = scanner;
  }

  /**
   * @param parent parent parser used to get scanner.
   */
  protected Parser(Parser parent) {
    this.scanner = parent.getScanner();
  }

  /**
   * @return the scanner used by this parser.
   */
  public Scanner getScanner() {
    return scanner;
  }

  /**
   * @return the message handler.
   */
  public MessageHandler getMessageHandler() {
    return messageHandler;
  }

  /**
   * Parse a source program and generate the intermediate code.
   *
   * @throws IOException if an error occurred.
   */
  public ICodeNode parse() throws Exception {
    StatementParser statementParser = new StatementParser(this);
    Token token = currentToken();
    ICodeNode iCodeNode = null;

    // the parser use the statement parser to parse
    try {
      iCodeNode = statementParser.parse(token);
    } catch(IOException ex) {
      errorHandler.abortTranslation(IO_ERROR, this);
    }

    return iCodeNode;
  }

  /**
   * match expected token and get the next token.
   * @param expectedTokenType the type of expected token.
   * @return the next token.
   * @throws IOException if an error occurred.
   */
  public Token match(TokenType expectedTokenType) throws IOException {
    if (currentToken().getType() != expectedTokenType)
      errorHandler.flag(currentToken(), UNEXPECTED_TOKEN, this);
    return nextToken();
  }

  /**
   * Synchronize the parser.
   * @param syncSet the set of token types for synchronizing the parser.
   * @return the token where the parser has synchronized.
   * @throws IOException if an error occurred.
   */
  public Token synchronize(EnumSet syncSet) throws IOException {
    Token token = currentToken();

    if (!syncSet.contains(token.getType())) {
      // Flag the unexpected token.
      errorHandler.flag(token, UNEXPECTED_TOKEN, this);

      // Recover by skipping tokens that are not in the syncSet
      do {
        token = nextToken();
      } while (!(token instanceof EofToken) && !syncSet.contains(token.getType()));
    }
    return token;
  }


  /**
   * @return the error count.
   */
  public int getErrorCount() {
    return errorHandler.getErrorCount();
  }

  /**
   * Clear the count of errors.
   */
  public void clearErrorCount() {
    errorHandler.clearErrorCount();
  }

  /**
   * @return the current token.
   */
  public Token currentToken() {
    return scanner.currentToken();
  }

  /**
   * @return the next token.
   * @throws IOException if an error occurred.
   */
  public Token nextToken() throws IOException {
    return scanner.nextToken();
  }

  /**
   * @param listener the message listener to add.
   */
  public void addMessageListener(MessageListener listener) {
    messageHandler.addListener(listener);
  }

  /**
   * @param listener the message listener to remove.
   */
  public void removeMessageListener(MessageListener listener) {
    messageHandler.removeListener(listener);
  }

  /**
   * @param message the message to set.
   */
  public void sendMessage(Message message) {
    messageHandler.sendMessage(message);
  }
}