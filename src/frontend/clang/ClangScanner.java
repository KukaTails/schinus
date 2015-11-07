package frontend.clang;

import static frontend.Source.EOF;

import frontend.Token;
import frontend.Source;
import frontend.Scanner;

public class ClangScanner extends Scanner {
  /**
   * Constructor
   *
   * @param source the source to be used with this scanner.
   */
  public ClangScanner(Source source) {
    super(source);
  }

  /**
   * Extract and return the next Pascal token from the source.
   *
   * @return the next token.
   * @throws Exception if an error occurred.
   */
  protected Token extractToken() throws Exception {
    skipWhiteSpace();

    Token token;
    char currentChar = currentChar();

    // Construct the next token.  The current character determines the
    // token type.

    /*if (currentChar == EOF) {
      token = new EofToken(source);
    }
    else if (Character.isLetter(currentChar)) {
      token = new PascalWordToken(source);
    }
    else if (Character.isDigit(currentChar)) {
      token = new PascalNumberToken(source);
    }
    else if (currentChar == '\'') {
      token = new PascalStringToken(source);
    }
    else if (PascalTokenType.SPECIAL_SYMBOLS
        .containsKey(Character.toString(currentChar))) {
      token = new PascalSpecialSymbolToken(source);
    }
    else {
      token = new PascalErrorToken(source, INVALID_CHARACTER,
          Character.toString(currentChar));
      nextChar();  // consume character
    }
    */
    //return token;
    return null; // TODO
  }

  /**
   * Skip whitespace characters by consuming them.  A comment is whitespace.
   *
   * @throws Exception if an error occurred.
   */
  private void skipWhiteSpace() // TODO
      throws Exception {
    char currentChar = currentChar();

    while (Character.isWhitespace(currentChar) || (currentChar == '{')) {

      // Start of a comment?
      if (currentChar == '{') {
        do {
          currentChar = nextChar();  // consume comment characters
        } while ((currentChar != '}') && (currentChar != EOF));

        // Found closing '}'?
        if (currentChar == '}') {
          currentChar = nextChar();  // consume the '}'
        }
      }
      // Not a comment.
      else {
        currentChar = nextChar();  // consume whitespace character
      }
    }
  }
}