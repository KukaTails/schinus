package frontend;

/**
 * <h1>FrontendFactory</h1>
 * <p>
 * <p>A factory class that create parsers for special source language.</p>
 */
public class FrontendFactory {
  /**
   * @param language the name of the source language.
   * @param type     the type of parser (e.g., "top-down").
   * @param source   the source object.
   * @return the parser.
   * @throws Exception if an error occurred.
   */
  public static Parser createParser(String language, String type, Source source) throws Exception {
    if (language.equalsIgnoreCase("schinus") && type.equalsIgnoreCase("top-down")) {
      Scanner scanner = new Scanner(source);
      return new Parser(scanner);
    } else if (!language.equalsIgnoreCase("schinus")) {
      throw new Exception("Parser factory: Invalid language '" + language + "'");
    } else {
      throw new Exception("Parser factory: Invalid type '" + type + "'");
    }
  }
}