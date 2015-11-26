package interpreter.exception;

/**
 * <h1>UndefinedException</h1>
 * <p>
 * <p>Exception will be raised when use a undefine name.</p>
 */
public class UndefinedException extends SchniusException {
  public UndefinedException(String exceptionMessage) {
    super(exceptionMessage);
  }
}
