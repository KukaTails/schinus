package objectmodel.baseclasses;

import objectmodel.dictionary.Dictionary;

import java.util.ArrayList;

/**
 * <h1>Class</h1>
 * <p>
 * <p>A user-defined class.</p>
 */
public class Class extends Base {
  private String name;       // the name of the class
  private Class baseClass;   // the base class of the class

  /**
   * Crate a class defined by user which is a object, record the class's name,
   * the base class of the class is a instance of Class, the metaclass of the Class is TYPE.
   * and other instances of Class to be metaclass.
   *
   * @param name      the name of the class defined by user.
   * @param baseClass the base class of the class defined by user
   * @param fields    the dictionary of the class which contains the attribute of the class
   * @param metaClass the meta class of the class
   */
  public Class(String name, Class baseClass, Dictionary fields, Class metaClass,
               Dictionary parentDict, Dictionary existedEnv) {
    super(metaClass, fields, parentDict, existedEnv);
    this.name = name;
    this.baseClass = baseClass;
    writeAttr("__name__", name);
  }

  /**
   * Compute the method resolution order of the class
   *
   * @return the array list of the superclass of the class
   */
  public ArrayList<Class> methodResolutionOrder() {
    ArrayList<Class> superClasses = new ArrayList<>();

    superClasses.add(this);
    if (baseClass != null) {
      superClasses.addAll(baseClass.methodResolutionOrder());
    }
    return superClasses;
  }

  /**
   * Is the class is a subclass of className
   *
   * @param className the name of class
   * @return true or false
   */
  public boolean isSubclass(Class className) {
    ArrayList<Class> superClass = methodResolutionOrder();

    return superClass.contains(className);
  }

  /**
   * @param field the field to be find in the class and its superclasses.
   * @return the result of find
   */
  public Object readFromClass(String field) {
    ArrayList<Class> superClass = methodResolutionOrder();

    for (Class cls : superClass) {
      if (cls.getFields().containsKey(field)) {
        return cls.getFields().get(field);
      }
    }
    return null;
  }

  public String getName() {
    return name;
  }
}