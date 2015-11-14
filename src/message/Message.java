package message;

/**
 * <h1>Message</h1>
 * <p>
 * <p>Message format.</p>
 */
public class Message {
  private MessageType type;
  private Object body;

  /**
   * @param type message type
   * @param body message body which contains the message to be reported
   */
  public Message(MessageType type, Object body) {
    this.type = type;
    this.body = body;
  }

  /**
   * @return the type of message
   */
  public MessageType getType() {
    return type;
  }

  /**
   * @return the body of message
   */
  public Object getBody() {
    return body;
  }
}