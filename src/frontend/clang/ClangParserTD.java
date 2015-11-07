package frontend.clang;

import frontend.Parser;
import frontend.Scanner;

/**
 * <h1>ClangParserTD</h1>
 * <p>
 * <p>The top-down C language parser.</p>
 */
public class ClangParserTD extends Parser {

  /**
   * Constructor.
   *
   * @param scanner the scanner to be used with this parser.
   */
  public ClangParserTD(Scanner scanner) {

    super(scanner);
  }

  /**
   * Constructor.
   *
   * @param parent the parent parser.
   */
  public ClangParserTD(ClangParserTD parent) {
    super(parent.getScanner());
  }

  public void parse() throws Exception {
    // TODO
  }
}