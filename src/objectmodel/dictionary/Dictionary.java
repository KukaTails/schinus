package objectmodel.dictionary;

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