package frontend.parsers;

import static frontend.TokenType.END_OF_LINE;
import static intermediate.ICodeNodeType.EMPTY_STATEMENT;
import static intermediate.ICodeNodeType.ERROR_NODE;

import frontend.Token;
import frontend.Parser;
import intermediate.ICodeNode;
import intermediate.ICodeFactory;
import interpreter.exception.SyntaxErrorException;

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

  /**
   * @param token token used to start parse.
   * @return intermediate code node of statement.
   * @throws Exception if an error occurred.
   */
  public ICodeNode parse(Token token) throws Exception {
    ICodeNode statementNode;

    try {
      switch(token.getType()) {

        case IF: {
          IfStmtParser ifStatementParser = new IfStmtParser(this);
          statementNode = ifStatementParser.parse(token);
          break;
        }

        case WHILE: {
          WhileStmtParser whileStmtParser = new WhileStmtParser(this);
          statementNode = whileStmtParser.parse(token);
          break;
        }

        case RETURN: {
          ReturnStmtParser returnStmtParser = new ReturnStmtParser(this);
          statementNode = returnStmtParser.parse(token);
          break;
        }

        case DEF: {
          FuncDefStmtParser functionDefStatementParser = new FuncDefStmtParser(this);
          statementNode = functionDefStatementParser.parse(token);
          break;
        }

        case CLASS: {
          ClassDefStmtParser classDefStmtParser = new ClassDefStmtParser(this);
          statementNode = classDefStmtParser.parse(token);
          break;
        }

        case END_OF_LINE: {
          match(END_OF_LINE);  // consume end of line
          statementNode = ICodeFactory.createICodeNode(EMPTY_STATEMENT);
          break;
        }

        default: {
          ExprStmtParser expressionParser = new ExprStmtParser(this);
          statementNode = expressionParser.parse(token);
          break;
        }
      }
    } catch(SyntaxErrorException e) {
      statementNode = ICodeFactory.createICodeNode(ERROR_NODE);
    }
    return statementNode;
  }
}