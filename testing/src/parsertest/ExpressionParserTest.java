package parsertest;

import static org.junit.Assert.*;

import frontend.Token;
import frontend.Source;
import frontend.Parser;
import frontend.FrontendFactory;
import frontend.clang.ClangParserTD;
import frontend.clang.parsers.ExpressionParser;
import intermediate.ICode;
import intermediate.ICodeNode;
import backend.Backend;
import backend.BackendFactory;

import intermediate.icodeimpl.ICodeImpl;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    ExpressionParser expressionParser = new ExpressionParser((ClangParserTD)parser);
    Backend executor = BackendFactory.createBackend("execute");
    Token token = expressionParser.nextToken();
    ICodeNode iCodeNode = expressionParser.parse(token);
    ICode iCode = new ICodeImpl();
    iCode.setRoot(iCodeNode);

    executor.process(iCode, executor.getSymTabStack());
  }
}