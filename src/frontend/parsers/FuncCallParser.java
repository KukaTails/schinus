package frontend.parsers;

import static intermediate.ICodeNodeType.FUNCTION_CALL_STATEMENT;

import frontend.Token;
import frontend.Parser;
import intermediate.ICodeNode;
import intermediate.ICodeFactory;

/**
 * <h1>FuncCallParser</h1>
 *
 * <p></p>
 */
public class FuncCallParser extends StatementParser {
  public FuncCallParser(Parser parent) {
    super(parent);
  }

  public ICodeNode parse(Token token) throws Exception {
    ICodeNode functionCallNode = ICodeFactory.createICodeNode(FUNCTION_CALL_STATEMENT);
    VariableParser variableParser = new VariableParser(this);
    ParametersParser parameterListParser = new ParametersParser(this);

    token = nextToken(); // consume CALL token
    // parse variable to be called
    ICodeNode variableCalled = variableParser.parse(token);
    functionCallNode.addChild(variableCalled);

    // parse parameters
    token = currentToken();
    ICodeNode parameters = parameterListParser.parse(token);
    functionCallNode.addChild(parameters);

    return functionCallNode;
  }
}