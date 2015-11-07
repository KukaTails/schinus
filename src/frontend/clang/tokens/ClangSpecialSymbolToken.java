package frontend.clang.tokens;

import static frontend.clang.ClangTokenType.ERROR;
import static frontend.clang.ClangErrorCode.INVALID_CHARACTER;
import static frontend.clang.ClangTokenType.SPECIAL_SYMBOLS;

import frontend.Source;
import frontend.clang.ClangToken;

/**
 * <h1>PascalSpecialSymbolToken</h1>
 * <p>
 * <p>Pascal special symbol tokens.</p>
 */
public class ClangSpecialSymbolToken extends ClangToken {
  /**
   * Constructor.
   *
   * @param source the source from where to fetch the token's characters.
   * @throws Exception if an error occurred.
   */
  public ClangSpecialSymbolToken(Source source) throws Exception {
    super(source);
  }

  /**
   * Extract a C language special symbol token from the source.
   *
   * @throws Exception
   */
  @Override
  protected void extract() throws Exception {
    char firstChar = currentChar();

    text = Character.toString(firstChar);
    type = null;

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
        // symbol "++"
        else if ((firstChar == '+') && (secondChar == '+')) {
          text += secondChar;
          nextChar(); // consume '+'
        }
        // symbol "--"
        else if ((firstChar == '-') && (secondChar == '-' || secondChar == '>')) {
          text += secondChar;
          nextChar(); // consume '-' or '>'
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