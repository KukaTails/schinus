package frontend.parsers;

import static intermediate.ICodeNodeType.EMPTY_STATEMENT;

import frontend.Token;
import frontend.Parser;
import frontend.TokenType;
import intermediate.ICodeNode;
import intermediate.ICodeFactory;

/**
 * <h1>StatementParser</h1>
 * <p>
 * <p>Parser statements.</p>
 */
public class StatementParser extends Parser {
  /**
   * @param parent the parent parser.
   */
  public StatementParser(Parser parent) {
    super(parent);
  }

  public ICodeNode parse(Token token) throws Exception {
    ICodeNode statementNode = null;

    switch((TokenType) token.getType()) {
      case END_OF_LINE: {
        nextToken();  // consume END_OF_LINE
        statementNode = ICodeFactory.createICodeNode(EMPTY_STATEMENT);
        break;
      }

      case IF: {
        IfStmtParser ifStatementParser = new IfStmtParser(this);
        statementNode = ifStatementParser.parse(token);
        break;
      }

      case DEF: {
        FuncDefStmtParser functionDefStatementParser = new FuncDefStmtParser(this);
        statementNode = functionDefStatementParser.parse(token);
        break;
      }

      case RETURN: {
        ReturnStmtParser returnStmtParser = new ReturnStmtParser(this);
        statementNode = returnStmtParser.parse(token);
        break;
      }

      case WHILE: {
        WhileStmtParser whileStmtParser = new WhileStmtParser(this);
        statementNode = whileStmtParser.parse(token);
        break;
      }

      case CLASS: {
        ClassDefStmtParser classDefStmtParser = new ClassDefStmtParser(this);
        statementNode = classDefStmtParser.parse(token);
        break;
      }

      default: {
        ExprStmtParser expressionParser = new ExprStmtParser(this);
        statementNode = expressionParser.parse(token);
        break;
      }
    }
    return statementNode;
  }
}