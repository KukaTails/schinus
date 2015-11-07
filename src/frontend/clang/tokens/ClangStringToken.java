package frontend.clang.tokens;

import static frontend.Source.EOF;
import static frontend.clang.ClangTokenType.ERROR;
import static frontend.clang.ClangTokenType.STRING_LITERAL;
import static frontend.clang.ClangErrorCode.UNEXPECTED_EOF;

import frontend.Source;
import frontend.clang.ClangToken;

/**
 * <h1>ClangStringToken</h1>
 * <p>
 * <p>C language string literal token</p>
 * <p>TODO: deal with escape characters </p>
 */
public class ClangStringToken extends ClangToken {
  /**
   * Constructor.
   *
   * @param source the source from where to fetch the token's characters.
   * @throws Exception if an error occurred.
   */
  public ClangStringToken(Source source) throws Exception {
    super(source);
  }

  /**
   * Extract a C language string token from the source.
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