package interpreter.exception;

/**
 * Schinus Exception: the super class of all exceptions in Schinus language
 */
public abstract class SchniusException extends Exception {
  public SchniusException(String message) {
    super(message);
  }

  public SchniusException() {
    super();
  }
}
