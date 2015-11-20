package objectmodeltests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import objectmodel.baseclasses.Class;
import objectmodel.baseclasses.Instance;

import objectmodel.dictionary.Dictionary;

import org.junit.Test;

/**
 * Test for Base class's methods
 */
public class BaseTest {
  // Set up the base hierarchy in the ObjVLisp model
  // the ultimate base class is OBJECT
  private static Class OBJECT = new Class("object", null, new Dictionary(), null, null);
  private static Class TYPE = new Class("type", OBJECT, new Dictionary(), null, null);
  static {
    // TYPE and OBJECT is an instance of TYPE
    OBJECT.setClassName(TYPE);
    TYPE.setClassName(TYPE);
  }

  private Dictionary aDict = new Dictionary();
  Class testClass;
  {
    aDict.put("a", 1);
    testClass = new Class("A", OBJECT, aDict, TYPE, null);
  }

  @Test
  public void testReadAttribute() throws Exception {
    // test the method to read attribute
    assertEquals(1, testClass.readAttribute("a"));
  }

  @Test
  public void testWriteAttr() throws Exception {
    testClass.writeAttr("a", 5);

    // test for write attribute
    assertEquals(5, testClass.readAttribute("a"));
  }

  @Test
  public void testIsInstanceOf() throws Exception {
    Class A = new Class("A", OBJECT, new Dictionary(), TYPE, null);
    Class B = new Class("B", A, new Dictionary(), TYPE, null);
    Instance b = new Instance(B, new Dictionary(), B.getFields());

    assertTrue(b.isInstanceOf(B));
    assertTrue(b.isInstanceOf(A));
    assertTrue(b.isInstanceOf(OBJECT));
    assertFalse(b.isInstanceOf(TYPE));
  }

  @Test
  public void testCallMethod() throws Exception {
  }

  @Test
  public void testCallMethodWithSubclassingAndArgument() throws Exception {
  }
}