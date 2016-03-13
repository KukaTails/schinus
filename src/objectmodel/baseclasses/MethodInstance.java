package objectmodel.baseclasses;

import static intermediate.ICodeNodeType.RETURN_STATEMENT;
import static objectmodel.predefined.PredefinedType.FUNCTION;
import static objectmodel.predefined.PredefinedConstant.NO_PRINT;

import intermediate.ICodeNode;
import objectmodel.dictionary.Dictionary;
import interpreter.executors.StmtExecutor;
import interpreter.exception.SchinusException;
import interpreter.exception.ReturnFlowException;
import interpreter.exception.ParametersException;

import java.io.IOException;
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
   * @param methodName       the name of method.
   * @param parentDictionary the parent of method.
   * @param existedEnv       the environment which contains the object.
   * @param parameterCount   the number of parameters of the method.
   * @param parametersName   the name of parameters.
   * @param funcBodyCode     the intermediate code node of method.
   */
  public MethodInstance(String methodName, Dictionary parentDictionary, Dictionary existedEnv, int parameterCount,
      ArrayList<String> parametersName, ICodeNode funcBodyCode) {
    super(FUNCTION, new Dictionary(), parentDictionary, existedEnv);
    this.methodName = methodName;
    this.parameterCount = parameterCount;
    this.funcBodyCode = funcBodyCode;
    this.parametersName = parametersName;

    writeAttr("__name__", methodName);
    writeAttr("__code__", funcBodyCode);
  }

  /**
   * @param parameters the object of parameters
   * @return the result of executing method on parameters.
   */
  public Object callMethod(ArrayList<Object> parameters)
      throws SchinusException, IOException {
    Object result = NO_PRINT;
    StmtExecutor statementExecutor = new StmtExecutor();

    if (parameters.size() != parameterCount) {
      throw new ParametersException(methodName, parameterCount, parameters.size());
    }

    // put the parameters into dictionary, and set the
    // field of method to be the existedEnv of parameters
    for (int i = 0; i < parameterCount; ++i) {
      Base parameter = (Base) parameters.get(i);
      parameter.setExistedEnv(this.getFields());
      writeAttr(parametersName.get(i), parameter);
    }

    ArrayList<ICodeNode> statements = funcBodyCode.getChildren();
    try {
      for (ICodeNode statement : statements) {
        if (statement.getType() != RETURN_STATEMENT) {
          statementExecutor.execute(statement, getFields());
        } else {
          result = statementExecutor.execute(statement, getFields());
          throw new ReturnFlowException(result);
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