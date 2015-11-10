package frontend;

import message.Message;
import message.MessageHandler;
import message.MessageListener;
import message.MessageProducer;
import intermediate.SymTabStack;
import intermediate.SymTabFactory;

/**
 * <h1>Parser</h1>
 * <p>
 * <p>A language-independent framework class.
 * This abstract parser class will be implemented by language-specific subclasses.</p>
 */
public abstract class Parser implements MessageProducer {
  protected static SymTabStack symTabStack;        // symbol table stack
  protected static MessageHandler messageHandler;  // message handler delegate

  static {
    symTabStack = SymTabFactory.createSymTabStack();
    messageHandler = new MessageHandler();
  }

  protected Scanner scanner;  // scanner used with this parser

  /**
   * Constructor.
   *
   * @param scanner the scanner to be used with this parser.
   */
  protected Parser(Scanner scanner) {
    this.scanner = scanner;
  }

  /**
   * Getter.
   *
   * @return the scanner used by this parser.
   */
  public Scanner getScanner() {
    return scanner;
  }

  /**
   * Getter.
   *
   * @return the symbol table stack generated by this parser.
   */
  public SymTabStack getSymTabStack() {
    return symTabStack;
  }

  /**
   * Getter.
   *
   * @return the message handler.
   */
  public MessageHandler getMessageHandler() {
    return messageHandler;
  }

  /**
   * Parse a source program and generate the intermediate code and the symbol
   * table.  To be implemented by a language-specific parser subclass.
   *
   * @throws Exception if an error occurred.
   */
  public abstract void parse() throws Exception;

  /**
   * Return the number of syntax errors found by the parser.
   * To be implemented by a language-specific parser subclass.
   *
   * @return the error count.
   */
  public abstract int getErrorCount();

  /**
   * Call the scanner's currentToken() method.
   *
   * @return the current token.
   */
  public Token currentToken() {
    return scanner.currentToken();
  }

  /**
   * Call the scanner's nextToken() method.
   *
   * @return the next token.
   * @throws Exception if an error occurred.
   */
  public Token nextToken() throws Exception {
    return scanner.nextToken();
  }

  /**
   * Add a parser message listener.
   *
   * @param listener the message listener to add.
   */
  public void addMessageListener(MessageListener listener) {
    messageHandler.addListener(listener);
  }

  /**
   * Remove a parser message listener.
   *
   * @param listener the message listener to remove.
   */
  public void removeMessageListener(MessageListener listener) {
    messageHandler.removeListener(listener);
  }

  /**
   * Notify listeners after setting the message.
   *
   * @param message the message to set.
   */
  public void sendMessage(Message message) {
    messageHandler.sendMessage(message);
  }
}