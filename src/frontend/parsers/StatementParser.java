package frontend.parsers;

import static frontend.TokenType.END_OF_LINE;
import static intermediate.ICodeNodeType.ERROR_NODE;
import static intermediate.ICodeNodeType.EMPTY_STATEMENT;

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
    ICodeNode stmtNode;

    try {
      switch(token.getType()) {

        case IF: {
          IfStmtParser ifStmtParser = new IfStmtParser(this);
          stmtNode = ifStmtParser.parse(token);
          break;
        }

        case WHILE: {
          WhileStmtParser whileStmtParser = new WhileStmtParser(this);
          stmtNode = whileStmtParser.parse(token);
          break;
        }

        case RETURN: {
          ReturnStmtParser returnStmtParser = new ReturnStmtParser(this);
          stmtNode = returnStmtParser.parse(token);
          break;
        }

        case DEF: {
          FuncDefStmtParser funcDefStmtParser = new FuncDefStmtParser(this);
          stmtNode = funcDefStmtParser.parse(token);
          break;
        }

        case CLASS: {
          ClassDefStmtParser classDefStmtParser = new ClassDefStmtParser(this);
          stmtNode = classDefStmtParser.parse(token);
          break;
        }

        case BREAK: {
          BreakStmtParser breakStmtParser = new BreakStmtParser(this);
          stmtNode = breakStmtParser.parse(token);
          break;
        }

        case END_OF_LINE: {
          match(END_OF_LINE);
          stmtNode = ICodeFactory.createICodeNode(EMPTY_STATEMENT);
          break;
        }

        default: {
          ExprStmtParser exprStmtParser = new ExprStmtParser(this);
          stmtNode = exprStmtParser.parse(token);
          break;
        }
      }
    } catch(SyntaxErrorException e) {
      stmtNode = ICodeFactory.createICodeNode(ERROR_NODE);
    }
    return stmtNode;
  }
}