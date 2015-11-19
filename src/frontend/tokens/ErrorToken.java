package frontend.tokens;

import static frontend.TokenType.ERROR;

import frontend.Token;
import frontend.Source;
import frontend.ErrorCode;

/**
 * <h1>ErrorToken</h1>
 * <p>
 * <p>error token of Schinus language.</p>
 */
public class ErrorToken extends Token {

  /**
   * @param source    the source from where to fetch subsequent characters.
   * @param errorCode the error code.
   * @param tokenText the text of the erroneous token.
   * @throws Exception if an error occurred.
   */
  public ErrorToken(Source source, ErrorCode errorCode, String tokenText) throws Exception {
    super(source);

    this.text = tokenText;
    this.type = ERROR;
    this.value = errorCode;
  }

  /**
   * Do nothing. Do not consume any source characters.
   *
   * @throws Exception if an error occurred.
   */
  @Override
  protected void extract() throws Exception {

  }
}
