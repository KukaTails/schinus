package message;

/**
 * <h1>MessageType</h1>
 * <p>
 * <p>the message type of message</p>
 */
public enum MessageType {
  TOKEN,                // message about tokens that scanned by parser
  SOURCE_LINE,          // message about source line produced by source
  SYNTAX_ERROR,         // message about syntax error produce by ClangErrorHandler in parser
  PARSER_SUMMARY,       // message about parser summary
  RUNTIME_ERROR,        // message send by backend about runtime error
  INTERPRETER_SUMMARY,  // message send by interpreter to send message about summary message about interpreter.

}