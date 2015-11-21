package objectmodel.dictionary;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * <h1>Dictionary</h1>
 *
 * <p>the dictionary used to store objects.</p>
 */
public class Dictionary extends Hashtable<String, Object> {
  Dictionary parentDictionary;     // the parent environment

  public Dictionary() {
    parentDictionary = null;
  }

  public Object readField(String field){
    Object result = get(field);

    if (result != null) {
      return result;
    }
    result = readFromSuperDict(field);
    return result;
  }


  /**
   * Get the object resolution dictionaries.
   * @return
   */
  public ArrayList<Dictionary> fieldResolutionOrder() {
    ArrayList<Dictionary> superDic = new ArrayList<>();

    superDic.add(this);
    if (parentDictionary != null) {
      superDic.addAll(parentDictionary.fieldResolutionOrder());
    }
    return superDic;
  }

  /**
   * Read a field from superDict
   * @param field the field to read.
   * @return field object.
   */
  public Object readFromSuperDict(String field) {
    ArrayList<Dictionary> superDict = fieldResolutionOrder();

    for (Dictionary dict : superDict) {
      if (dict.containsKey(field))
        return dict.get(field);
    }
    return null;
  }


  /**
   * @param parent the parent dictionary which contains the dictionary.
   */
  public Dictionary(Dictionary parent) {
    this.parentDictionary = parent;
  }

  /**
   * @return parent dictionary
   */
  public Dictionary getDictionaryParent() {
    return parentDictionary;
  }

  /**
   * @param parent set the parent dictionary of this dictionary.
   */
  public void setParentDictionary(Dictionary parent) {
    this.parentDictionary = parent;
  }
}