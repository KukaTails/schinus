package interpreter;

import static objectmodel.predefined.PredefinedConstant.NONE;
import static objectmodel.predefined.PredefinedConstant.NO_PRINT;

import objectmodel.baseclasses.Class;
import objectmodel.baseclasses.Instance;
import objectmodel.baseclasses.MethodInstance;

/**
 * <h1>ExecuteResultPrinter</h1>
 *
 * <p>print the result executed by interpreter.</p>
 */
public class ExecuteResultPrinter {
  /**
   * Print the result executed by interpreter.
   * @param object the result object executed by interpreter.
   */
  public void printObject(Object object) {
    if (object == NO_PRINT)
      return;
    if (object == NONE) {
      System.out.print("None");
    } else if (object instanceof Class) {
      String stringPrinted = "<class \"" + ((Class)object).getName() + "\">";
      System.out.print(stringPrinted);
    } else if (object instanceof MethodInstance) {
      String functionName = ((MethodInstance)object).getName();
      System.out.print("<function " + functionName + " at " + object.hashCode() + '>');
    }else if (object instanceof Instance) {
      System.out.print(((Instance) object).readAttribute("__value__"));
    }
  }
}
