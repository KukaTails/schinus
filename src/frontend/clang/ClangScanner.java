package frontend.clang;

import static frontend.Source.EOF;
import static frontend.clang.ClangErrorCode.INVALID_CHARACTER;

import frontend.Token;
import frontend.Source;
import frontend.Scanner;
import frontend.EofToken;
import frontend.clang.tokens.ClangErrorToken;
import frontend.clang.tokens.ClangWordToken;
import frontend.clang.tokens.ClangStringToken;
import frontend.clang.tokens.ClangNumberToken;
import frontend.clang.tokens.ClangSpecialSymbolToken;

/**
 * <h1>ClangScanner</h1>
 * <p>
 * <p>C language scanner to scan character stream.</p>
 */
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
   * Extract and return the next C language token from the source.
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

    // EOF token
    if (currentChar == EOF) {
      token = new EofToken(source);
    }
    // Word(Identifier and reserved word) token
    else if (Character.isLetter(currentChar)) {
      token = new ClangWordToken(source);
    }
    // Number constant
    else if (Character.isDigit(currentChar)) {
      token = new ClangNumberToken(source);
    }
    // String constant
    else if (currentChar == '\'') {
      token = new ClangStringToken(source);
    }
    // Special symbols(operators and delimiter)
    else if (ClangTokenType.SPECIAL_SYMBOLS.containsKey(Character.toString(currentChar))) {
      token = new ClangSpecialSymbolToken(source);
    }
    // Wrong token
    else {
      token = new ClangErrorToken(source, INVALID_CHARACTER, Character.toString(currentChar));
      nextChar();  // consume character
    }

    //return token;
    return token; // TODO
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