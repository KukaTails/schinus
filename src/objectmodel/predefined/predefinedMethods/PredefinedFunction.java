package objectmodel.predefined.predefinedMethods;

import static objectmodel.predefined.PredefinedConstant.NO_PRINT;
import static objectmodel.predefined.PredefinedConstant.GLOBAL_DICT;

import interpreter.ExecuteResultPrinter;
import objectmodel.dictionary.Dictionary;
import objectmodel.baseclasses.PredefinedFuncInstance;

import java.util.ArrayList;

/**
 * <h1>PredefinedFunction</h1>
 *
 * <p>the predefined function of language.</p>
 */
public class PredefinedFunction {
  public static PredefinedFuncInstance PRINT = new PredefinedFuncInstance("print", GLOBAL_DICT, GLOBAL_DICT) {
    @Override
    public Object callMethod(ArrayList<Object> parameters) {
      ExecuteResultPrinter objectPrinter = new ExecuteResultPrinter();
      int size = parameters.size();

      for (int i = 0; i < size; ++ i) {
        if (i != size - 1) {
          objectPrinter.printObject(parameters.get(i));
          System.out.print(" ");
        }
        else
          objectPrinter.printObject(parameters.get(i));
      }
      System.out.println();
      return NO_PRINT;
    }
  };

  public static void initializeEnvironment(Dictionary environment) {
    environment.put("print", PRINT);
  }
}