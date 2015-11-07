package frontend.clang;

import frontend.Token;
import frontend.Source;

/**
 * <h1>ClangToken</h1>
 * <p>
 * <p>Base class for C language token classes.</p>
 */
public class ClangToken extends Token {
  /**
   * Constructor.
   *
   * @param source the source from where to fetch the token's characters.
   * @throws Exception if an error occurred.
   */
  protected ClangToken(Source source) throws Exception {
    super(source);
  }
}
