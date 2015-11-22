package interpreter.executors;

import static intermediate.ICodeKey.CLASS_NAME;
import static intermediate.ICodeKey.BASE_CLASSES_NAME;
import static interpreter.RuntimeErrorCode.UNDEFINED_NAME;
import static interpreter.RuntimeErrorCode.OBJECT_IS_NOT_CLASS;
import static objectmodel.predefined.PredefinedType.TYPE;
import static objectmodel.predefined.PredefinedType.OBJECT;
import static objectmodel.predefined.PredefinedConstant.NO_PRINT;

import intermediate.ICodeNode;
import interpreter.exception.ReturnFlowException;
import objectmodel.baseclasses.Class;
import objectmodel.dictionary.Dictionary;

import java.util.ArrayList;

public class ClassDefStmtExecutor extends StmtExecutor {
  public ClassDefStmtExecutor() {
  }

  public Object execute(ICodeNode iCodeNode, Dictionary environment)
      throws ReturnFlowException {
    ArrayList<ICodeNode> children = iCodeNode.getChildren();
    ICodeNode arglistNode = children.get(0);
    ICodeNode classBodyNode = children.get(1);

    String clsName = (String)iCodeNode.getAttribute(CLASS_NAME);
    ArrayList<String> baseClsesName = (ArrayList<String>)arglistNode.getAttribute(BASE_CLASSES_NAME);

    for (String baseClsName: baseClsesName) {
      Object cls = environment.get(baseClsName);

      if (cls == null)
        errorHandler.flag(arglistNode, UNDEFINED_NAME, this);
      if (!(cls instanceof Class))
        errorHandler.flag(arglistNode, OBJECT_IS_NOT_CLASS, this);
    }

    Class newCls;
    if (baseClsesName.size() == 0)
      newCls = new Class(clsName, OBJECT, new Dictionary(), TYPE, environment, environment);
    else {
      Object baseCls = environment.get(baseClsesName.get(0));
      newCls = new Class(clsName, (Class)baseCls, new Dictionary(), TYPE, environment, environment);
    }
    newCls.writeAttr("__name__", clsName);
    environment.put(clsName, newCls);

    ArrayList<ICodeNode> stmtsNode = classBodyNode.getChildren();
    StmtExecutor stmtExecutor = new StmtExecutor();
    Dictionary fields = newCls.getFields();

    for (ICodeNode stmtNode : stmtsNode) {
      stmtExecutor.execute(stmtNode, fields);
    }
    return NO_PRINT;
  }
}