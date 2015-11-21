package frontend.parsers;

import static frontend.TokenType.COMMA;
import static frontend.TokenType.IDENTIFIER;
import static frontend.TokenType.LEFT_PAREN;
import static frontend.TokenType.RIGHT_PAREN;
import static frontend.ErrorCode.UNEXPECTED_TOKEN;
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
   *
   * @param token the current token to start parse.
   * @return the code node of if statement.
   * @throws Exception if an error occurred.
   */
  public ICodeNode parse(Token token) throws Exception {
    ICodeNode parametersNode = ICodeFactory.createICodeNode(PARAMETERS);
    ArrayList<String> parameters = new ArrayList<>();

    token = match(LEFT_PAREN);  // consume '('
    while (token.getType() != RIGHT_PAREN) {
      parameters.add(token.getText());
      token = match(IDENTIFIER); // consume the identifier
      if (token.getType() == RIGHT_PAREN)
        break;

      // consume '(' or ','
      if (token.getType() == LEFT_PAREN)
        token = match(LEFT_PAREN);
      else if (token.getType() == COMMA)
        token = match(COMMA);
      else {
        token = nextToken();
        errorHandler.flag(token, UNEXPECTED_TOKEN, this);
      }
    }

    // if there is no parameter, the size of parameters is zero.
    parametersNode.setAttribute(PARAMETERS_LIST, parameters);

    match(RIGHT_PAREN);  // consume ')'
    return parametersNode;
  }
}