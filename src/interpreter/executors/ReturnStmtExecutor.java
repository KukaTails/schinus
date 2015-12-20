package interpreter.executors;

import intermediate.ICodeNode;
import objectmodel.dictionary.Dictionary;
import interpreter.exception.SchinusException;

import java.io.IOException;

/**
 * <h1>ReturnStatementExecutor</h1>
 */
public class ReturnStmtExecutor extends StmtExecutor {
  /**
   * Execute a return statement and return the object of executing return statement.
   * @param iCodeNode   the return statement intermediate code node.
   * @param environment the environment used to execute return statement.
   * @return the result of executing return statement.
   */
  public Object execute(ICodeNode iCodeNode, Dictionary environment)
      throws SchinusException, IOException {
    ICodeNode expressionNode = iCodeNode.getChildren().get(0);
    ExprStmtExecutor exprStmtExecutor = new ExprStmtExecutor();

    return exprStmtExecutor.execute(expressionNode, environment);
  }
}