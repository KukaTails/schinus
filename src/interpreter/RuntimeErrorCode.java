package interpreter;

/**
 * <h1>RuntimeErrorCode</h1>
 * <p>
 * <p>Runtime error codes.</p>
 */
public enum RuntimeErrorCode {
  UNINITIALIZED_VALUE("Uninitialized value"),                // use uninitialized value
  DIVISION_BY_ZERO("Division by zero"),                      // division by zero
  UNDEFINED_NAME("Undefined name"),                           // use undefined name
  UNIMPLEMENTED_FEATURE("Unimplemented runtime feature"),    // unimplemented runtime feature

  CALL_UNCALLABLE_OBJECT("Object is not callable"),          // call a object which is not callable
  ASSIGN_TO_LEFT_VALUE("Can not assign to left value"),      // assign to left value
  OBJECT_IS_NOT_CLASS("Object is not class");                // use a object which is not class to be base class

  private String message;      // error message

  /**
   * @param message the error message.
   */
  RuntimeErrorCode(String message) {
    this.message = message;
  }

  public String toString() {
    return message;
  }
}