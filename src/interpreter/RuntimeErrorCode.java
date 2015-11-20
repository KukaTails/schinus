package interpreter;

/**
 * <h1>RuntimeErrorCode</h1>
 * <p>
 * <p>Runtime error codes.</p>
 */
public enum RuntimeErrorCode {
  UNINITIALIZED_VALUE("Uninitialized value"),                // use uninitialized value
  DIVISION_BY_ZERO("Division by zero"),                      // division by zero
  UNDEFINE_VARIABLE("Undefined variable"),                   // use undefined variable
  UNIMPLEMENTED_FEATURE("Unimplemented runtime feature"),    // unimplemented runtime feature

  CALL_UNCALLABLE_OBJECT("Object is not callable"),          // call a object which is not callable
  ASSIGN_TO_LEFT_VALUE("Can not assign to left value");      // assign to left value

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