package frontend.tokens;

import static frontend.Source.EOF;
import static frontend.TokenType.ERROR;
import static frontend.TokenType.STRING_LITERAL;
import static frontend.ErrorCode.UNEXPECTED_EOF;

import frontend.Token;
import frontend.Source;

/**
 * <h1>StringToken</h1>
 * <p>
 * <p>Schinus language string literal token</p>
 */
public class StringToken extends Token {
  /**
   * @param source the source from where to fetch the token's characters.
   * @throws Exception if an error occurred.
   */
  public StringToken(Source source) throws Exception {
    super(source);
  }

  /**
   * Extract a Schinus language string token from the source.
   *
   * @throws Exception if an error occurred.
   */
  @Override
  protected void extract() throws Exception {
    StringBuilder textBuffer = new StringBuilder();
    StringBuilder valueBuffer = new StringBuilder();

    char currentChar = nextChar();  // consume initial quote
    textBuffer.append('\'');

    // Get string characters.
    do {
      // Replace any whitespace character with a blank.
      if (Character.isWhitespace(currentChar)) {
        currentChar = ' ';
      }

      if ((currentChar != '\'') && (currentChar != EOF)) {
        textBuffer.append(currentChar);
        valueBuffer.append(currentChar);
        currentChar = nextChar();  // consume character
      }
    } while ((currentChar != '\'') && (currentChar != EOF));

    if (currentChar == '\'') {
      nextChar();  // consume final quote
      textBuffer.append('\'');
      type = STRING_LITERAL;
      value = valueBuffer.toString();
    } else {
      type = ERROR;
      value = UNEXPECTED_EOF;
    }

    text = textBuffer.toString();
  }
}