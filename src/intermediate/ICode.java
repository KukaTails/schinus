package intermediate;

/**
 * <h1>ICodeImpl</h1>
 * <p>
 * <p>An implementation of the intermediate code as a parse tree.</p>
 */
public class ICode {
  private ICodeNode root;  // root node

  /**
   * Set and return the root node.
   *
   * @param node the node to set as root.
   * @return the root node.
   */
  public ICodeNode setRoot(ICodeNode node) {
    root = node;
    return root;
  }

  /**
   * Get the root node.
   *
   * @return the root node.
   */
  public ICodeNode getRoot() {
    return root;
  }
}
