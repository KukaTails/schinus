package interpreter.executors;

import static objectmodel.predefined.PredefinedConstant.NO_PRINT;

import intermediate.ICodeNode;
import objectmodel.baseclasses.Instance;
import objectmodel.dictionary.Dictionary;
import interpreter.exception.SchinusException;
import interpreter.exception.BreakFlowException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * <h1>WhileStatementExecutor</h1>
 * <p>
 * <p>While statement executor.</p>
 */
public class WhileStmtExecutor extends StmtExecutor {
  public WhileStmtExecutor() {
  }

  /**
   * @param iCodeNode   the intermediate code node of while statement.
   * @param environment the environment to execute while statement.
   * @return the result of executing while statement.
   */
  @Override
  public Object execute(ICodeNode iCodeNode, Dictionary environment)
      throws SchinusException, IOException {
    ExprStmtExecutor exprStmtExecutor = new ExprStmtExecutor();
    StmtExecutor stmtExecutor = new StmtExecutor();

    ICodeNode expressionNode = iCodeNode.getChildren().get(0);
    ICodeNode compoundStatementNode = iCodeNode.getChildren().get(1);

    while (true) {
      Object expressionResult = exprStmtExecutor.execute(expressionNode, environment);

      if (!checkConditionResult(expressionResult))
        break;
      stmtExecutor.execute(compoundStatementNode, environment);
    }
    return null;
  }

  /**
   * Execute compound statement.
   *
   * @param compoundNode intermediate code node of compound statement.
   * @param environment  environment to execute compound statement.
   */
  private Object executeCompoundStmt(ICodeNode compoundNode, Dictionary environment)
      throws SchinusException, IOException {
    ArrayList<ICodeNode> stmtsNode = compoundNode.getChildren();
    StmtExecutor stmtExecutor = new StmtExecutor();
    Object result = NO_PRINT;

    try {
      for (ICodeNode stmtNode : stmtsNode) {
        result = stmtExecutor.execute(stmtNode, environment);
      }
    } catch(BreakFlowException e) {
      return result;
    }
    return result;
  }

  /**
   * Check the result of condition expression of all branches.
   *
   * @param conditionResult result of condition result.
   * @return if the condition result if true or false
   */
  private boolean checkConditionResult(Object conditionResult) {
    if (conditionResult instanceof Instance)
      conditionResult = ((Instance) conditionResult).readAttribute("__value__");

    if (conditionResult instanceof Boolean) {
      return (Boolean) conditionResult;
    } else if (conditionResult instanceof Integer) {
      return ((Integer) conditionResult) != 0;
    } else if (conditionResult instanceof Float) {
      return ((Float) conditionResult) == 0.0;
    } else if (conditionResult instanceof String) {
      return conditionResult != "";
    } else {
      return true;
    }
  }
}
