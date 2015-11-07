import static org.junit.Assert.*;

import frontend.Source;
import frontend.clang.tokens.ClangNumberToken;

import org.junit.Test;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.StringReader;

public class ClangNumberTokenTest {
  String testString = "12345";
  Source source;
  ClangNumberToken numberToken;

  @Before
  public void setUp() throws Exception {
    source = new Source(new BufferedReader(new StringReader(testString)));
    numberToken = new ClangNumberToken(source);
  }

  @Test
  public void testExtract() throws Exception {
    assertEquals(testString, numberToken.getText());
  }

  @Test
  public void testExtractNumber() throws Exception {

  }
}