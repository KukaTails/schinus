package frontend.parsers;

import static frontend.TokenType.END;
import static intermediate.ICodeNodeType.WHILE_STATEMENT;
import static intermediate.ICodeNodeType.COMPOUND_STATEMENT;

import frontend.Token;
import frontend.Parser;
import intermediate.ICodeNode;
import intermediate.ICodeFactory;

/**
 * <h1>WhileStatementParser</h1>
 *
 * <p>While statement parser.</p>
 */
public class WhileStmtParser extends StatementParser {
  /**
   * @param parent get scanner from parent.
   */
  public WhileStmtParser(Parser parent) {
    super(parent);
  }

  /**
   * Parse while statement and return intermediate code node of while statement.
   * @param token the token to start parse.
   * @return the intermediate code of while statement.
   * @throws Exception if an error occurred.
   */
  @Override
  public ICodeNode parse(Token token) throws Exception {
    ICodeNode iCodeNode = ICodeFactory.createICodeNode(WHILE_STATEMENT);

    token = nextToken();  // consume token WHILE

    // parse expression statement of while
    ExprStmtParser expressionParser = new ExprStmtParser(this);
    ICodeNode expression = expressionParser.parseTest(token);
    iCodeNode.addChild(expression);

    // parse the body of while statement
    ICodeNode compoundStatement = ICodeFactory.createICodeNode(COMPOUND_STATEMENT);
    StatementParser statementParser = new StatementParser(this);

    while ((token = currentToken()).getType() != END) {
      ICodeNode statementNode = statementParser.parse(token);
      compoundStatement.addChild(statementNode);
    }

    if (currentToken().getType() == END)
      nextToken();  // consume END
    iCodeNode.addChild(compoundStatement);

    return iCodeNode;
  }
}