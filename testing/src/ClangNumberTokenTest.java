import static org.junit.Assert.assertEquals;

import frontend.Token;
import frontend.Scanner;
import frontend.Source;
import frontend.clang.ClangScanner;

import org.junit.Test;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.StringReader;

public class ClangNumberTokenTest {
  String testString = "12345";
  Source source;
  Scanner clangScanner;

  @Before
  public void setUp() throws Exception {
    source = new Source(new BufferedReader(new StringReader(testString)));
    clangScanner = new ClangScanner(source);
  }

  @Test
  public void testExtract() throws Exception {
    Token token = clangScanner.nextToken();
    assertEquals(testString, token.getText());
  }
}