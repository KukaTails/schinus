package parsesrtests;

import frontend.Token;
import frontend.Source;
import frontend.Parser;
import util.ObjectPrinter;
import intermediate.ICodeNode;
import frontend.FrontendFactory;
import frontend.parsers.IfStmtParser;
import objectmodel.dictionary.Dictionary;
import objectmodel.predefined.PredefinedConstant;
import interpreter.executors.IfStmtExecutor;

import org.junit.Test;

import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.reflect.Method;

public class IfStmtParserTest {
  private Dictionary GLOBAL_ENV = PredefinedConstant.GLOBAL_DICT;
  private IfStmtExecutor executor = new IfStmtExecutor();
  private Class ifStmtParserClass = IfStmtParser.class;

  Source source;
  Parser parser;
  IfStmtParser ifStmtParser;

  @Test
  public void parseIfStmtTest() throws Exception {
    MethodTest("if_stmt_test.txt", "parse");
  }

  private void MethodTest(String filePath, String methodName) throws Exception {
    Method method = initParse(filePath, methodName);
    Token token = ifStmtParser.nextToken();

    ICodeNode iCodeNode = (ICodeNode) method.invoke(ifStmtParser, token);
    Object result = executor.execute(iCodeNode, GLOBAL_ENV);
    ObjectPrinter.printValue(result);
  }

  private Method initParse(String filePath, String methodName) throws Exception {
    source = new Source(new BufferedReader(new FileReader(filePath)));
    parser = FrontendFactory.createParser("Schinus", "top-down", source);
    ifStmtParser = new IfStmtParser(parser);

    return setMethodPublic(methodName);
  }

  private Method setMethodPublic(String methodName) {
    Method[] methods = ifStmtParserClass.getDeclaredMethods();

    for (Method method : methods) {
      if (method.getName() == methodName) {
        method.setAccessible(true);
        return method;
      }
    }
    return null;
  }
}
