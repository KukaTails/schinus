package interpreter.exception;

/**
 * Schinus Exception: the super class of all exceptions in Schinus language
 */
public abstract class SchinusException extends Exception {
  public SchinusException(String message) {
    super(message);
  }

  public SchinusException() {
    super();
  }
}
