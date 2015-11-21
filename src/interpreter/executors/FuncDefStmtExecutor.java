package interpreter.executors;

import static intermediate.ICodeKey.PARAMETERS_LIST;
import static intermediate.ICodeKey.FUNCTION_NAME_KEY;

import intermediate.ICodeNode;
import objectmodel.dictionary.Dictionary;
import objectmodel.baseclasses.MethodInstance;

import java.util.ArrayList;

/**
 * <h1>FunctionDefStatementExecutor</h1>
 * <p>
 * <p>Function define statement executor.</p>
 */
public class FuncDefStmtExecutor extends StmtExecutor {
  public FuncDefStmtExecutor() {
  }

  /**
   * @param iCodeNode intermediate code node of function define statement
   * @param environment environment to execute the statement
   * @return result of executing the statement
   */
  public Object execute(ICodeNode iCodeNode, Dictionary environment) {
    MethodInstance functionInstance;
    ArrayList<ICodeNode> children = iCodeNode.getChildren();
    ICodeNode functionNameNode = children.get(0);
    ICodeNode functionParams = children.get(1);
    ICodeNode functionBody = children.get(2);

    // get function name
    String funcName = (String) functionNameNode.getAttribute(FUNCTION_NAME_KEY);

    // get the parameters
    ArrayList<String> paramsName = (ArrayList) functionParams.getAttribute(PARAMETERS_LIST);

    // put the function into environment
    functionInstance = new MethodInstance(funcName, environment, environment, paramsName.size(), paramsName, functionBody);
    environment.put(funcName, functionInstance);
    return null;
  }
}