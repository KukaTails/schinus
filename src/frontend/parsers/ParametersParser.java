package frontend.parsers;

import static frontend.TokenType.COLON;
import static frontend.TokenType.RIGHT_PAREN;
import static frontend.ErrorCode.MISSING_RIGHT_PAREN;
import static intermediate.ICodeKey.PARAMETERS_LIST;
import static intermediate.ICodeNodeType.PARAMETERS;

import frontend.Token;
import frontend.Parser;
import intermediate.ICodeNode;
import intermediate.ICodeFactory;

import java.util.ArrayList;

/**
 * <h1>ParameterListParser</h1>
 * <p>
 * <p>parse the parameters list of function define statement or function call statement</p>
 */
public class ParametersParser extends Parser {

  /**
   * @param parent the parser used to get scanner.
   */
  public ParametersParser(Parser parent) {
    super(parent);
  }

  /**
   * Parse parameter list of function define statement or function call statement.
   * @param token the current token to start parse.
   * @return the code node of if statement.
   * @throws Exception if an error occurred.
   */
  public ICodeNode parse(Token token) throws Exception {
    ICodeNode parametersNode = ICodeFactory.createICodeNode(PARAMETERS);
    ArrayList<String> parameters = new ArrayList<>();

    token = nextToken();  // consume '('
    while (token.getType() != RIGHT_PAREN && token.getType() != COLON) {
      parameters.add(token.getText());
      token = nextToken(); // consume the identifier
      if (token.getType() == RIGHT_PAREN || token.getType() == COLON)
        break;
      token = nextToken(); // consume '(' or ','
    }

    // if there is no parameter, the size of parameters is zero.
    parametersNode.setAttribute(PARAMETERS_LIST, parameters);

    // missing right paren of parameter list
    if (token.getType() == RIGHT_PAREN) {
      nextToken();  // consume ')'
    } else
      errorHandler.flag(token, MISSING_RIGHT_PAREN, this);
    return parametersNode;
  }
}