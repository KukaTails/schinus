package message;

/**
 * <h1>MessageProducer</h1>
 * <p>
 * <p>All classes that produce messages must implement this interface.</p>
 */
public interface MessageProducer {
  /**
   * @param listener the listener to add.
   */
  public void addMessageListener(MessageListener listener);

  /**
   * @param listener the listener to remove.
   */
  public void removeMessageListener(MessageListener listener);

  /**
   * @param message the message to set.
   */
  public void sendMessage(Message message);
}