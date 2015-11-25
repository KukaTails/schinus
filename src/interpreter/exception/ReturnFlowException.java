package interpreter.exception;

/**
 * <h1>ReturnFlowException</h1>
 * <p>the control flow exception raised by return statement.</p>
 */
public class ReturnFlowException extends SchniusException {
  private Object returnValue;

  public ReturnFlowException(Object returnValue) {
    this.returnValue = returnValue;
  }

  public Object getReturnValue() {
    return returnValue;
  }
}
