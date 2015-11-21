package objectmodel.baseclasses;

import java.util.ArrayList;

import static objectmodel.predefined.PredefinedConstant.NO_PRINT;

public abstract class FuncBody implements Runnable {
  private ArrayList<Object> parameters;
  private Object result = NO_PRINT;

  public FuncBody(ArrayList<Object> parameters) {
    this.parameters = parameters;
  }

  public Object getResult() {
    return result;
  }
}
