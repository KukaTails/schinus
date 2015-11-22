package interpreter.exception;

public class ReturnFlowException extends Exception {
  private Object returnValue;

  public ReturnFlowException() {
  }

  public ReturnFlowException(Object returnValue) {
    this.returnValue = returnValue;
  }

  public Object getReturnValue() {
    return returnValue;
  }
}
