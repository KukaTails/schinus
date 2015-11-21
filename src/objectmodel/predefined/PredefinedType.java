package objectmodel.predefined;

import static objectmodel.predefined.PredefinedConstant.GLOBAL_DICT;

import objectmodel.baseclasses.Class;
import objectmodel.dictionary.Dictionary;

public class PredefinedType {
  public static Class OBJECT = new Class("object", null, new Dictionary(), null, null, GLOBAL_DICT);
  public static Class TYPE = new Class("type", OBJECT, new Dictionary(), null, null, GLOBAL_DICT);
  static {
    // TYPE and OBJECT is an instance of TYPE
    OBJECT.setClassName(TYPE);
    TYPE.setClassName(TYPE);
  }
  public static Class INTEGER = new Class("Integer", OBJECT, new Dictionary(), TYPE, GLOBAL_DICT, GLOBAL_DICT);
  public static Class FLOAT = new Class("Float", OBJECT, new Dictionary(), TYPE, GLOBAL_DICT, GLOBAL_DICT);
  public static Class BOOLEAN = new Class("Boolean", OBJECT, new Dictionary(), TYPE, GLOBAL_DICT, GLOBAL_DICT);
  public static Class STRING = new Class("String", OBJECT, new Dictionary(), TYPE, GLOBAL_DICT, GLOBAL_DICT);
  public static Class FUNCTION = new Class("Function", OBJECT, new Dictionary(), TYPE, GLOBAL_DICT, GLOBAL_DICT);
  public static Class ERRORTYPE = new Class("ErrorType", OBJECT, new Dictionary(), TYPE, GLOBAL_DICT, GLOBAL_DICT);
  public static Class TMPTYPE = new Class("TmpType", OBJECT, new Dictionary(), TYPE, GLOBAL_DICT, GLOBAL_DICT);
  /**
   * Initialize the type in environment
   * @param dictionary the environment
   */
  public static void initializeEnvironment(Dictionary dictionary) {
    dictionary.put("Type", TYPE);
    dictionary.put("Object", OBJECT);
    dictionary.put("Integer", INTEGER);
    dictionary.put("Float", FLOAT);
    dictionary.put("Boolean", BOOLEAN);
    dictionary.put("String", STRING);
    dictionary.put("Function", FUNCTION);
  }
}