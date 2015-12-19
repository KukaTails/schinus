package interpreter.executors;

import static intermediate.ICodeNodeType.ELSE_BRANCH;
import static objectmodel.predefined.PredefinedConstant.NO_PRINT;

import intermediate.ICodeNode;
import intermediate.ICodeNodeType;
import objectmodel.baseclasses.Instance;
import objectmodel.dictionary.Dictionary;
import interpreter.exception.SchinusException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * <h1>IfStatementExecutor</h1>
 * <p>
 * <p>If statement executor.</p>
 */
public class IfStmtExecutor extends StmtExecutor {
  public IfStmtExecutor() {
  }

  /**
   * @param iCodeNode   if statement code node
   * @param environment environment that used to execute if statement
   * @return the result of execute if statement
   */
  public Object execute(ICodeNode iCodeNode, Dictionary environment)
      throws SchinusException, IOException {
    ExprStmtExecutor exprStmtExecutor = new ExprStmtExecutor();
    StmtExecutor stmtExecutor = new StmtExecutor();
    Object executeResult = NO_PRINT;

    ArrayList<ICodeNode> branches = iCodeNode.getChildren();

    // execute all branches of if statement
    for (ICodeNode branch : branches) {
      ArrayList<ICodeNode> children = branch.getChildren();
      ICodeNodeType nodeType = branch.getType();
      ICodeNode conditionExpr = null;
      ICodeNode bodyNode;

      if (nodeType != ELSE_BRANCH) {
        conditionExpr = children.get(0);
        bodyNode = children.get(1);
      } else {
        bodyNode = children.get(0);
      }

      // execute condition expression of branches
      Object conditionResult;
      boolean result = true;

      if (nodeType != ELSE_BRANCH) {
        conditionResult = exprStmtExecutor.execute(conditionExpr, environment);
        result = checkConditionResult(conditionResult);
      }
      // execute one statement of branches or no statement
      if (result) {
        executeResult = stmtExecutor.execute(bodyNode, environment);
        break;
      }
    }

    return executeResult;
  }

  /**
   * Check the result of condition expression of all branches.
   * @param conditionResult result of condition result.
   * @return if the condition result if true or false
   */
  private boolean checkConditionResult(Object conditionResult) {
    if (conditionResult instanceof Instance) {
      conditionResult = ((Instance) conditionResult).readAttribute("__value__");
    }

    if (conditionResult instanceof Boolean) {
      return (Boolean) conditionResult;
    } else if (conditionResult instanceof Integer) {
      return ((Integer) conditionResult) != 0;
    } else if (conditionResult instanceof Float) {
      return ((Float) conditionResult) != 0.0;
    } else {
      return true;
    }
  }
}