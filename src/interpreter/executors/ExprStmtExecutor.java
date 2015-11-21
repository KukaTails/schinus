package interpreter.executors;

import static intermediate.ICodeKey.*;
import static intermediate.ICodeNodeType.*;
import static interpreter.RuntimeErrorCode.*;
import static objectmodel.predefined.PredefinedConstant.NONE;
import static objectmodel.predefined.PredefinedType.*;

import interpreter.Executor;
import intermediate.ICodeNode;
import intermediate.ICodeNodeType;
import objectmodel.baseclasses.Base;
import objectmodel.baseclasses.Instance;
import objectmodel.dictionary.Dictionary;
import objectmodel.baseclasses.MethodInstance;
import interpreter.exception.UndefinedException;
import interpreter.exception.NotCallableException;
import interpreter.exception.AssignedFaultException;
import util.ObjectPrinter;

import java.util.EnumSet;
import java.util.ArrayList;

/**
 * <h1>ExpressionStatementExecutor</h1>
 * <p>
 * <p>Execute an expression.</p>
 */
public class ExprStmtExecutor extends Executor {
  public ExprStmtExecutor() {
  }

  /**
   * @param node the intermediate code node of the expression statement.
   * @return the computed value of the expression.
   */
  public Object execute(ICodeNode node, Dictionary environment) throws Exception {
    ICodeNodeType nodeType = node.getType();
    Object result;

    switch(nodeType) {
      case ASSIGN_EXP_NODE: {
        result = executeAssignExp(node, environment);
        break;
      }

      default: {
        result = executeTest(node, environment);
      }
    }
    return result;
  }

  private Object executeAssignExp(ICodeNode node, Dictionary environment) throws Exception {
    ArrayList<ICodeNode> children = node.getChildren();
    ICodeNode assignedNode = children.get(0);
    ICodeNode expNode = children.get(1);

    Base assignedObject = (Base)executeTest(assignedNode, environment);

    Dictionary parentEnv = ((Base) assignedObject).getFields().getDictionaryParent();
    String variableName = (String) ((Base) assignedObject).readAttribute("__name__");
    Object expObject = executeTest(expNode, environment);

    ((Base)expObject).writeAttr("__name__", variableName);
    if (parentEnv.containsKey(variableName)) {
      parentEnv.replace(variableName, expObject);
    } else {
      parentEnv.put(variableName, expObject);
    }

    return NONE;
  }


  private void createInstance(String objectName, Object object, Dictionary environment) {
    Object instance = object;

    if (instance instanceof Integer) {
      instance = new Instance(INTEGER, new Dictionary(), environment);
      ((Instance)instance).writeAttr("__value__", object);
    } else if (instance instanceof Float) {
      instance = new Instance(FLOAT, new Dictionary(), environment);
      ((Instance)instance).writeAttr("__value__", object);
    } else if (instance instanceof String) {
      instance = new Instance(STRING, new Dictionary(), environment);
    }

    environment.put(objectName, instance);
  }


  public Object executeTest(ICodeNode node, Dictionary environment) throws Exception {
    ICodeNodeType nodeType = node.getType();
    Object result = NONE;

    switch(nodeType) {
      case LOGICAL_OR_OP: {
        result = executeOrTest(node, environment);
        break;
      }

      case LOGICAL_AND_OP: {
        result = executeAndTest(node, environment);
        break;
      }

      case LOGICAL_NOT_OP: {
        result = executeNotTest(node, environment);
        break;
      }

      case ATOM_EXPR_NODE: {
        result = executeAtomExpr(node, environment);
        break;
      }

      case NONE_OPERAND:
      case IDENTIFIER_OPERAND:
      case STRING_LITERAL_OPERAND:
      case FLOAT_CONSTANT_OPERAND:
      case BOOLEAN_CONSTANT_OPERAND:
      case INTEGER_CONSTANT_OPERAND: {
        result = executeAtom(node, environment);
        break;
      }

      case NEGATE_OP: {
        // Get the NEGATE node's expression node child.
        ICodeNode expressionNode = node.getChildren().get(0);

        // Execute the expression and return the negative of its value.
        Object operand = executeTest(expressionNode, environment);
        Object value = getValue(operand);
        if (value instanceof Integer) {
          value = -((Integer) value);
        } else {
          value = -((Float) value);
        }

        ((Instance)operand).writeAttr("__value__", value);
        return operand;
      }

      // Must be a binary operator.
      default:
        return executeComparisonExpression(node, environment);
    }
    return result;
  }

