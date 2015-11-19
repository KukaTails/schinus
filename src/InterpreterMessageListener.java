import message.Message;
import message.MessageType;
import message.MessageListener;

/**
 * Listener for back end message.
 */
public class InterpreterMessageListener implements MessageListener {

  private static final String LINE_FORMAT = ">>> AT LINE %03d\n";

  private static final String ASSIGN_FORMAT = ">>> LINE %03d: %s = %s\n";

  /**
   * Called by the back end whenever it produces a message.
   *
   * @param message the message.
   */
  @Override
  public void messageReceived(Message message) {
    MessageType type = message.getType();

    switch(type) {
      case RUNTIME_ERROR: {
        Object body[] = (Object[]) message.getBody();
        String errorMessage = (String) body[0];
        Integer lineNumber = (Integer) body[1];

        System.out.print("*** RUNTIME ERROR");
        if (lineNumber != null) {
          System.out.print(" AT LINE " + String.format("%03d", lineNumber));
        }
        System.out.println(": " + errorMessage);
        break;
      }
    }
  }
}
