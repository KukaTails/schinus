package message;

/**
 * <h1>MessageType</h1>
 *
 * <p>the message type of message</p>
 */
public enum MessageType {
  TOKEN,          // message about tokens that scanned by parser
  SOURCE_LINE,    // message about source line produced by source
  SYNTAX_ERROR,   // message about syntax error produce by ClangErrorHandler in parser
  PARSER_SUMMARY, // message about parser summary
}