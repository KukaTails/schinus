package interpreter.exception;

/**
 * <h1>NotCallableException</h1>
 * <h1>Exception will be raised when a object which is not callable is called.</h1>
 */
public class NotCallableException extends SchinusException {
  public NotCallableException(String exceptionMessage) {
    super(exceptionMessage);
  }
}
