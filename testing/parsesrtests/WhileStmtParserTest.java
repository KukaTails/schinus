package parsesrtests;

import frontend.Token;
import frontend.Source;
import frontend.Parser;
import interpreter.executors.StmtExecutor;
import util.ObjectPrinter;
import intermediate.ICodeNode;
import frontend.tokens.EofToken;
import frontend.FrontendFactory;
import objectmodel.dictionary.Dictionary;
import objectmodel.predefined.PredefinedConstant;

import org.junit.Test;

import java.io.FileReader;
import java.io.BufferedReader;

public class WhileStmtParserTest {
  private Dictionary GLOBAL_ENV = PredefinedConstant.GLOBAL_DICT;
  private StmtExecutor executor = new StmtExecutor();

  Source source;
  Parser parser;

  @Test
  public void parseTest() throws Exception {
    MethodTest("while_stmt_test.txt");
  }

  private void MethodTest(String filePath) throws Exception {
    initParse(filePath);
    Token token = parser.nextToken();

    do {
      ICodeNode iCodeNode = parser.parse();
      Object result = executor.execute(iCodeNode, GLOBAL_ENV);
      ObjectPrinter.printValue(result);
    } while (!(parser.currentToken() instanceof EofToken));
  }

  private void initParse(String filePath) throws Exception {
    source = new Source(new BufferedReader(new FileReader(filePath)));
    parser = FrontendFactory.createParser("Schinus", "top-down", source);
  }
}