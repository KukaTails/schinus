import frontend.Token;
import frontend.Source;
import frontend.Parser;
import interpreter.Executor;
import intermediate.ICodeNode;
import frontend.tokens.EofToken;
import frontend.FrontendFactory;
import objectmodel.dictionary.Dictionary;
import objectmodel.predefined.GlobalConstant;
import objectmodel.predefined.PredefinedType;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * <h1>Schinus</h1>
 * <p>
 * <p>interpret a Schinus language source program.</p>
 */
public class Schinus {
  private Source source;            // the source of the interpreter
  private Parser parser;            // the parser of the interpreter
  private Executor executor;        // the backend of the interpreter

  public Schinus(String filePath) {
    final String PROMPT = ">>> ";
    boolean inputFromConsole = false;
    boolean firstParse = true;
    Token token;
    filePath = "E:\\test.txt";

    try {
      if (filePath != null) {
        inputFromConsole = false;
        source = new Source(new BufferedReader(new FileReader(filePath)));
      } else {
        inputFromConsole = true;
        source = new Source(new BufferedReader(new InputStreamReader(System.in)));
      }

      parser = FrontendFactory.createParser("schinus", "top-down", source);
      parser.addMessageListener(new ParserMessageListener());

      // get global environment
      Dictionary globalEnvironment = GlobalConstant.GLOBAL_DICT;

      executor = new Executor();
      executor.addMessageListener(new InterpreterMessageListener());

      //parser.nextToken();
      do {
        if (inputFromConsole) {
          System.out.print(PROMPT);
        }
        if (firstParse) {
          parser.nextToken();
          firstParse = false;
        }
        ICodeNode node = parser.parse();
        if (parser.getErrorCount() == 0) {
          executor.process(node, globalEnvironment);
        }
        parser.clearErrorCount();
      } while (!((token = parser.currentToken()) instanceof EofToken));

      source.close();
    } catch(Exception ex) {
      System.out.println("***** Internal translator error.*****");
      ex.printStackTrace();
    }
  }

  /**
   * The main method.
   *
   * @param args command-line arguments: "execute" followed
   *             by optional flags followed by the source file path.
   */
  public static void main(String args[]) {
    int i = 0;
    String filePath = null;
    String flags = null;

    // get Flags.
    for (; (i < args.length) && (args[i].charAt(0) == '-'); ++i) {
      flags += args[i].substring(1);
    }

    // Source path.
    if (i < args.length) {
      filePath = args[i];
    }

    new Schinus(filePath);
  }
}
