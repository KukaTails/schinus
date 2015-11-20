package objectmodel.predefined;

import static objectmodel.predefined.PredefinedType.BOOLEAN;
import static objectmodel.predefined.PredefinedType.OBJECT;

import objectmodel.baseclasses.Instance;
import objectmodel.dictionary.Dictionary;

public class PredefinedConstant {
  public static Dictionary GLOBAL_DICT = new Dictionary();

  public static Instance NONE = new Instance(OBJECT, new Dictionary(), GLOBAL_DICT);
  public static Instance TRUE = new Instance(BOOLEAN, new Dictionary(), GLOBAL_DICT);
  public static Instance FALSE = new Instance(BOOLEAN, new Dictionary(), GLOBAL_DICT);

  static {
    NONE.writeAttr("__value__", "None");
    TRUE.writeAttr("__value__", true);
    FALSE.writeAttr("__value__", false);
  }

  static {
    PredefinedType.initializeEnvironment(GLOBAL_DICT);
    PredefinedConstant.initializeEnvironment(GLOBAL_DICT);
  }

  public static void initializeEnvironment(Dictionary environment) {
    environment.put("None", NONE);
    environment.put("True", NONE);
    environment.put("False", NONE);
  }
}
