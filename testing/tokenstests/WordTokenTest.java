package tokenstests;

import static org.junit.Assert.assertEquals;

import frontend.Token;
import frontend.Source;
import frontend.Scanner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class WordTokenTest {
  private String testString;
  private String tokenType;
  private String tokenText;

  public WordTokenTest(String testString, String tokenType, String tokenText) {
    this.testString = testString;
    this.tokenType = tokenType;
    this.tokenText = tokenText;
  }

  @Parameterized.Parameters
  public static Collection collectDate() {
    return Arrays.asList(new Object[][] {{"and", "LOGICAL_AND", "and"}, {"or", "LOGICAL_OR", "or"}, {"iF", "IDENTIFIER", "iF"}});
  }

  @Test
  public void testExtract() throws Exception {
    Source source = new Source(new BufferedReader(new StringReader(testString)));
    Scanner scanner = new Scanner(source);

    Token token = scanner.nextToken();
    assertEquals(tokenType, token.getType().toString());
    assertEquals(tokenText, token.getText());
  }
}