package interpreter;

import static intermediate.ICodeKey.LINE;
import static message.MessageType.RUNTIME_ERROR;

import message.Message;
import intermediate.ICodeNode;

/**
 * <h1>RuntimeErrorHandler</h1>
 * <p>
 * <p>Runtime error handler for the interpreter.</p>
 */
public class RuntimeErrorHandler {
  private static final int MAX_ERRORS = 5;    // the max errors allowed
  private static int errorCount = 0;          // count of runtime errors

  /**
   * @return the count of runtime errors.
   */
  public static int getErrorCount() {
    return errorCount;
  }

  /**
   * @param node      the root node of the offending statement or expression.
   * @param errorCode the runtime error code.
   * @param executor  the executor.
   */
  public void flag(ICodeNode node, RuntimeErrorCode errorCode, Executor executor) {
    String lineNumber = null;

    // Look for the ancestor statement node with a line number attribute.
    while ((node != null) && (node.getAttribute(LINE) == null)) {
      node = node.getParent();
    }

    // Notify the interpreter's listeners.
    executor.sendMessage(new Message(RUNTIME_ERROR, new Object[] {errorCode.toString(), (Integer) node.getAttribute
        (LINE)}));

    if (++errorCount > MAX_ERRORS) {
      System.out.println("*** ABORTED AFTER TOO MANY RUNTIME ERRORS.");
      System.exit(-1);
    }
  }
}