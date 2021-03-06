package message;

import java.util.ArrayList;

/**
 * <h1>MessageHandler</h1>
 * <p>
 * <p>A helper class to which message producer classes delegate the task of
 * maintaining and notifying listeners.</p>
 */
public class MessageHandler {
  private Message message;                        // message
  private ArrayList<MessageListener> listeners;   // listener list

  public MessageHandler() {
    this.listeners = new ArrayList<MessageListener>();
  }

  /**
   * @param listener the listener to add.
   */
  public void addListener(MessageListener listener) {
    listeners.add(listener);
  }

  /**
   * @param listener the listener to remove.
   */
  public void removeListener(MessageListener listener) {
    listeners.remove(listener);
  }

  /**
   * Notify listeners after setting the message.
   *
   * @param message the message to set.
   */
  public void sendMessage(Message message) {
    this.message = message;
    notifyListeners();
  }

  /**
   * Notify each listener in the listener list by calling the listener's
   * messageReceived() method.
   */
  private void notifyListeners() {
    for (MessageListener listener : listeners) {
      listener.messageReceived(message);
    }
  }
}