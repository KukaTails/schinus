package frontend.clang;

import static frontend.clang.ClangErrorCode.IO_ERROR;

import frontend.*;
import frontend.TokenType;
import frontend.clang.parsers.AsgnAndExpStatementParser;
import frontend.clang.parsers.StatementParser;
import intermediate.ICode;
import intermediate.ICodeFactory;
import intermediate.ICodeNode;

import java.beans.Statement;
import java.io.IOException;

/*
 * <h1>ClangParserTD</h1>
 * <p>
 * <p>The top-down C language parser.</p>
 */
public class ClangParserTD extends Parser {
  private ICode iCode = ICodeFactory.createICode();

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
  public ICodeNode parse() throws Exception {
    long startTime = System.currentTimeMillis();
    ICodeNode resultNode = null;

    try {
      Token token = currentToken();
      TokenType tokenType = token.getType();

      StatementParser statementParser = new StatementParser(this);
      resultNode = statementParser.parse(token);

      // parser statement again and again
      //AsgnAndExpStatementParser asgnAndExpStatementParser = new AsgnAndExpStatementParser(this);
      //statementCodeNode = asgnAndExpStatementParser.parse(token);
      //statementICode.setRoot(statementCodeNode);

      // TODO // stop send message
      // Send the parser summary message.
      // float elapsedTime = (System.currentTimeMillis() - startTime) / 1000f;
      //sendMessage(new Message(PARSER_SUMMARY, new Number[] {token.getLineNumber(), getErrorCount(), elapsedTime}));
    } catch(IOException ex) {
      errorHandler.abortTranslation(IO_ERROR, this);
    }
    iCode.setRoot(resultNode);
    return resultNode;
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
  public ICode getiCode() {
    return iCode;
  }
}