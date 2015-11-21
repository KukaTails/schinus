package interpreter.executors;

import static intermediate.ICodeKey.LINE;
import static message.MessageType.SOURCE_LINE;
import static intermediate.ICodeNodeType.RETURN_STATEMENT;
import static objectmodel.predefined.PredefinedConstant.NO_PRINT;
import static interpreter.RuntimeErrorCode.UNIMPLEMENTED_FEATURE;

import message.Message;
import interpreter.Executor;
import intermediate.ICodeNode;
import intermediate.ICodeNodeType;
import objectmodel.dictionary.Dictionary;

import java.util.ArrayList;

/**
 * <h1>StatementExecutor</h1>
 * <p>
 * <p>Execute a statement.</p>
 */
public class StmtExecutor extends Executor {
  public StmtExecutor() {
  }

  /**
   * Execute a statement.
   * To be overridden by the specialized statement executor subclasses.
   *
   * @param node the root node of the statement.
   * @return null.
   */
  public Object execute(ICodeNode node, Dictionary environment) {
    ICodeNodeType nodeType = node.getType();
    Object result = NO_PRINT;

    try {

      switch(nodeType) {
        case ATOM_EXPR_NODE:
        case ASSIGN_EXP_NODE:
        case EXPRESSION_NODE: {
          ExprStmtExecutor expressionStatementExecutor = new ExprStmtExecutor();
          result = expressionStatementExecutor.execute(node, environment);
          break;
        }

        case IF_STATEMENT: {
          IfStmtExecutor ifStmtExecutor = new IfStmtExecutor();
          result = ifStmtExecutor.execute(node, environment);
          break;
        }

        case WHILE_STATEMENT: {
          WhileStmtExecutor whileStmtExecutor = new WhileStmtExecutor();
          result = whileStmtExecutor.execute(node, environment);
          break;
        }

        case COMPOUND_STATEMENT: {
          StmtExecutor statementExecutor = new StmtExecutor();
          ArrayList<ICodeNode> statements = node.getChildren();

          for (ICodeNode statement : statements) {
            result = statementExecutor.execute(statement, environment);
            if (statement.getType() == RETURN_STATEMENT)
              break;
          }
          break;
        }

        case FUNCTION_DEFINE_STATEMENT: {
          FuncDefStmtExecutor functionDefStatementExecutor = new FuncDefStmtExecutor();
          result = functionDefStatementExecutor.execute(node, environment);
          break;
        }

        case CLASS_DEFINE_STATEMENT: {
          ClassDefStmtExecutor classDefStmtExecutor = new ClassDefStmtExecutor();
          result = classDefStmtExecutor.execute(node, environment);
          break;
        }

        case RETURN_STATEMENT: {
          ReturnStmtExecutor returnStatementExecutor = new ReturnStmtExecutor();
          result = returnStatementExecutor.execute(node, environment);
          break;
        }

        case EMPTY_STATEMENT: {
          return NO_PRINT;
        }

        default: {
          errorHandler.flag(node, UNIMPLEMENTED_FEATURE, this);
          return NO_PRINT;
        }
      }
    } catch(Exception e) {
      return result;
    }
    return result;
  }

  /**
   * @param node the statement node.
   */
  private void sendSourceLineMessage(ICodeNode node) {
    Object lineNumber = node.getAttribute(LINE);

    // Send the SOURCE_LINE message.
    if (lineNumber != null) {
      sendMessage(new Message(SOURCE_LINE, lineNumber));
    }
  }
}