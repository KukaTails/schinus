package frontend.tokens;

import static frontend.TokenType.IDENTIFIER;
import static frontend.TokenType.RESERVED_WORDS;

import frontend.Token;
import frontend.Source;

import java.io.IOException;

/**
 * <h1>WordToken</h1>
 * <p>
 * <p>word tokens of Schinus(identifiers and reserved words).</p>
 */
public class WordToken extends Token {
  /**
   * @param source the source from where to fetch the token's characters.
   * @throws IOException if an error occurred.
   */
  public WordToken(Source source) throws IOException {
    super(source);
  }

  /**
   * Extract a Schinus language word token from the source.
   *
   * @throws IOException if an error occurred.
   */
  @Override
  protected void extract() throws IOException {
    StringBuilder textBuffer = new StringBuilder();
    char currentChar = currentChar();

    // Get the word characters (letter or digit).
    // The scanner has already determined that the first character is a letter.
    while (Character.isLetterOrDigit(currentChar)) {
      textBuffer.append(currentChar);
      currentChar = nextChar();  // consume character
    }
    text = textBuffer.toString();

    // Is it a reserved word or an identifier?
    // the reserved word should be lowercase,
    // if the word is uppercase, it's not a reserved word
    type = (RESERVED_WORDS.containsKey(text)) ? RESERVED_WORDS.get(text)  // reserved word
        : IDENTIFIER;                               // identifier
  }
}