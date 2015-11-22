package objectmodel.baseclasses;

import static intermediate.ICodeNodeType.RETURN_STATEMENT;
import static objectmodel.predefined.PredefinedConstant.NONE;
import static objectmodel.predefined.PredefinedType.FUNCTION;

import intermediate.ICodeNode;
import interpreter.exception.ReturnFlowException;
import objectmodel.dictionary.Dictionary;
import interpreter.executors.StmtExecutor;

import java.util.ArrayList;

/**
 * <h1>MethodInstance</h1>
 * <p>MethodInstance class is the class of all method defined by user or predefined.</p>
 */
public class MethodInstance extends Instance {
  private String methodName;                // name of method
  private int parameterCount = 0;           // the number of parameters
  private ArrayList<String> parametersName; // the names of parameters
  ICodeNode funcBodyCode;                   // the intermediate code node of function body

  /**
   * @param methodName the name of method.
   * @param parentDictionary the parent of method.
   * @param existedEnv the environment which contains the object.
   * @param parameterCount the number of parameters of the method.
   * @param parametersName the name of parameters.
   * @param funcBodyCode the intermediate code node of method.
   */
  public MethodInstance(String methodName, Dictionary parentDictionary,
                        Dictionary existedEnv, int parameterCount, ArrayList<String> parametersName, ICodeNode funcBodyCode) {
    // all methods are instance of function
    super(FUNCTION, new Dictionary(), parentDictionary, existedEnv);
    this.methodName = methodName;
    this.parameterCount = parameterCount;
    this.funcBodyCode = funcBodyCode;
    this.parametersName = parametersName;
    // method instance has a attribute called "__code__"
    writeAttr("__code__", funcBodyCode);
  }

  /**
   * @param parameters the object of parameters
   * @return the result of executing method on parameters.
   */
  public Object callMethod(ArrayList<Object> parameters)
      throws ReturnFlowException {
    // if the number of parameters doesn't equal to parameterCount,
    // return None.
    if (parameters.size() != parameterCount) {
      return NONE;
    }

    // put the parameters into dictionary,
    // and set the field of method to be the existedEnv of parameters
    for (int i = 0; i < parameterCount; ++i) {
      Base parameter = (Base)parameters.get(i);
      parameter.setExistedEnv(this.getFields());
      writeAttr(parametersName.get(i), parameter);
    }

    StmtExecutor statementExecutor = new StmtExecutor();
    Object result = NONE;
    ArrayList<ICodeNode> statements = funcBodyCode.getChildren();

    try {
      // return the result of return statement
      for (ICodeNode statement : statements) {
        if (statement.getType() == RETURN_STATEMENT) {
          result = statementExecutor.execute(statement, getFields());
        } else {
          statementExecutor.execute(statement, getFields());
        }
      }
    } catch(ReturnFlowException e) {
      return e.getReturnValue();
    }
    return result;
  }

  /**
   * @return the name of method
   */
  public String getName() {
    return methodName;
  }
}