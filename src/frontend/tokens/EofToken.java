package frontend.tokens;

import frontend.Token;
import frontend.Source;

public class EofToken extends Token {
  /**
   * @param source the source from where to fetch subsequent characters.
   * @throws Exception if an error occurred.
   */
  public EofToken(Source source) throws Exception {
    super(source);
  }

  /**
   * Do nothing.  Do not consume any source characters.
   *
   * @throws Exception if an error occurred.
   */
  @Override
  protected void extract() throws Exception {
  }
}
