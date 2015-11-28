package interpreter.exception;

/**
 * <h1>UndefinedException</h1>
 * <p>
 * <p>Exception will be raised when use a undefine name.</p>
 */
public class UndefinedException extends SchinusException {
  public UndefinedException(String name) {
    super("name " + name + " not defined.");
  }
}
