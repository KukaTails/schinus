package intermediate;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <h1>ICodeNodeImpl</h1>
 * <p>
 * <p>An implementation of a node of the intermediate code.</p>
 */
public class ICodeNode extends HashMap<ICodeKey, Object> {

  private ICodeNodeType type;             // node type
  private ICodeNode parent;               // parent node
  private ArrayList<ICodeNode> children;  // children array list

  /**
   * Constructor.
   *
   * @param type the node type whose name will be the name of this node.
   */
  public ICodeNode(ICodeNodeType type) {
    this.type = type;
    this.parent = null;
    this.children = new ArrayList<ICodeNode>();
  }

  /**
   * Getter.
   *
   * @return the node type.
   */
  public ICodeNodeType getType() {
    return type;
  }

  /**
   * Return the parent of this node.
   *
   * @return the parent node.
   */
  public ICodeNode getParent() {
    return parent;
  }

  /**
   * Add a child node, and set the child node's parent
   *
   * @param node the child node. Not added if null.
   * @return the child node.
   */
  public ICodeNode addChild(ICodeNode node) {
    if (node != null) {
      children.add(node);
      ((ICodeNode) node).parent = this;
    }
    return node;
  }

  /**
   * Return an array list of this node's children.
   *
   * @return the array list of children.
   */
  public ArrayList<ICodeNode> getChildren() {
    return children;
  }

  /**
   * Set a node attribute.
   *
   * @param key   the attribute key.
   * @param value the attribute value.
   */
  public void setAttribute(ICodeKey key, Object value) {
    put(key, value);
  }

  /**
   * Get the value of a node attribute.
   *
   * @param key the attribute key.
   * @return the attribute value.
   */
  public Object getAttribute(ICodeKey key) {
    return get(key);
  }

  /**
   * Make a copy of this node.
   *
   * @return the copy.
   */
  public ICodeNode copy() {
    // Create a copy with the same type.
    ICodeNode copy = (ICodeNode) ICodeFactory.createICodeNode(type);

    Set<Entry<ICodeKey, Object>> attributes = entrySet();
    Iterator<Entry<ICodeKey, Object>> iter = attributes.iterator();

    // Copy attributes
    while (iter.hasNext()) {
      Map.Entry<ICodeKey, Object> attribute = iter.next();
      copy.put(attribute.getKey(), attribute.getValue());
    }

    return copy;
  }

  /**
   * Set ICodeImpl's String is the type
   *
   * @return
   */
  public String toString() {
    return type.toString();
  }
}