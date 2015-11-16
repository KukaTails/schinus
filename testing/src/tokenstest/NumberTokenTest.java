package tokenstest;

import static org.junit.Assert.assertEquals;

import frontend.Token;
import frontend.Source;
import frontend.Scanner;

import org.junit.Test;
import org.junit.Before;

import java.io.StringReader;
import java.io.BufferedReader;

public class NumberTokenTest {
  String testString = "12345";
  Source source;
  Scanner Scanner;

  @Before
  public void setUp() throws Exception {
    source = new Source(new BufferedReader(new StringReader(testString)));
    Scanner = new Scanner(source);
  }

  @Test
  public void testExtract() throws Exception {
    Token token = Scanner.nextToken();
    assertEquals(testString, token.getText());
  }
}