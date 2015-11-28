package frontend.parsers;

import static frontend.TokenType.BREAK;
import static intermediate.ICodeNodeType.BREAK_STMT_NODE;

import frontend.Token;
import frontend.Parser;
import intermediate.ICodeNode;
import intermediate.ICodeFactory;

/**
 * <h1>BreakStmtParser</h1>
 * <p>The parser used to parse break statement.</p>
 */
public class BreakStmtParser extends StatementParser {
  public BreakStmtParser(Parser parent) {
    super(parent);
  }

  /**
   * Break statement parser.
   * @param token token used to start parse.
   * @return the intermediate code node of break statement.
   * @throws Exception if an error occurred.
   */
  @Override
  public ICodeNode parse(Token token)
      throws Exception {
    ICodeNode breakStmtNode = ICodeFactory.createICodeNode(BREAK_STMT_NODE);

    match(BREAK);
    return breakStmtNode;
  }
}