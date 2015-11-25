package frontend;

import static frontend.Source.EOF;
import static frontend.ErrorCode.INVALID_CHARACTER;

import frontend.tokens.EofToken;
import frontend.tokens.WordToken;
import frontend.tokens.ErrorToken;
import frontend.tokens.NumberToken;
import frontend.tokens.StringToken;
import frontend.tokens.SpecialSymbolToken;

import java.io.IOException;

/**
 * <h1>Scanner</h1>
 * <p>
 * <p>The scanner which used to scan tokens.</p>
 */
public class Scanner {
  protected Source source;      // source
  private Token currentToken;   // current token

  /**
   * @param source the source to be used with this scanner.
   */
  public Scanner(Source source) {
    this.source = source;
  }

  /**
   * @return the current token.
   */
  public Token currentToken() {
    return currentToken;
  }

  /**
   * @return the next token.
   * @throws IOException if an error occurred.
   */
  public Token nextToken() throws IOException {
    currentToken = extractToken();
    return currentToken;
  }

  /**
   * Do the actual work of extracting and returning the next token from the source.
   *
   * @return the next token.
   * @throws IOException if an error occurred.
   */
  protected Token extractToken() throws IOException {
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
      token = new WordToken(source);
    }
    // Number constant
    else if (Character.isDigit(currentChar)) {
      token = new NumberToken(source);
    }
    // String constant
    else if (currentChar == '\'') {
      token = new StringToken(source);
    }
    // Special symbols(operators and delimiter)
    else if (TokenType.SPECIAL_SYMBOLS.containsKey(Character.toString(currentChar))) {
      token = new SpecialSymbolToken(source);
    }
    // Wrong token
    else {
      token = new ErrorToken(source, INVALID_CHARACTER, Character.toString(currentChar));
      nextChar();  // consume character
    }

    //return token;
    return token; // TODO
  }

  /**
   * @return the current character from the source.
   * @throws IOException if an error occurred.
   */
  public char currentChar() throws IOException {
    return source.currentChar();
  }

  /**
   * @return the next character from the source.
   * @throws IOException if an error occurred.
   */
  public char nextChar() throws IOException {
    return source.nextChar();
  }

  /**
   * @return true if at the end of the source line, else return false.
   * @throws IOException if an error occurred.
   */
  public boolean atEol() throws IOException {
    return source.atEol();
  }

  /**
   * @return true if at the end of the source file, else return false.
   * @throws IOException if an error occurred.
   */
  public boolean atEof() throws IOException {
    return source.atEof();
  }

  /**
   * @throws IOException if an error occurred.
   */
  public void skipToNextLine() throws IOException {
    source.skipToNextLine();
  }


  /**
   * Skip whitespace characters by consuming them.
   * A comment is whitespace.
   *
   * @throws IOException if an error occurred.
   */
  private void skipWhiteSpace() throws IOException {
    char currentChar = currentChar();

    while ((currentChar != '\n') && (Character.isWhitespace(currentChar)
        || (currentChar == '{'))) {

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