package frontend.clang.tokens;

import static frontend.clang.ClangTokenType.IDENTIFIER;
import static frontend.clang.ClangTokenType.RESERVED_WORDS;

import frontend.Source;
import frontend.clang.ClangToken;
import frontend.clang.ClangTokenType;

/**
 * <h1>PascalWordToken</h1>
 * <p>
 * <p> Pascal word tokens (identifiers and reserved words).</p>
 */
public class ClangWordToken extends ClangToken {
  /**
   * Constructor.
   *
   * @param source the source from where to fetch the token's characters.
   * @throws Exception if an error occurred.
   */
  public ClangWordToken(Source source) throws Exception {
    super(source);
  }

  /**
   * Extract a C language word token from the source.
   *
   * @throws Exception if an error occurred.
   */
  @Override
  protected void extract() throws Exception {
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