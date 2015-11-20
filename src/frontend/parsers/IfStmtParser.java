package frontend.parsers;

import static frontend.TokenType.*;
import static intermediate.ICodeNodeType.*;

import frontend.Token;
import frontend.Parser;
import intermediate.ICodeNode;
import intermediate.ICodeFactory;

/**
 * <h1>IfStatementParser</h1>
 * <p>If statement parser of Schinus language.</p>
 */
public class IfStmtParser extends StatementParser {
  public IfStmtParser(Parser parent) {
    super(parent);
  }

  /**
   * @param token the token to start parse.
   * @return intermediate code node of if statement.
   * @throws Exception if an error occurred.
   */
  public ICodeNode parse(Token token) throws Exception {
    ICodeNode ifStatementNode = ICodeFactory.createICodeNode(IF_STATEMENT);
    ExprStmtParser expressionParser = new ExprStmtParser(this);
    StatementParser statementParser = new StatementParser(this);

    token = nextToken(); // consume "if"

    ICodeNode ifBranchNode = ICodeFactory.createICodeNode(SELECTION_BRANCH);
    ICodeNode ifExpressionNode = expressionParser.parseTest(token);
    ICodeNode ifCompoundStatement = ICodeFactory.createICodeNode(COMPOUND_STATEMENT);

    ifBranchNode.addChild(ifExpressionNode);
    token = currentToken();
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
      ICodeNode elifBranchNode = ICodeFactory.createICodeNode(SELECTION_BRANCH);
      ICodeNode elifCompoundStatement = ICodeFactory.createICodeNode(COMPOUND_STATEMENT);

      token = nextToken(); // consume "elif"
      ICodeNode elifExpressionNode = expressionParser.parseTest(token);
      elifBranchNode.addChild(elifExpressionNode);
      // parse elif compound statements
      token = currentToken();
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
      ICodeNode elseBranchNode = ICodeFactory.createICodeNode(SELECTION_BRANCH);
      ICodeNode elseCompoundStatementNode = ICodeFactory.createICodeNode(COMPOUND_STATEMENT);

      token = nextToken();  // consume "else"
      token = nextToken(); // consume END_OF_LINE //TODO may flag error message

      // else statement has no expression
      while (currentToken().getType() != END) {
        ICodeNode statementNode = statementParser.parse(currentToken());
        elseCompoundStatementNode.addChild(statementNode);
      }
      elseBranchNode.addChild(elseCompoundStatementNode);
    }

    token = currentToken();
    if (token.getType() == END) {
      nextToken(); // consume END
      nextToken(); // consume END_OF_LINE;
      return ifStatementNode;
    }

    return ICodeFactory.createICodeNode(ERROR_NODE);
  }
}