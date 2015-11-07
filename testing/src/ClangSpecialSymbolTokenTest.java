import static org.junit.Assert.*;

import frontend.Token;
import frontend.Source;
import frontend.Scanner;
import frontend.clang.ClangScanner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ClangSpecialSymbolTokenTest {
  private static String[] specialSymbols = {};

  private String testString;
  private String tokenText;
  private String tokenType;

  public ClangSpecialSymbolTokenTest(String testString, String tokenText, String tokenType) {
    this.testString = testString;
    this.tokenText = tokenText;
    this.tokenType = tokenType;
  }

  @Parameterized.Parameters
  public static Collection CollectDate() {
    return Arrays.asList(new Object[][] {
        {"+", "+", "ADD"}
    });
  }

  @Test
  public void testExtract() throws Exception {
    Source source = new Source(new BufferedReader(new StringReader(testString)));
    Scanner scanner = new ClangScanner(source);

    Token token = scanner.nextToken();
    assertEquals(tokenText, token.getText());
    assertEquals(tokenType, token.getType().toString());
  }
}