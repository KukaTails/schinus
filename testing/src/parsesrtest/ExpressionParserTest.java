package parsesrtest;

import frontend.Token;
import frontend.Source;
import frontend.Parser;
import frontend.FrontendFactory;
import frontend.parsers.ExpressionParser;

import intermediate.ICode;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;


/**
 * <h1>ExpressionParser</h1>
 *
 * <p>Testing on parsing expression</p>
 */
public class ExpressionParserTest {

  @Test
  public void testParse() throws Exception {
    Source source = new Source(new BufferedReader(new StringReader("2 + 5")));
    Parser parser = FrontendFactory.createParser("C", "top-down", source);
    ExpressionParser expressionParser = new ExpressionParser((Parser)parser);
    Backend executor = BackendFactory.createBackend("execute");
    Token token = expressionParser.nextToken();
    ICodeNode iCodeNode = expressionParser.parse(token);
    ICode iCode = new ICode();
    iCode.setRoot(iCodeNode);

    executor.process(iCode, executor.getSymTabStack());
  }
}