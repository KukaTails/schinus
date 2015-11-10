package frontend.clang;

import static message.MessageType.PARSER_SUMMARY;
import static frontend.clang.ClangErrorCode.IO_ERROR;

import frontend.Token;
import frontend.Scanner;
import frontend.Parser;
import frontend.EofToken;
import frontend.clang.parsers.AsgnAndExpStatementParser;
import intermediate.ICode;
import intermediate.ICodeFactory;
import intermediate.ICodeNode;
import intermediate.icodeimpl.ICodeImpl;
import message.Message;

import java.io.IOException;

/*
 * <h1>ClangParserTD</h1>
 * <p>
 * <p>The top-down C language parser.</p>
 */
public class ClangParserTD extends Parser {
  private ICode statementICode = ICodeFactory.createICode();

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
    long startTime = System.currentTimeMillis();

    try {
      Token token = currentToken();
      ICodeNode statementCodeNode;

      // parser statement again and again
      AsgnAndExpStatementParser asgnAndExpStatementParser = new AsgnAndExpStatementParser(this);
      statementCodeNode = asgnAndExpStatementParser.parse(token);
      statementICode.setRoot(statementCodeNode);

      // TODO // stop send message
      // Send the parser summary message.
      // float elapsedTime = (System.currentTimeMillis() - startTime) / 1000f;
      //sendMessage(new Message(PARSER_SUMMARY, new Number[] {token.getLineNumber(), getErrorCount(), elapsedTime}));
    } catch(IOException ex) {
      errorHandler.abortTranslation(IO_ERROR, this);
    }
  }

  /**
   * Return the number of syntax errors found by the parser.
   *
   * @return the error count.
   */
  public int getErrorCount() {
    return errorHandler.getErrorCount();
  }

  /**
   * Getter.
   *
   * @return icode of statement for executor
   */
  public ICode getStatementICode() {
    return statementICode;
  }
}