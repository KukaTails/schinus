package util;

import static objectmodel.predefined.PredefinedConstant.NONE;

import objectmodel.baseclasses.Instance;

public class ObjectPrinter {
  public static void printValue(Object object) {
    Object value = object;

    if (object == null)
      return;

    if (object instanceof Instance) {
      value = ((Instance)object).readAttribute("__value__");
    }

    if (!object.equals(NONE)) {
      System.out.println(value);
    } else {
      System.out.println();
    }
  }
}
