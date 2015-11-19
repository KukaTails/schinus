import static frontend.TokenType.STRING_LITERAL;

import frontend.TokenType;
import message.Message;
import message.MessageType;
import message.MessageListener;

/**
 * <h1>ParserMessageListener</h1>
 * <p>
 * <p>Listener for parser message</p>
 */
public class ParserMessageListener implements MessageListener {
  // the format to print token
  private static final String TOKEN_FORMAT = ">>> %-15s line=%03d, pos=%2d, text=\"%s\"";
  // the format to print token's value
  private static final String VALUE_FORMAT = ">>>                            value=%s";
  // the format to print the summary of parser
  private static final String PARSER_SUMMARY_FORMAT = "\n%,20d source lines." +
      "\n%,20d syntax errors." +
      "\n%,20.2f seconds total parsing time.\n";

  private static final int PREFIX_WIDTH = 5;

  /**
   * Called by the parser whenever it produces a message.
   *
   * @param message the message.
   */
  @Override
  public void messageReceived(Message message) {
    MessageType type = message.getType();

    switch(type) {
      // the message about token's type, line number, text and value
      case TOKEN: {
        Object body[] = (Object[]) message.getBody();
        int line = (Integer) body[0];
        int position = (Integer) body[1];
        TokenType tokenType = (TokenType) body[2];
        String tokenText = (String) body[3];
        Object tokenValue = body[4];

        // Print token's type, line, position and token's text
        System.out.println(String.format(TOKEN_FORMAT, tokenType, line, position, tokenText));

        // if token's value is not null and token's type is STRING_LITERAL, print token's value
        if (tokenValue != null && tokenType == STRING_LITERAL) {
          System.out.println(String.format(VALUE_FORMAT, tokenValue));
        }
        break;
      }

      case SYNTAX_ERROR: {
        Object body[] = (Object[]) message.getBody();
        int lineNumber = (Integer) body[0];
        int position = (Integer) body[1];
        String tokenText = (String) body[2];
        String errorMessage = (String) body[3];

        int spaceCount = PREFIX_WIDTH + position;
        StringBuilder flagBuffer = new StringBuilder();

        // Spaces up to the error position.
        for (int i = 1; i < spaceCount; ++i) {
          flagBuffer.append(' ');
        }

        // A pointer to the error followed by the error message.
        flagBuffer.append("^\n*** ").append(errorMessage);

        // Text, if any, of the bad token.
        if (tokenText != null) {
          flagBuffer.append(" [at \"").append(tokenText).append("\"]");
        }

        System.out.println(flagBuffer.toString());
        break;
      }
    }
  }
}