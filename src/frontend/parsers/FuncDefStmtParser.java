package frontend.parsers;

import static frontend.TokenType.END;
import static intermediate.ICodeKey.FUNCTION_NAME_KEY;
import static intermediate.ICodeNodeType.FUNCTION_NAME;
import static intermediate.ICodeNodeType.FUNCTION_BODY;
import static intermediate.ICodeNodeType.EMPTY_STATEMENT;
import static intermediate.ICodeNodeType.FUNCTION_DEFINE_STATEMENT;

import frontend.Token;
import frontend.Parser;
import intermediate.ICodeNode;
import intermediate.ICodeFactory;

/**
 * <h1>FuncDefStmtParser</h1>
 *
 * <p>Function define statement parser.</p>
 */
public class FuncDefStmtParser extends StatementParser {
  public FuncDefStmtParser(Parser parent) {
    super(parent);
  }

  /**
   * @param token the token used to start parse.
   * @return intermediate code node of function define statement.
   * @throws Exception if an error occurred.
   */
  public ICodeNode parse(Token token) throws Exception {
    ICodeNode functionDefineNode = ICodeFactory.createICodeNode(FUNCTION_DEFINE_STATEMENT);

    token = nextToken(); // consume "def"

    //VariableParser variableParser = new VariableParser(this);
    ICodeNode functionNameNode = ICodeFactory.createICodeNode(FUNCTION_NAME);
    // parse the name of function
    String functionName = token.getText();
    functionNameNode.setAttribute(FUNCTION_NAME_KEY, functionName);
    functionDefineNode.addChild(functionNameNode);

    // parse parameter list
    token = nextToken(); // consume the name of token
    ParametersParser parameterListParser = new ParametersParser(this);
    ICodeNode functionParameters = parameterListParser.parse(token);
    functionDefineNode.addChild(functionParameters);

    token = nextToken(); // consume ':'
    token = nextToken(); // consume '\n'

    ICodeNode functionBodyNode = ICodeFactory.createICodeNode(FUNCTION_BODY);
    StatementParser statementParser = new StatementParser(this);

    // parse function body
    token = currentToken();
    while (token.getType() != END) {
      ICodeNode statement = statementParser.parse(token);
      if (statement.getType() != EMPTY_STATEMENT) {
        functionBodyNode.addChild(statement);
      }
      token = currentToken();
    }
    functionDefineNode.addChild(functionBodyNode);

    if (token.getType() == END) {
      nextToken(); // consume END
      nextToken(); // consume END_OF_LINE
      return functionDefineNode;
    }
    return null;
  }
}