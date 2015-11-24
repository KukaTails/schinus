package objectmodel.baseclasses;

import objectmodel.dictionary.Dictionary;

/**
 * <h1>Base</h1>
 * <p>
 * <p>The base class that all of the object model classes inherit from.</p>
 */
public class Base {
  private Class className;        // all objects is an instance of class
  private Dictionary fields;      // both class and instance has a dict
  private Dictionary existedEnv;  // environment which contains the object

  /**
   * initialize the object's fields and class
   *
   * @param className the class of the instance or the metaclass of class
   * @param fields    the field of class or instance
   */
  public Base(Class className, Dictionary fields, Dictionary parentDict, Dictionary existedEnv) {
    this.className = className;
    this.fields = fields;
    this.fields.setParentDictionary(parentDict);
    this.existedEnv = existedEnv;
  }

  public Class getClassName() {
    return className;
  }

  public void setClassName(Class className) {
    this.className = className;
  }

  public Dictionary getFields() {
    return fields;
  }

  public Dictionary getExistedEnv() {
    return existedEnv;
  }

  public void setExistedEnv(Dictionary existedEnv) {
    this.existedEnv = existedEnv;
  }

  /**
   * Read field(attribute) of instance of a class or a class
   *
   * @param fieldName the name of field(attribute)
   * @return the field(attribute)
   */
  public Object readAttribute(String fieldName) {
    Object result = readDict(fieldName);
    if (result != null) {
      return result;
    }
    result = className.readFromClass(fieldName);
    //if (isBindable(result)) {
    //  return makeBoundMethod(result, this);
    //}
    //if (result != null)
    //  return result;
    return result;
    // TODO attributeError(fieldname)
  }

  /**
   * Write field(attribute) of instance of a class or a class
   *
   * @param fieldName the name of a field(attribute)
   * @param value     the value to be written to field(attribute)
   */
  public void writeAttr(String fieldName, Object value) {
    writeDict(fieldName, value);
  }

  /**
   * Judge a object is a instance of a class(the class is a object of Class).
   * The class could be TYPE(all the class in object model is a instance of type),
   * and the class could be a instance of user-defined class.
   *
   * @return True or False
   */
  public boolean isInstanceOf(Class className) {
    return this.className.isSubclass(className);
  }

  /**
   * Call method 'methodName' with arguments 'args' on the object
   *
   * @param methodName the method which should be called
   * @param arguments  the arguments of the method
   */
  public Object callMethod(String methodName, Object[] arguments) {
    Object method  = readAttribute(methodName);
    return null;
  }

  /**
   * Read an field 'fieldName' out of the object's dict
   *
   * @param fieldName the field name to be read
   * @return the value of the field
   */
  private Object readDict(String fieldName) {
    return fields.get(fieldName);
  }

  /**
   * Write an attribute into the object's dict.
   *
   * @param fieldName the field name to be write
   * @param value     the value of the field name to be write into the dict
   */
  private void writeDict(String fieldName, Object value) {
    if (fields.containsKey(fieldName)) {
      fields.replace(fieldName, value);
    } else {
      fields.put(fieldName, value);
    }
  }
}