  private Object executeOrTest(ICodeNode node, Dictionary environment) throws Exception {
    ArrayList<ICodeNode> children = node.getChildren();

    for (ICodeNode child : children) {
      Object value = executeTest(child, environment);
      if (convertValueTOBoolean(value)) {
        return createConstantInstance(true, environment);
      }
    }
    return createConstantInstance(false, environment);
  }

  public Object executeAndTest(ICodeNode node, Dictionary environment) throws Exception {
    ArrayList<ICodeNode> children = node.getChildren();

    for (ICodeNode child : children) {
      Object value = executeTest(child, environment);
      if (!convertValueTOBoolean(value)) {
        return createConstantInstance(false, environment);
      }
    }
    return createConstantInstance(true, environment);
  }

  public Object executeNotTest(ICodeNode node, Dictionary environment) throws Exception {
    if (node.getType() != LOGICAL_NOT_OP) {
      return executeTest(node, environment);
    } else {
      ICodeNode iCodeNode = node.getChildren().get(0);
      Object value = executeNotTest(iCodeNode, environment);

      return createConstantInstance(!convertValueTOBoolean(value), environment);
    }
  }

  private boolean convertValueTOBoolean(Object value) {
    if (value instanceof Boolean) {
      return (Boolean) value;
    } else if (value instanceof Integer) {
      return (Integer) value != 0;
    } else if (value instanceof Float) {
      return (Float) value != 0.0;
    } else if (value instanceof Instance) {
      Object instanceValue = ((Instance) value).readAttribute("__value__");
      return convertValueTOBoolean(instanceValue);
    } else if (value instanceof Class) {
      return true;
    } else {
      return true;
    }
  }

  // Set of arithmetic operator node types.
  private static final EnumSet<ICodeNodeType> ARITH_OPS = EnumSet.of(ADD_OP, SUB_OP, MULTIPLY_OP,
      FLOAT_DIVIDE_OP, INTEGER_DIVIDE_OP, MOD_OP, POWER_OP);

