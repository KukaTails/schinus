package interpreter.executors;

import static intermediate.ICodeNodeType.RETURN_STATEMENT;
import static objectmodel.predefined.PredefinedConstant.NO_PRINT;

import intermediate.ICodeNode;
import intermediate.ICodeNodeType;
import objectmodel.baseclasses.Instance;
import objectmodel.dictionary.Dictionary;

import java.util.ArrayList;

/**
 * <h1>WhileStatementExecutor</h1>
 *
 * <p>While statement executor.</p>
 */
public class WhileStmtExecutor extends StmtExecutor {
  public WhileStmtExecutor() {
  }

  /**
   * @param iCodeNode the intermediate code node of while statement.
   * @param environment the environment to execute while statement.
   * @return the result of executing while statement.
   */
  @Override
  public Object execute(ICodeNode iCodeNode, Dictionary environment) {
    ICodeNode expressionNode = iCodeNode.getChildren().get(0);
    ICodeNode compoundStatementNode = iCodeNode.getChildren().get(1);

    try {
      while (true) {
        ExprStmtExecutor expressionStatementExecutor = new ExprStmtExecutor();
        Object expressionResult = expressionStatementExecutor.execute(expressionNode, environment);

        // if the result of executing condition expression is false,
        // stop execute compound statement.
        if (!checkConditionResult(expressionResult))
          break;
        executeCompoundStmt(compoundStatementNode, environment);
      }
    }catch(Exception e) {
      System.out.println(e);
    }
    return null;
  }

  /**
   * Execute compound statement.
   * @param compoundNode intermediate code node of compound statement.
   * @param environment environment to execute compound statement.
   */
  private Object executeCompoundStmt(ICodeNode compoundNode, Dictionary environment) {
    ArrayList<ICodeNode> stmtsNode = compoundNode.getChildren();
    StmtExecutor stmtExecutor = new StmtExecutor();
    Object result = NO_PRINT;
    for (ICodeNode stmtNode : stmtsNode) {
      result = stmtExecutor.execute(stmtNode, environment);
      if (stmtNode.getType() == RETURN_STATEMENT)
        break;
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
      conditionResult = ((Instance)conditionResult).readAttribute("__value__");

    if (conditionResult instanceof Boolean) {
      return (Boolean) conditionResult;
    } else if (conditionResult instanceof Integer) {
      return ((Integer) conditionResult) != 0;
    } else if (conditionResult instanceof Float) {
      return ((Float) conditionResult) == 0.0;
    } else if (conditionResult instanceof String) {
      return (String)conditionResult != "";
    } else {
      return true;
    }
  }
}
