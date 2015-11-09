package objectmodeltest;

/**
 * Created by Keen on 2015/11/8.
 */
public class Method {
  public int call(Base base) {
    return (int)base.getFields().get("x") + 1;
  }
}
