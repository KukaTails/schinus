package objectmodel.baseclasses;

import static objectmodel.predefined.PredefinedType.FUNCTION;

import objectmodel.dictionary.Dictionary;

import java.util.ArrayList;

public abstract class PredefinedFuncInstance extends Instance {
  private String methodName;

  public PredefinedFuncInstance(String methodName, Dictionary parentDict, Dictionary existedEnv) {
    super(FUNCTION, new Dictionary(), parentDict, existedEnv);
    this.methodName = methodName;
    writeAttr("__name__", this.methodName);
  }

  public abstract Object callMethod(ArrayList<Object> parameters);
}
