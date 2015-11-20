package interpreter.executors;

import static intermediate.ICodeKey.FIELD_NAME;
import static intermediate.ICodeKey.VARIABLE_FIELD;
import static intermediate.ICodeKey.PARAMETERS_LIST;
import static objectmodel.predefined.PredefinedType.FLOAT;
import static objectmodel.predefined.PredefinedType.INTEGER;

import intermediate.ICodeNode;
import objectmodel.baseclasses.Base;
import objectmodel.baseclasses.Instance;
import objectmodel.baseclasses.MethodInstance;
import objectmodel.dictionary.Dictionary;

import java.util.ArrayList;

/**
 * <h1>FunctionCallExecutor</h1>
 * <p>Function call statement executor.</p>
 */
public class FuncCallExecutor extends StmtExecutor {
  public FuncCallExecutor() {
  }

  /**
   * @param iCodeNode the intermediate code node of function statement.
   * @param environment the environment used to execute function call statement.
   * @return result of executing function call statement.
   */
  public Object execute(ICodeNode iCodeNode, Dictionary environment) {
    ArrayList<ICodeNode> children = iCodeNode.getChildren();
    ICodeNode variableCalled = children.get(0);
    ICodeNode parameters = children.get(1);

    // get list of parameters name
    ArrayList<String> parametersName = (ArrayList) parameters.getAttribute(PARAMETERS_LIST);

    String funcName = (String) variableCalled.getAttribute(FIELD_NAME);
    ArrayList<ICodeNode> fields = (ArrayList) variableCalled.getAttribute(VARIABLE_FIELD);

    // get the function name called
    if (fields.size() != 0) {
      ICodeNode lastFieldNode = fields.get(fields.size() - 1);
      funcName = (String) lastFieldNode.getAttribute(FIELD_NAME);
    }

    // executor variable expression and get the environment that contains function that will be called
    VariableExecutor variableExecutor = new VariableExecutor();
    Dictionary objectCalledExistDict = (Dictionary) variableExecutor.execute(variableCalled, environment);
    Object objectCalled = objectCalledExistDict.get(funcName);

    // if the function can not be found, report runtime error and stop executing the statement.
    if (objectCalled == null || !(objectCalled instanceof MethodInstance)) {
      return null;
      // TODO need to report wrong
    }

    // get parameter objects of function
    MethodInstance methodCalled = (MethodInstance) objectCalled;
    ArrayList<Object> parametersObject = new ArrayList<>();

    // get the expression value of parameters
    // only support a number or a name of identifier
    for (String parameter : parametersName) {
      Object param = null;

      if (Character.isDigit(parameter.charAt(0))) {
        if (parameter.indexOf('.') != -1) {
          Float floatValue = Float.parseFloat(parameter);
          param = new Instance(FLOAT, new Dictionary(), methodCalled.getFields());
          ((Base) param).writeAttr("value", floatValue);
        } else {
          Integer intValue = Integer.parseInt(parameter);
          param = new Instance(INTEGER, new Dictionary(), methodCalled.getFields());
          ((Base) param).writeAttr("value", intValue);
        }
      } else {
        param = environment.get(parameter);
      }

      if (param != null) {
        parametersObject.add(param);
      }
    }

    // call the method instance and get the result
    Object callResult = methodCalled.callMethod(parametersObject);
    return callResult;
  }
}