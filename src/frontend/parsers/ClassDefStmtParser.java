package frontend.parsers;

import static frontend.TokenType.END;
import static frontend.TokenType.LEFT_PAREN;
import static frontend.TokenType.RIGHT_PAREN;
import static intermediate.ICodeKey.CLASS_NAME;
import static intermediate.ICodeKey.BASE_CLASSES_NAME;
import static intermediate.ICodeNodeType.EMPTY_STATEMENT;
import static intermediate.ICodeNodeType.CLASS_DEFINE_BODY;
import static intermediate.ICodeNodeType.BASE_CLASSES_LIST;
import static intermediate.ICodeNodeType.CLASS_DEFINE_STATEMENT;

import frontend.Token;
import frontend.Parser;
import intermediate.ICodeNode;
import intermediate.ICodeFactory;

import java.util.ArrayList;

/**
 * <h1>ClassDefStmtParser</h1>
 *
 * <p>Class define statement parser.</p>
 */
public class ClassDefStmtParser extends StatementParser {
  public ClassDefStmtParser(Parser parent) {
    super(parent);
  }

  /**
   * @param token token used to start parse.
   * @return intermediate code node of class define statement.
   * @throws Exception if an error occurred.
   */
  public ICodeNode parse(Token token) throws Exception {
    ICodeNode iCodeNode = ICodeFactory.createICodeNode(CLASS_DEFINE_STATEMENT);

    token = nextToken();  // consume CLASS
    iCodeNode.setAttribute(CLASS_NAME, token.getText());

    token = nextToken();  // consume class name
    ICodeNode arglistNode = ICodeFactory.createICodeNode(BASE_CLASSES_LIST);
    ArrayList<String> baseClasses = new ArrayList<>();
    if (token.getType() == LEFT_PAREN) {
      token = nextToken();  // consume left paren
      while (token.getType() != RIGHT_PAREN) {
        baseClasses.add(token.getText());
        token = nextToken();  // consume class name
        if (token.getType() == RIGHT_PAREN)
          break;
        token = nextToken(); // consume COMMA
      }
      nextToken();  // consume right paren
    }
    arglistNode.setAttribute(BASE_CLASSES_NAME, baseClasses);
    iCodeNode.addChild(arglistNode);

    token = nextToken();  // consume :
    token = nextToken();  // consume EOL
    ICodeNode classBodyNode = ICodeFactory.createICodeNode(CLASS_DEFINE_BODY);
    StatementParser statementParser = new StatementParser(this);
    while ((token = currentToken()).getType() != END) {
      ICodeNode stmtNode = statementParser.parse(token);
      if (stmtNode.getType() != EMPTY_STATEMENT) {
        classBodyNode.addChild(stmtNode);
      }
    }
    iCodeNode.addChild(classBodyNode);

    nextToken();  // consume END
    return iCodeNode;
  }
}