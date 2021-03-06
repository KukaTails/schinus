package parsesrtests;

import static org.junit.Assert.*;

import frontend.Token;
import frontend.Parser;
import frontend.Source;
import util.ObjectPrinter;
import intermediate.ICodeNode;
import frontend.tokens.EofToken;
import frontend.FrontendFactory;
import objectmodel.dictionary.Dictionary;
import interpreter.executors.StmtExecutor;
import objectmodel.predefined.PredefinedConstant;

import org.junit.Test;

import java.io.FileReader;
import java.io.BufferedReader;

public class ClassDefStmtParserTest {
  private Dictionary GLOBAL_ENV = PredefinedConstant.GLOBAL_DICT;
  private StmtExecutor executor = new StmtExecutor();

  Source source;
  Parser parser;

  @Test
  public void testParse() throws Exception {
    MethodTest("class_def_test.txt");
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