  /**
   * Execute a binary operator.
   *
   * @param node     the root node of the expression.
   * @return the computed value of the expression.
   */
  private Object executeComparisonExpression(ICodeNode node, Dictionary environment) throws Exception{
    // Get the two operand children of the operator node.
    ICodeNodeType nodeType = node.getType();
    ArrayList<ICodeNode> children = node.getChildren();
    ICodeNode leftOperandNode = children.get(0);
    ICodeNode rightOperandNode = children.get(1);

    Object leftOperand = executeTest(leftOperandNode, environment);
    Object rightOperand = executeTest(rightOperandNode, environment);

    Object leftValue = getValue(leftOperand);
    Object rightValue = getValue(rightOperand);

    Object arithResult = 0;
    boolean integerMode = (leftValue instanceof Integer) && (rightValue instanceof Integer);
    // ====================
    // Arithmetic operators
    // ====================
    if (ARITH_OPS.contains(nodeType)) {
      if (integerMode) {
        int value1 = (Integer) leftValue;
        int value2 = (Integer) rightValue;

        // Integer operations.
        switch(nodeType) {
          case ADD_OP:
            arithResult = value1 + value2;
            break;
          case SUB_OP:
            arithResult = value1 - value2;
            break;
          case MULTIPLY_OP:
            arithResult = value1 * value2;
            break;
          case POWER_OP: {
            double result = Math.pow(value1, value2);
            arithResult = Math.floor(result + 0.5d);
            break;
          }
          case FLOAT_DIVIDE_OP: {
            // Check for division by zero.
            if (value2 != 0) {
              arithResult = ((float) value1) / ((float) value2);
            } else {
              errorHandler.flag(node, DIVISION_BY_ZERO, this);
              arithResult = 0;
            }
            break;
          }

          case INTEGER_DIVIDE_OP: {
            // Check for division by zero.
            if (value2 != 0) {
              arithResult = value1 / value2;
            } else {
              errorHandler.flag(node, DIVISION_BY_ZERO, this);
              arithResult = 0;
            }
            break;
          }

          case MOD_OP: {
            // Check for division by zero.
            if (value2 != 0) {
              arithResult = value1 % value2;
            } else {
              errorHandler.flag(node, DIVISION_BY_ZERO, this);
              arithResult = 0;
            }
            break;
          }
        }
      } else {
        float value1 = leftValue instanceof Integer ? (Integer) leftValue : (Float) leftValue;
        float value2 = rightValue instanceof Integer ? (Integer) rightValue : (Float) rightValue;

        // Float operations.
        switch(nodeType) {
          case ADD_OP:
            arithResult = value1 + value2;
            break;
          case SUB_OP:
            arithResult = value1 - value2;
            break;
          case POWER_OP:
            arithResult = Math.pow(value1, value2);
            break;
          case MULTIPLY_OP:
            arithResult = value1 * value2;
            break;
          case FLOAT_DIVIDE_OP: {
            // Check for division by zero.
            if (value2 != 0.0f) {
              arithResult = value1 / value2;
            } else {
              errorHandler.flag(node, DIVISION_BY_ZERO, this);
              arithResult =  0.0f;
            }
            break;
          }
        }
      }
    }

    // ====================
    // Relational operators
    // ====================
    else if (integerMode) {
      int value1 = (Integer) leftValue;
      int value2 = (Integer) rightValue;

      // Integer operands.
      switch(nodeType) {
        case EQ_OP:
          arithResult =  value1 == value2;
          break;
        case NE_OP:
          arithResult =  value1 != value2;
          break;
        case LT_OP:
          arithResult =  value1 < value2;
          break;
        case LE_OP:
          arithResult =  value1 <= value2;
          break;
        case GT_OP:
          arithResult =  value1 > value2;
          break;
        case GE_OP:
          arithResult =  value1 >= value2;
          break;
      }
    } else {
      float value1 = leftValue instanceof Integer ? (Integer) leftValue : (Float) leftValue;
      float value2 = rightValue instanceof Integer ? (Integer) rightValue : (Float) rightValue;

      // Float operands.
      switch(nodeType) {
        case EQ_OP:
          arithResult =  value1 == value2;
          break;
        case NE_OP:
          arithResult =  value1 != value2;
          break;
        case LT_OP:
          arithResult =  value1 < value2;
          break;
        case LE_OP:
          arithResult =  value1 <= value2;
          break;
        case GT_OP:
          arithResult =  value1 > value2;
          break;
        case GE_OP:
          arithResult =  value1 >= value2;
          break;
      }
    }
    return createConstantInstance(arithResult, environment);
  }

  /**
   * Used to get the value of object
   * @param value the object from which used to get value.
   * @return the value of object
   */
  private Object getValue(Object value) {
    Object returnValue = NONE;

    if (value instanceof MethodInstance) {
      return returnValue;
    } else if (value instanceof Instance) {
      return ((Base) value).readAttribute("__value__");
    } else if (value instanceof Integer || value instanceof Float) {
      return value;
    } else {
      return value;
    }
  }

