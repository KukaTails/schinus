package parsesrtest;

import frontend.Token;
import frontend.Source;
import frontend.Parser;
import frontend.FrontendFactory;
import frontend.parsers.ExpressionStatementParser;

import intermediate.ICodeNode;
import objectmodel.dictionary.Dictionary;
import interpreter.executors.ExpressionStatementExecutor;

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
public class ExpressionStatementParserTest {
  private Dictionary GLOBAL_ENV = PredefinedConstant.GLOBAL_DICT;
  private ExpressionStatementExecutor executor = new ExpressionStatementExecutor();
  private Class expParserClass = ExpressionStatementParser.class;

  Source source;
  Parser parser;
  ExpressionStatementParser expressionParser;

  @Test
  public void AtomParseTest() throws Exception {
    String[] testCases = {"identifier", "\'string\'", "23323", "3.456", "None", "True", "False"};

    for (String testCase: testCases) {
      Method parseAtomMethod = initParse(testCase, "parseAtom");
      Token token = expressionParser.nextToken();
      ICodeNode iCodeNode = (ICodeNode) parseAtomMethod.invoke(expressionParser, token);

      Object result = executor.execute(iCodeNode, GLOBAL_ENV);
      System.out.print(testCase + " = ");
      ObjectPrinter.printValue(result);
    }
  }

  @Test
  public void atomExprParseTest() throws Exception {
    String[] testCases = {"1234"};

    for (String testCase : testCases) {
      Method parseAtomExprMethod = initParse(testCase, "parseAtomExpr");
      Token token = expressionParser.nextToken();

      ICodeNode iCodeNode = (ICodeNode) parseAtomExprMethod.invoke(expressionParser, token);
      Object result = executor.execute(iCodeNode, GLOBAL_ENV);
      System.out.print(testCase + " = ");

      ObjectPrinter.printValue(result);
    }
  }

  @Test
  public void parsePowerTest() throws Exception {
    String[] testCases = {"2 ** 3", "2.9833 ** 10", "10.11 ** 10.11"};

    for (String testCase : testCases) {
      Method method = initParse(testCase, "parsePower");
      Token token = expressionParser.nextToken();

      ICodeNode iCodeNode = (ICodeNode) method.invoke(expressionParser, token);
      Object result = executor.execute(iCodeNode, GLOBAL_ENV);
      System.out.print(testCase + " = ");
      ObjectPrinter.printValue(result);
    }
  }

  @Test
  public void parseFactorTest() throws Exception {
    String[] testCases = {"+12", "-10", "-10.29", "--193", "--192.3", "++20132159.99"};

    for (String testCase : testCases) {
      Method method = initParse(testCase, "parseFactor");
      Token token = expressionParser.nextToken();

      ICodeNode iCodeNode = (ICodeNode)method.invoke(expressionParser, token);
      Object result = executor.execute(iCodeNode, GLOBAL_ENV);
      System.out.print(testCase + " = ");
      ObjectPrinter.printValue(result);
    }
  }

  @Test
  public void parseTermTest() throws Exception {
    String[] testCases = {"+12 * -10", "-10 / --1", "-10.29", "--193 // 2 // 4", "--192.3 * -10", "++20132159 % 10"};

    for (String testCase : testCases) {
      Method method = initParse(testCase, "parseTerm");
      Token token = expressionParser.nextToken();

      ICodeNode iCodeNode = (ICodeNode)method.invoke(expressionParser, token);
      Object result = executor.execute(iCodeNode, GLOBAL_ENV);
      System.out.print(testCase + " = ");
      ObjectPrinter.printValue(result);
    }
  }


  @Test
  public void parseComparsion() throws Exception {
    String[] testCases = {
        "2 != 2", "1 > 2", "2 < 3" ,
        "10.5 >= 10.5", "10 >= 10.65",
        "20.3 <= 10", "10 < 20",
        "2 == 2", "10.3 == (10 + 0.3)",
        "2 != 2.0",  "2 != 1 + 1"};

    for (String testCase : testCases) {
      Method method = initParse(testCase, "parseComparisonExpression");
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
    expressionParser = new ExpressionStatementParser(parser);

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
