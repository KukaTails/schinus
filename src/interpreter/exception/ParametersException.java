package interpreter.exception;

public class ParametersException extends SchinusException {
  public ParametersException(String methodName, int expectedArguNumber, int actualArguNumber) {
    super("function " + methodName + " takes " + expectedArguNumber
        + " argument" + (expectedArguNumber > 1 ? "s" : " ") + " but "
        + actualArguNumber + " were given.");
  }
}