  private Object executeAtomExpr(ICodeNode iCodeNode, Dictionary environment) throws Exception {
    ArrayList<ICodeNode> children = iCodeNode.getChildren();
    ICodeNode currentNode = children.get(0);
    Dictionary currentEnv = environment;
    Object currentObject = executeAtom(currentNode, currentEnv);

    if (children.size() == 1) {
      if (currentObject instanceof String) {
        String name = (String)currentObject;
        currentObject = environment.get(currentObject);
        if (currentObject == null) {
          currentObject = new Instance(TMPTYPE, new Dictionary(), environment);
          ((Instance)currentObject).writeAttr("__name__", name);
        }
      }
      return currentObject;
    }

    // the type of currentObject should be String or Instance
    if (!(currentObject instanceof String || currentObject instanceof Base)) {
      errorHandler.flag(currentNode, ASSIGN_TO_LEFT_VALUE, this);
      throw new AssignedFaultException();
    } else if (currentObject instanceof String) {
      currentObject = environment.get(currentObject);

      if (currentObject == null) {
        errorHandler.flag(currentNode, UNDEFINED_NAME, this);
        throw new UndefinedException();
      }
    }

    for (int i = 1; i < children.size(); ++ i) {
      ICodeNode child = children.get(i);
      ICodeNodeType nodeType = child.getType();

      switch(nodeType) {
        case ARGUMENTS_TRAILER: {
          // object is not callable
          if (!(currentObject instanceof MethodInstance)) {
            errorHandler.flag(child, CALL_UNCALLABLE_OBJECT, this);
            throw new NotCallableException();
          }

          MethodInstance method = (MethodInstance) currentObject;
          ICodeNode argumentsNode = child.getChildren().get(0);
          ArrayList<Object> arguments = executeArguList(argumentsNode, environment);
          currentObject = method.callMethod(arguments);
          currentEnv = ((MethodInstance) currentObject).getFields();
          break;
        }

        case SUBSCRIPT_TRAILER: {
          break;
        }

        case FIELD_TRAILER: {
          String fieldName = (String) child.getAttribute(FIELD_NAME);
          currentEnv = ((Base) currentObject).getFields();
          currentObject = currentEnv.get(fieldName);
          break;
        }

        default: {
          // TODO
          errorHandler.flag(child, UNIMPLEMENTED_FEATURE, this);
        }
      }
    }
    return currentObject;
  }


  /**
   * Parse atom.
   *
   * @param node        intermediate code node of atom.
   * @param environment the environment used to execute intermediate code node.
   * @return result of executing the intermediate code node.
   */
  private Object executeAtom(ICodeNode node, Dictionary environment) throws Exception {
    ICodeNodeType nodeType = node.getType();

    switch(nodeType) {
      case IDENTIFIER_OPERAND: {
        String identifierName = (String)node.getAttribute(IDENTIFIER_NAME);
        Object identifier = environment.get(identifierName);

        if (identifier == null) {
          identifier = new Instance(TMPTYPE, new Dictionary(), environment);
        }
        ((Base)identifier).writeAttr("__name__", node.getAttribute(IDENTIFIER_NAME));
        return identifier;
      }

      case STRING_LITERAL_OPERAND:
      case FLOAT_CONSTANT_OPERAND:
      case INTEGER_CONSTANT_OPERAND:
      case BOOLEAN_CONSTANT_OPERAND: {
        // create constant instance
        return createConstantInstance(node.getAttribute(VALUE), environment);
      }

      case NONE_OPERAND: {
        return NONE;
      }

      default: {
        return executeTest(node, environment);
      }
    }
  }

  private ArrayList<Object> executeArguList(ICodeNode node, Dictionary environment) throws Exception {
    ArrayList<ICodeNode> arguments = node.getChildren();
    ArrayList<Object> argumentsObject = new ArrayList<>();

    for (ICodeNode argument : arguments) {
      Object argOperand = executeArgument(argument, environment);
      argumentsObject.add(argOperand);
    }

    return argumentsObject;
  }

  private Object executeArgument(ICodeNode node, Dictionary environment) throws Exception {
    ArrayList<ICodeNode> children = node.getChildren();
    Object argument = NONE;

    if (children.size() == 2) {
      ICodeNode leftExpNode = children.get(0);
      ICodeNode rightExpNode = children.get(1);

      Object leftOperand = executeTest(leftExpNode, environment);
      Object rightOperand = executeTest(rightExpNode, environment);

      if (!(leftOperand instanceof Base)) {
        errorHandler.flag(leftExpNode, ASSIGN_TO_LEFT_VALUE, this);
      }

      String leftOperandName = (String) ((Base) leftOperand).readAttribute("__name__");
      if (environment.containsKey(leftOperandName)) {
        environment.replace(leftOperandName, rightOperand);
      } else {
        environment.put(leftOperandName, rightOperand);
      }
      argument = rightOperand;
    } else {
      ICodeNode expNode = children.get(0);
      argument = executeTest(expNode, environment);
    }
    return argument;
  }
}