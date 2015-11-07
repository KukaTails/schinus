package frontend.clang.tokens;

import static frontend.clang.ClangTokenType.ERROR;

import frontend.Source;
import frontend.clang.ClangToken;
import frontend.clang.ClangErrorCode;

/**
 * <h1>ClangErrorToken</h1>
 * <p>
 * <p>error token of C language.</p>
 */
public class ClangErrorToken extends ClangToken {

  /**
   * Constructor.
   *
   * @param source    the source from where to fetch subsequent characters.
   * @param errorCode the error code.
   * @param tokenText the text of the erroneous token.
   * @throws Exception if an error occurred.
   */
  public ClangErrorToken(Source source, ClangErrorCode errorCode, String tokenText) throws Exception {
    super(source);

    this.text = tokenText;
    this.type = ERROR;
    this.value = errorCode;
  }

  /**
   * Do nothing. Do not consume any source characters.
   *
   * @throws Exception if an error occurred.
   * @throws Exception
   */
  protected void extract() throws Exception {

  }
}
