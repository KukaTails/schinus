package frontend;

import java.util.Hashtable;

/**
 * <h1>TokenType</h1>
 * <p>
 * <p>The token type of the lanuage.</p>
 */
public enum TokenType {
  /* Reserved words. */

  // control flow keyword
  IF, ELIF, ELSE, DO, WHILE, CONTINUE, BREAK, RETURN, END,

  // logical operators
  LOGICAL_AND("and"), LOGICAL_OR("or"), LOGICAL_NOT("not"),

  // boolean constant
  FALSE_TOKEN("False"), TRUE_TOKEN("True"), NONE_TOKEN("None"),

  DEF,   // function define
  CLASS, // class define

  // basic operators
  ADD("+"), SUB("-"), MUL("*"), POWER("**"), FLOAT_DIV("/"), INTEGER_DIV("//"), MOD("%"),
  ADD_ASSIGN("+="), SUB_ASSIGN("-="), MUL_ASSIGN("*="), DIV_ASSIGN("/="), MOD_ASSIGN("%="),

  // relational operators
  GREAT_THAN(">"), LESS_THAN("<"),
  GREAT_EQUAL(">="), LESS_EQUAL("<="),
  EQUAL("=="), NOT("!"), NOT_EQUAL("!="),

  // other operators
  ASSIGN("="), DOT("."),
  ELLIPSIS("..."),

  // other symbols
  COMMA(","), SEMICOLON(";"), END_OF_LINE("\n"),
  LEFT_PAREN("("), RIGHT_PAREN(")"), LEFT_BRACKET("["), RIGHT_BRACKET("]"),

  // other token types
  IDENTIFIER, FLOAT_CONSTANT, INTEGER_CONSTANT, CHAR_CONSTANT, STRING_LITERAL,
  ERROR, END_OF_FILE;

  private String text; // token text

  /**
   * Constructor.
   */
  private TokenType() {
    this.text = this.toString().toLowerCase();
  }

  /**
   * Constructor.
   *
   * @param text the token text
   */
  private TokenType(String text) {
    this.text = text;
  }

  /**
   * Getter.
   *
   * @return the token text.
   */
  public String getText() {
    return text;
  }

  private static final int FIRST_RESERVED_INDEX = IF.ordinal();
  private static final int LAST_RESERVED_INDEX = CLASS.ordinal();

  // Set of lower-cased C reserved word text strings.
  public static Hashtable<String, TokenType> RESERVED_WORDS = new Hashtable<>();

  static {
    TokenType[] values = TokenType.values();
    for (int i = FIRST_RESERVED_INDEX; i <= LAST_RESERVED_INDEX; ++i) {
      RESERVED_WORDS.put(values[i].getText(), values[i]);
    }
  }

  private static final int FIRST_SPECIAL_INDEX = ADD.ordinal();
  private static final int LAST_SPECIAL_INDEX = RIGHT_BRACKET.ordinal();

  // Hash table of C language special symbols.
  // Each special symbol's text is the key to its C language token type.
  public static Hashtable<String, TokenType> SPECIAL_SYMBOLS = new Hashtable<>();

  static {
    TokenType[] values = TokenType.values();
    for (int i = FIRST_SPECIAL_INDEX; i <= LAST_SPECIAL_INDEX; ++i) {
      SPECIAL_SYMBOLS.put(values[i].getText(), values[i]);
    }
  }
}