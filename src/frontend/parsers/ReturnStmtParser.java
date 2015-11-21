package frontend.parsers;

import static frontend.TokenType.END;
import static frontend.TokenType.END_OF_LINE;
import static frontend.TokenType.RETURN;
import static intermediate.ICodeNodeType.EXPRESSION_NODE;
import static intermediate.ICodeNodeType.RETURN_STATEMENT;

import frontend.Token;
import frontend.Parser;
import intermediate.ICode;
import intermediate.ICodeNode;
import intermediate.ICodeFactory;

/**
 * <h1>ReturnStatementParser</h1>
 * <p>
 * <p>return statement parser</p>
 */
public class ReturnStmtParser extends StatementParser {
  /**
   * @param parent get scanner from parent.
   */
  public ReturnStmtParser(Parser parent) {
    super(parent);
  }

  /**
   * @param token the token to start parse.
   * @return return statement intermediate code node.
   * @throws Exception if an error occurred.
   */
  @Override
  public ICodeNode parse(Token token) throws Exception {
    ICodeNode iCodeNode = ICodeFactory.createICodeNode(RETURN_STATEMENT);

    token = match(RETURN);  // consume return
    // parse expression
    ExprStmtParser exprParser = new ExprStmtParser(this);
    ICodeNode exprNode = exprParser.parseExpr(token);

    iCodeNode.addChild(exprNode);
    match(END_OF_LINE);
    return iCodeNode;
  }
}