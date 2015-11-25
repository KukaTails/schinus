package frontend.tokens;

import frontend.Token;
import frontend.Source;

import java.io.IOException;

public class EofToken extends Token {
  /**
   * @param source the source from where to fetch subsequent characters.
   * @throws IOException if an error occurred.
   */
  public EofToken(Source source) throws IOException {
    super(source);
  }

  /**
   * Do nothing.  Do not consume any source characters.
   * @throws IOException if an error occurred.
   */
  @Override
  protected void extract() throws IOException {
  }
}
