package interpreter.exception;

public class SyntaxErrorException extends SchinusException {
  public SyntaxErrorException() {
    super();
  }

  public SyntaxErrorException(String message) {
    super(message);
  }
}
