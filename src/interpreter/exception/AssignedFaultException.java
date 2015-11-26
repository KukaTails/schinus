package interpreter.exception;

/**
 * <h1>AssignedFaultException</h1>
 * <p>
 * <p>Exception will be raised when value is assigned to a object which can not be assigned.</p>
 */
public class AssignedFaultException extends SchniusException {
  public AssignedFaultException(String exceptionMessage) {
    super(exceptionMessage);
  }
}
