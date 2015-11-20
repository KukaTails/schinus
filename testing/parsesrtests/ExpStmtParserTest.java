package parsesrtests;

import frontend.Token;
import frontend.Source;
import frontend.Parser;
import frontend.FrontendFactory;
import frontend.parsers.ExprStmtParser;

import intermediate.ICodeNode;
import objectmodel.dictionary.Dictionary;
import interpreter.executors.ExprStmtExecutor;

import util.ObjectPrinter;
import objectmodel.predefined.PredefinedConstant;

import org.junit.Test;

import java.io.StringReader;
import java.io.BufferedReader;
import java.lang.reflect.Method;

/**
 * <h1>ExpressionParser</h1>
 *
 * <p>Testing on parsing expression</p>
 */
public class ExpStmtParserTest {
  private Dictionary GLOBAL_ENV = PredefinedConstant.GLOBAL_DICT;
  private ExprStmtExecutor executor = new ExprStmtExecutor();
  private Class expParserClass = ExprStmtParser.class;

  Source source;
  Parser parser;
  ExprStmtParser expressionParser;

  @Test
  public void AtomParseTest() throws Exception {
    String[] testCases = {"identifier", "\'string\'", "23323", "3.456", "None", "True", "False"};

    MethodTest(testCases, "parseAtom");
  }

  @Test
  public void atomExprParseTest() throws Exception {
    String[] testCases = {"1234"};

    MethodTest(testCases, "parseAtomExpr");
  }

  @Test
  public void parsePowerTest() throws Exception {
    String[] testCases = {"2 ** 3", "2.9833 ** 10", "10.11 ** 10.11"};

    MethodTest(testCases, "parsePower");
  }

  @Test
  public void parseFactorTest() throws Exception {
    String[] testCases = {"+12", "-10", "-10.29", "--193", "--192.3", "++20132159.99"};

    MethodTest(testCases, "parseFactor");
  }

  @Test
  public void parseTermTest() throws Exception {
    String[] testCases = {"+12 * -10", "-10 / --1", "-10.29", "--193 // 2 // 4", "--192.3 * -10", "++20132159 % 10"};

    MethodTest(testCases, "parseTerm");
  }

  @Test
  public void parseArithExprTest() throws Exception {
    String[] testCases = {"-90 + 39.98", "-983 - 839"};

    MethodTest(testCases, "parseArithExpression");
  }


  @Test
  public void parseComparsionTest() throws Exception {
    String[] testCases = {
        "2 != 2", "1 > 2", "2 < 3" ,
        "10.5 >= 10.5", "10 >= 10.65",
        "20.3 <= 10", "10 < 20",
        "2 == 2", "10.3 == (10 + 0.3)",
        "2 != 2.0",  "2 != 1 + 1"};

    MethodTest(testCases, "parseComparisonExpression");
  }

  @Test
  public void parseNotTest() throws Exception {
    String[] testCases = {"not False", "not True", "not 1", "not 1.1", "not 0", "not 0.0"};

    MethodTest(testCases, "parseNotTest");
  }

  @Test
  public void parseAndTest() throws Exception {
    String[] testCases = {
        "1 and 2", "1 and 0", "1.0 and 2.0", "0.4 and 0.0",
        "\'string\' and \'test\'",
        "True and True", "False and True", "True and False",
    };

    MethodTest(testCases, "parseAndTest");
  }

  @Test
  public void parseOrTest() throws Exception {
    String[] testCases = {
        "false or 1", "false or true", "false or \'sting\'",
        "0 or 1", "0 or 0.0", "0.0 or 1.0", "0.0 or 1", "1.3 or 8.9",
        "\'string\' or \'student\'"
    };

    MethodTest(testCases, "parseOrTest");
  }

  @Test
  public void parseAssignExp() throws Exception {
    String[] testCases = {
        "a = 1", "a", "a = \'string\'", "a", "b = 2", "b",
        "c = False", "c", "d = 1.1245", "d", "f = 12", "f",
        "g = \'g\'", "g", "h = 12 + 124", "h", "i = 1 and 2 and 3", "i"
    };

    MethodTest(testCases, "parse");
  }


  private void MethodTest(String[] testCases, String methodName) throws Exception {
    for (String testCase : testCases) {
      Method method = initParse(testCase, methodName);
      Token token = expressionParser.nextToken();

      ICodeNode iCodeNode = (ICodeNode) method.invoke(expressionParser, token);
      Object result = executor.execute(iCodeNode, GLOBAL_ENV);
      System.out.print(testCase + " = ");
      ObjectPrinter.printValue(result);

    }
  }

  private Method initParse(String testCase, String methodName) throws Exception {
    source = new Source(new BufferedReader(new StringReader(testCase)));
    parser = FrontendFactory.createParser("Schinus", "top-down", source);
    expressionParser = new ExprStmtParser(parser);

    return setMethodPublic(methodName);
  }

  private Method setMethodPublic(String methodName) {
    Method[] methods = expParserClass.getDeclaredMethods();

    for (Method method : methods) {
      if (method.getName() == methodName) {
        method.setAccessible(true);
        return method;
      }
    }
    return null;
  }
}
