package frontend.parsers;

import static frontend.TokenType.IF;
import static frontend.TokenType.END;
import static frontend.TokenType.ELIF;
import static frontend.TokenType.ELSE;
import static frontend.TokenType.END_OF_LINE;
import static intermediate.ICodeNodeType.IF_STATEMENT;
import static intermediate.ICodeNodeType.IF_BRANCH;
import static intermediate.ICodeNodeType.ELIF_BRANCH;
import static intermediate.ICodeNodeType.ELSE_BRANCH;
import static intermediate.ICodeNodeType.COMPOUND_STATEMENT;

import frontend.Token;
import frontend.Parser;
import frontend.TokenType;
import intermediate.ICodeNode;
import intermediate.ICodeFactory;

import java.util.EnumSet;

/**
 * <h1>IfStatementParser</h1>
 * <p>If statement parser of Schinus language.</p>
 */
public class IfStmtParser extends StatementParser {
  public IfStmtParser(Parser parent) {
    super(parent);
  }

  private static final EnumSet<TokenType> TEST_FIRST_SET = ExprStmtParser.EXPR_FIRST_SET.clone();

  /**
   * @param token the token to start parse.
   * @return intermediate code node of if statement.
   * @throws Exception if an error occurred.
   */
  @Override
  public ICodeNode parse(Token token) throws Exception {
    ICodeNode ifStatementNode = ICodeFactory.createICodeNode(IF_STATEMENT);
    ExprStmtParser expressionParser = new ExprStmtParser(this);
    StatementParser statementParser = new StatementParser(this);

    token = match(IF); // consume "if"
    token = synchronize(TEST_FIRST_SET);

    ICodeNode ifBranchNode = ICodeFactory.createICodeNode(IF_BRANCH);
    ICodeNode ifExpressionNode = expressionParser.parseExpr(token);
    ICodeNode ifCompoundStatement = ICodeFactory.createICodeNode(COMPOUND_STATEMENT);

    ifBranchNode.addChild(ifExpressionNode);
    token = match(END_OF_LINE);
    // parse the statements after <if expression EOL>
    while (token.getType() != ELIF && token.getType() != ELSE && token.getType() != END) {
      ICodeNode statementNode = statementParser.parse(token);
      ifCompoundStatement.addChild(statementNode);
      token = currentToken();
    }
    ifBranchNode.addChild(ifCompoundStatement);
    ifStatementNode.addChild(ifBranchNode);

    token = currentToken();
    while (token.getType() != ELSE && token.getType() != END) {
      ICodeNode elifBranchNode = ICodeFactory.createICodeNode(ELIF_BRANCH);
      ICodeNode elifCompoundStatement = ICodeFactory.createICodeNode(COMPOUND_STATEMENT);

      token = match(ELIF); // consume "elif"
      token = synchronize(TEST_FIRST_SET);

      ICodeNode elifExpressionNode = expressionParser.parseExpr(token);
      elifBranchNode.addChild(elifExpressionNode);
      // parse elif compound statements
      token = match(END_OF_LINE);  // consume end of line

      while (token.getType() != ELIF && token.getType() != ELSE && token.getType() != END) {
        ICodeNode statementNode = statementParser.parse(token);
        elifCompoundStatement.addChild(statementNode);
        token = currentToken();
      }
      elifBranchNode.addChild(elifCompoundStatement);
      ifStatementNode.addChild(elifBranchNode);
      token = currentToken();
    }

    token = currentToken();
    if (token.getType() != END) {
      ICodeNode elseBranchNode = ICodeFactory.createICodeNode(ELSE_BRANCH);
      ICodeNode elseCompoundStatementNode = ICodeFactory.createICodeNode(COMPOUND_STATEMENT);

      match(ELSE);         // consume "else"
      match(END_OF_LINE);  // consume END_OF_LINE

      // else statement has no expression
      while (currentToken().getType() != END) {
        ICodeNode statementNode = statementParser.parse(currentToken());
        elseCompoundStatementNode.addChild(statementNode);
      }
      elseBranchNode.addChild(elseCompoundStatementNode);
      ifStatementNode.addChild(elseBranchNode);
    }

    match(END);
    return ifStatementNode;
  }
}