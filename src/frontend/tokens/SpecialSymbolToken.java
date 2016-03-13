package frontend.tokens;

import static frontend.TokenType.ERROR;
import static frontend.TokenType.END_OF_LINE;
import static frontend.TokenType.SPECIAL_SYMBOLS;
import static frontend.ErrorCode.INVALID_CHARACTER;

import frontend.Token;
import frontend.Source;

import java.io.IOException;

/**
 * <h1>SpecialSymbolToken</h1>
 * <p>
 * <p>special symbol tokens of Schinus.</p>
 */
public class SpecialSymbolToken extends Token {
  /**
   * @param source the source from where to fetch the token's characters.
   * @throws IOException if an error occurred.
   */
  public SpecialSymbolToken(Source source) throws IOException {
    super(source);
  }

  /**
   * Extract special symbol token from the source.
   *
   * @throws IOException
   */
  @Override
  protected void extract() throws IOException {
    char firstChar = currentChar();

    text = Character.toString(firstChar);
    type = null;

    if (source.atEol() || source.atEof()) {
      type = END_OF_LINE;
      source.addPosition(); // consume EOL
      return;
    }

    switch(firstChar) {
      // Single-character special symbols.
      case '(':
      case ')':
      case '{':
      case '}':
      case '[':
      case ']':
      case ':':
      case ',':
      case '?':
      case '~': {
        nextChar(); // consume character
        break;
      }

      //
      case '+':
      case '-':
      case '*':
      case '/':
      case '%':
      case '<':
      case '>':
      case '=':
      case '!':
      case '&':
      case '^':
      case '|': {
        char secondChar = nextChar();

        if (secondChar == '=') {
          text += secondChar;
          nextChar(); // consume '='
        }
        // symbol "**"
        else if ((firstChar == '*') && (secondChar == '*')) {
          text += secondChar;
          nextChar();
        }
        // symbol "<<" or "<<="
        else if ((firstChar == '<') && (secondChar == '<')) {
          text += secondChar;

          char thirdChar = nextChar(); // consume '<'
          if (thirdChar == '=') {
            text += thirdChar;
            nextChar(); // consume '='
          }
        }
        // symbol ">>" or ">>="
        else if ((firstChar == '>') && (secondChar == '>')) {
          text += secondChar;

          char thirdChar = nextChar();  // consume '>'
          if (thirdChar == '=') {
            text += thirdChar;
            nextChar();  // consume '='
          }
        }
        // symbol "//"
        else if ((firstChar == '/') && (secondChar == '/')) {
          text += secondChar;
          nextChar();
        }
        // symbol "&&"
        else if ((firstChar == '&') && (secondChar == '&')) {
          text += secondChar;
          nextChar();  // consume '&'
        }
        // symbol "||"
        else if ((firstChar == '|') && (secondChar == '|')) {
          text += secondChar;
          nextChar();  // consume '|'
        }
        break;
      }
      // symbol "." or "..."
      case '.': {
        char secondChar = nextChar(); // consume '.'

        if (secondChar == '.') {
          text += secondChar;
          char thirdChar = nextChar(); // consume '.';
          if (thirdChar == '.') {
            text += thirdChar;
            nextChar(); // consume '.'
          }
        }
        break;
      }

      default: {
        nextChar(); // consume bad character
        type = ERROR;
        value = INVALID_CHARACTER;
      }
    }

    // set the type if it wasn't an error
    if (type == null) {
      type = SPECIAL_SYMBOLS.get(text);
    }
  }
}