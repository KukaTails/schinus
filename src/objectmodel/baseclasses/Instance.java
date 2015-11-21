package objectmodel.baseclasses;

import objectmodel.dictionary.Dictionary;

/**
 * <h1>Instance</h1>
 *
 * <p>Instance of a user-defined class</p>
 */
public class Instance extends Base {
  /**
   * Create a instance of a class, and the class should be a instance of Class.
   * initialize the instance's field by using the dict of the className's field,
   * and record the instance's class is className
   *
   * @param className a instance of class
   */
  public Instance(objectmodel.baseclasses.Class className, Dictionary fields,
                  Dictionary parentDictionary, Dictionary existedEnv) {
    super(className, fields, parentDictionary, existedEnv);
  }
}