package intermediate;

/**
 * <h1>ICodeNodeType</h1>
 *
 * <p>Node types of the intermediate code parse tree.</p>
 */
public enum ICodeNodeType {

  // Statements
  EMPTY_STATEMENT, // empty statement
  EXPRESSION_STATEMENT, EXPRESSION_NODE, COMPOUND_STATEMENT,
  IF_STATEMENT, IF_BRANCH, ELIF_BRANCH, ELSE_BRANCH,// selection_branch used in if statement to represent a branch
  WHILE_STATEMENT,
  FUNCTION_DEFINE_STATEMENT, FUNCTION_NAME, PARAMETERS, FUNCTION_BODY, // function define
  FUNCTION_CALL_STATEMENT, RETURN_STATEMENT,

  // statements for class
  CLASS_DEFINE_STATEMENT, BASE_CLASSES_LIST, CLASS_DEFINE_BODY,

  // expression
  ASSIGN_EXP_NODE,

  // node for variable
  ATOM_EXPR_NODE,

  // the negative operator in variable or constant
  NEGATE_OP,

  // Relational operators
  GT_OP, GE_OP, LT_OP, LE_OP, EQ_OP, NE_OP,

  // Additive operators
  ADD_OP, SUB_OP, POWER_OP,

  // Logical operators
  TEST_NODE, LOGICAL_AND_OP, LOGICAL_OR_OP, LOGICAL_NOT_OP,
  COMAPRISON_NODE,

  // Multiplicative operators
  MULTIPLY_OP, INTEGER_DIVIDE_OP, FLOAT_DIVIDE_OP, MOD_OP,

  // Operands
  IDENTIFIER_OPERAND,
  INTEGER_CONSTANT_OPERAND,
  FLOAT_CONSTANT_OPERAND,
  STRING_LITERAL_OPERAND,
  BOOLEAN_CONSTANT_OPERAND,
  NONE_OPERAND,

  // function call
  ARGUMENTS_TRAILER, SUBSCRIPT_TRAILER, FIELD_TRAILER,
  // arguments of function
  ARGUMENTS_NODE, ARGUMENTS_LIST_NODE,

  // subscript
  SUBSCRIPT_LIST_NODE, SUBSCRIPT_NODE,

  // code node for error handle
  ERROR_NODE,
}
