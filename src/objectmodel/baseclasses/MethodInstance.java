package objectmodel.baseclasses;

import static intermediate.ICodeNodeType.RETURN_STATEMENT;
import static objectmodel.predefined.PredefinedConstant.NONE;
import static objectmodel.predefined.PredefinedType.FUNCTION;

import intermediate.ICodeNode;
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
   * @param methodName       the name of method
   * @param parentDictionary the parent dictionary of method
   * @param parameterCount   the number of parameters
   * @param parametersName   the names of parameters
   * @param funcBodyCode     the intermediate code node of function body
   */
  public MethodInstance(String methodName, Dictionary parentDictionary, int parameterCount, ArrayList<String> parametersName, ICodeNode funcBodyCode) {
    // all methods are instance of function
    super(FUNCTION, new Dictionary(), parentDictionary);
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
  public Object callMethod(ArrayList<Object> parameters) {
    // if the number of parameters doesn't equal to parameterCount,
    // return None.
    if (parameters.size() != parameterCount) {
      return NONE;
    }

    // put the parameters into dictionary
    for (int i = 0; i < parameterCount; ++i) {
      writeAttr(parametersName.get(i), parameters.get(i));
    }

    StmtExecutor statementExecutor = new StmtExecutor();
    Object returnStatementResult = NONE;
    Object statementResult = NONE;

    ArrayList<ICodeNode> statements = funcBodyCode.getChildren();

    // if there is a return statement, and the result of return statement will be returned.
    // if there is no return statement, the result of last statement will be returned.
    for (ICodeNode statement : statements) {
      if (statement.getType() == RETURN_STATEMENT) {
        returnStatementResult = statementExecutor.execute(statement, getFields());
      } else {
        statementResult = statementExecutor.execute(statement, getFields());
      }
    }
    return returnStatementResult == null ? statementResult : returnStatementResult;
  }

  /**
   * @return the name of method
   */
  public String getName() {
    return methodName;
  }
}