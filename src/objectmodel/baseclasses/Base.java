package objectmodel.baseclasses;

import objectmodel.dictionary.Dictionary;

/**
 * <h1>Base</h1>
 * <p>
 * <p>The base class that all of the object model classes inherit from.</p>
 */
public class Base {
  /* Every object has a class in object model.
   * If the object is a instance in object model, the className should be the class of instance.
   * If the object is a class in object model, the className should be the metaclass of class.
   */
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

  /**
   * @return the class name
   */
  public Class getClassName() {
    return className;
  }

  /**
   * @param className the classname to be set.
   */
  public void setClassName(Class className) {
    this.className = className;
  }

  /**
   * @return the fields
   */
  public Dictionary getFields() {
    return fields;
  }

  /**
   * @return the environment which contains the object.
   */
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
    /**
     * if the object is a instance of user-define class, then find the class of the instance
     * and the superclass of the class which it belongs to.
     *
     * if the object is a class object, then find the class object's class - TYPE, TYPE have no
     * supper class.
     */
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
    //return ((MethodInstance)method).callMethod(); // TODO
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
    if (fields.contains(fieldName)) {
      fields.replace(fieldName, value);
    } else {
      fields.put(fieldName, value);
    }
  }
}