package interpreter.executors;

import static intermediate.ICodeKey.*;
import static intermediate.ICodeNodeType.*;
import static interpreter.RuntimeErrorCode.*;
import static objectmodel.predefined.PredefinedType.TMPTYPE;
import static objectmodel.predefined.PredefinedConstant.NONE;
import static objectmodel.predefined.PredefinedConstant.NO_PRINT;

import interpreter.Executor;
import intermediate.ICodeNode;
import intermediate.ICodeNodeType;
import objectmodel.baseclasses.Base;
import objectmodel.baseclasses.Class;
import objectmodel.baseclasses.Instance;
import objectmodel.baseclasses.MethodInstance;
import objectmodel.dictionary.Dictionary;
import interpreter.exception.NotCallableException;

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
    Object result = NO_PRINT;

    switch(nodeType) {
      case ASSIGN_EXP_NODE: {
        result = executeAssignExp(node, environment);
        break;
      }

      case EXPRESSION_NODE: {
        result = executeExpr(node, environment);
        break;
      }

      default: {
        errorHandler.flag(node, UNIMPLEMENTED_FEATURE, this);
      }
    }
    return result;
  }

  private Object executeAssignExp(ICodeNode node, Dictionary environment) throws Exception {
    ArrayList<ICodeNode> children = node.getChildren();
    ICodeNode assignedNode = children.get(0);
    ICodeNode expNode = children.get(1);

    Base assignedObject = (Base)executeTest(assignedNode, environment);
    Dictionary existedEnv = ((Base) assignedObject).getExistedEnv();
    String variableName = (String) ((Base) assignedObject).readAttribute("__name__");
    Object expObject = executeTest(expNode, environment);

    ((Base)expObject).writeAttr("__name__", variableName);
    ((Base)expObject).setExistedEnv(assignedObject.getExistedEnv());
    if (existedEnv.containsKey(variableName)) {
      existedEnv.replace(variableName, expObject);
    } else {
      existedEnv.put(variableName, expObject);
    }

    return NONE;
  }

  public Object executeExpr(ICodeNode node, Dictionary environment) throws Exception {
    node = node.getChildren().get(0);
    return executeTest(node, environment);
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
        return createConstantInstance(true, environment, environment);
      }
    }
    return createConstantInstance(false, environment, environment);
  }

  public Object executeAndTest(ICodeNode node, Dictionary environment) throws Exception {
    ArrayList<ICodeNode> children = node.getChildren();

    for (ICodeNode child : children) {
      Object value = executeTest(child, environment);
      if (!convertValueTOBoolean(value)) {
        return createConstantInstance(false, environment, environment);
      }
    }
    return createConstantInstance(true, environment, environment);
  }

  public Object executeNotTest(ICodeNode node, Dictionary environment) throws Exception {
    if (node.getType() != LOGICAL_NOT_OP) {
      return executeTest(node, environment);
    } else {
      ICodeNode iCodeNode = node.getChildren().get(0);
      Object value = executeNotTest(iCodeNode, environment);

      return createConstantInstance(!convertValueTOBoolean(value), environment, environment);
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
            arithResult = Math.pow(value1, value2);
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
          case INTEGER_DIVIDE_OP: {
            arithResult = (int)(value1 / value2);
            break;
          }
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
    return createConstantInstance(arithResult, environment, environment);
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
      return currentObject;
    }

    for (int i = 1; i < children.size(); ++ i) {
      ICodeNode child = children.get(i);
      ICodeNodeType nodeType = child.getType();

      switch(nodeType) {
        case ARGUMENTS_TRAILER: {
          // object is not callable
          if (!(currentObject instanceof MethodInstance || currentObject instanceof Class)) {
            errorHandler.flag(child, CALL_UNCALLABLE_OBJECT, this);
            throw new NotCallableException();
          }

          if (currentObject instanceof MethodInstance) {
            MethodInstance method = (MethodInstance) currentObject;
            ICodeNode argumentsNode = child.getChildren().get(0);
            ArrayList<Object> arguments = executeArguList(argumentsNode, environment);
            currentObject = method.callMethod(arguments);
            currentEnv = ((Base) currentObject).getFields();
          } else {
            Class cls = (Class)currentObject;
            currentObject = new Instance(cls, new Dictionary(), cls.getFields(), currentEnv);
            currentEnv = ((Instance) currentObject).getFields();
          }
          break;
        }

        case SUBSCRIPT_TRAILER: {
          break;
        }

        case FIELD_TRAILER: {
          String fieldName = (String) child.getAttribute(FIELD_NAME);
          currentObject = ((Base) currentObject).readAttribute(fieldName);

          if (currentObject == null && i == children.size() - 1) {
            currentObject = new Instance(TMPTYPE, new Dictionary(), currentEnv, currentEnv);
            ((Base) currentObject).writeAttr("__name__", fieldName);
            currentEnv.put(fieldName, currentObject);
          }

          if (currentObject == null && i != children.size() - 1) {
            errorHandler.flag(currentNode, UNDEFINED_NAME, this);
          }

          break;
        }

        default: {
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
          identifier = new Instance(TMPTYPE, new Dictionary(), environment, environment);
        }
        ((Base)identifier).writeAttr("__name__", node.getAttribute(IDENTIFIER_NAME));
        return identifier;
      }

      case STRING_LITERAL_OPERAND:
      case FLOAT_CONSTANT_OPERAND:
      case INTEGER_CONSTANT_OPERAND:
      case BOOLEAN_CONSTANT_OPERAND: {
        // create constant instance
        return createConstantInstance(node.getAttribute(VALUE), environment, environment);
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