package intermediate;

/**
 * <h1>ICodeFactory</h1>
 * <p>
 * <p>A factory for creating objects that implement the intermediate code.</p>
 */
public class ICodeFactory {
  /**
   * Create and return a node implementation.
   *
   * @param type the node type.
   * @return the node implementation.
   */
  public static ICodeNode createICodeNode(ICodeNodeType type) {
    return new ICodeNode(type);
  }
}
