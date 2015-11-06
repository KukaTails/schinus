package frontend;

import frontend.clang.ClangScanner;

public class FrontendFactory
{
  /**
   * Create a parser.
   * @param language the name of the source language (e.g., "Pascal").
   * @param type the type of parser (e.g., "top-down").
   * @param source the source object.
   * @return the parser.
   * @throws Exception if an error occurred.
   */
  public static Parser createParser(String language, String type, Source source)
      throws Exception
  {
    if (language.equalsIgnoreCase("Pascal") &&
        type.equalsIgnoreCase("top-down"))
    {
      Scanner scanner = new ClangScanner(source);
      return new ClangParserTD(scanner);
    } else if (!language.equalsIgnoreCase("Pascal")) {
      throw new Exception("Parser factory: Invalid language '" +
          language + "'");
    }
    else {
      throw new Exception("Parser factory: Invalid type '" +
          type + "'");
    }
  }
}
