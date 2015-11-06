package message;

/**
 * Created by Keen on 2015/11/6.
 */
public class Message {
  private MessageType type;
  private Object      body;

  public Message(MessageType type, Object body)
  {
    this.type = type;
    this.body = body;
  }

  public MessageType getType()
  {
    return type;
  }

  public Object getBody()
  {
    return body;
  }
}