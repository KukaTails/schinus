package objectmodeltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Hashtable;

/**
 * Test for Base class's methods
 */
public class BaseTest {
  // Set up the base hierarchy in the ObjVLisp model
  // the ultimate base class is OBJECT
  private static Class OBJECT = new Class("object", null, new Hashtable<String, Object>(), null);
  private static Class TYPE = new Class("type", OBJECT, new Hashtable<String, Object>(), null);
  static {
    // TYPE and OBJECT is an instance of TYPE
    OBJECT.setClassName(TYPE);
    TYPE.setClassName(TYPE);
  }

  private Hashtable<String, Object> aDict = new Hashtable<>();
  Class testClass;
  {
    aDict.put("a", 1);
    testClass = new Class("A", OBJECT, aDict, TYPE);
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
    Class A = new Class("A", OBJECT, new Hashtable<String, Object>(), TYPE);
    Class B = new Class("B", A, new Hashtable<String, Object>(), TYPE);
    Instance b = new Instance(B);

    assertTrue(b.isInstanceOf(B));
    assertTrue(b.isInstanceOf(A));
    assertTrue(b.isInstanceOf(OBJECT));
    assertFalse(b.isInstanceOf(TYPE));
  }

  @Test
  public void testCallMethod() throws Exception {
    Method method = new Method();
    Hashtable<String, Object> aDict = new Hashtable<>();

    aDict.put("method", method);
    Class A = new Class("A", OBJECT, aDict, TYPE);
    Instance obj = new Instance(A);
    obj.writeAttr("x", 1);
    int value = (int)obj.callMethod("method", null);
    assertEquals(obj.callMethod("method", null), 2);
  }

  @Test
  public void testCallMethodWithSubclassingAndArgument() throws Exception {

  }
}