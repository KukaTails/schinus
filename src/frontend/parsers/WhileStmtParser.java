package frontend.parsers;

import static frontend.TokenType.END;
import static frontend.TokenType.WHILE;
import static frontend.TokenType.END_OF_LINE;
import static intermediate.ICodeNodeType.WHILE_STATEMENT;
import static intermediate.ICodeNodeType.COMPOUND_STATEMENT;

import frontend.Token;
import frontend.Parser;
import frontend.TokenType;
import intermediate.ICodeNode;
import intermediate.ICodeFactory;

import java.util.EnumSet;

/**
 * <h1>WhileStatementParser</h1>
 * <p>
 * <p>While statement parser.</p>
 */
public class WhileStmtParser extends StatementParser {
  /**
   * @param parent get scanner from parent.
   */
  public WhileStmtParser(Parser parent) {
    super(parent);
  }


  private static final EnumSet<TokenType> TEST_FIRST_SET = ExprStmtParser.EXPR_FIRST_SET.clone();
  /**
   * Parse while statement and return intermediate code node of while statement.
   *
   * @param token the token to start parse.
   * @return the intermediate code of while statement.
   * @throws Exception if an error occurred.
   */
  @Override
  public ICodeNode parse(Token token) throws Exception {
    ICodeNode iCodeNode = ICodeFactory.createICodeNode(WHILE_STATEMENT);

    token = match(WHILE);

    // parse expression statement of while
    ExprStmtParser expressionParser = new ExprStmtParser(this);

    token = synchronize(TEST_FIRST_SET);
    ICodeNode expression = expressionParser.parseExpr(token);
    iCodeNode.addChild(expression);

    // parse the body of while statement
    ICodeNode compoundStatement = ICodeFactory.createICodeNode(COMPOUND_STATEMENT);
    StatementParser statementParser = new StatementParser(this);

    match(END_OF_LINE);
    while ((token = currentToken()).getType() != END) {
      ICodeNode statementNode = statementParser.parse(token);
      compoundStatement.addChild(statementNode);
    }
    iCodeNode.addChild(compoundStatement);
    match(END);

    return iCodeNode;
  }
}