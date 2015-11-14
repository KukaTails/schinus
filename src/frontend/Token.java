package frontend;

/**
 * <h1>Token</h1>
 * <p>
 * <p>base class of all the token classes.</p>
 */
public class Token {
  protected TokenType type;  // language-specific token type
  protected String text;     // token text
  protected Object value;    // token value
  protected Source source;   // source
  protected int lineNum;     // line number of the token's source line
  protected int position;    // position of the first token character

  /**
   * @param source the source from where to fetch the token's characters.
   * @throws Exception if an error occurred.
   */
  public Token(Source source) throws Exception {
    this.source = source;
    this.lineNum = source.getLineNum();
    this.position = source.getPosition();

    extract();
  }

  /**
   * @return the token type
   */
  public TokenType getType() {
    return type;
  }

  /**
   * @return the token text.
   */
  public String getText() {
    return text;
  }

  /**
   * @return the token value.
   */
  public Object getValue() {
    return value;
  }

  /**
   * @return the source line number.
   */
  public int getLineNumber() {
    return lineNum;
  }

  /**
   * @return the position.
   */
  public int getPosition() {
    return position;
  }

  /**
   * Default method to extract only one-character tokens from the source.
   * Subclasses can override this method to construct language-specific
   * tokens.  After extracting the token, the current source line position
   * will be one beyond the last token character.
   *
   * @throws Exception if an error occurred.
   */
  protected void extract() throws Exception {
    text = Character.toString(currentChar());
    value = null;

    nextChar();  // consume current character
  }

  /**
   * @return the current character from the source.
   * @throws Exception if an error occurred.
   */
  protected char currentChar() throws Exception {
    return source.currentChar();
  }

  /**
   * @return the next character from the source after moving forward.
   * @throws Exception if an error occurred.
   */
  protected char nextChar() throws Exception {
    return source.nextChar();
  }

  /**
   * @return the next character from the source without moving forward.
   * @throws Exception if an error occurred.
   */
  protected char peekChar() throws Exception {
    return source.peekChar();
  }
}