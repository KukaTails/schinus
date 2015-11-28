package interpreter.executors;

import intermediate.ICodeNode;
import interpreter.exception.BreakFlowException;
import interpreter.exception.SchinusException;
import objectmodel.dictionary.Dictionary;

/**
 * <h1>BreakStmtExecutor</h1>
 * <p>
 * <p>break statement executor.</p>
 */
public class BreakStmtExecutor extends StmtExecutor {
  public BreakStmtExecutor() {
  }

  /**
   * Execute break statement.
   * @param iCodeNode the intermediate code node of break statement.
   * @param environment the environment to execute break statement.
   * @return NO_PRINT will be returned.
   * @throws SchinusException BreakFlowException will be throwed.
   */
  @Override
  public Object execute(ICodeNode iCodeNode, Dictionary environment)
      throws SchinusException {
    throw new BreakFlowException();
  }
}
