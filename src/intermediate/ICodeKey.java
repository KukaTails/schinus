package intermediate;

public enum ICodeKey {
  LINE,                // the line number of token which is a node of AST
  VALUE,               // the value of token(constant of integer and float)
  ASSIGNMENT_OPERATOR, // the assignment operator of assignment statement
  IDENTIFIER_NAME,     // the name of identifier
  VARIABLE_FIELD,      // the field of a identifier
  FIELD_NAME,          // the name of field
  CALL_PARAMETERS,     // the parameters of function call
  PARAMETERS_LIST,     // the parameters of function
  FUNCTION_NAME_KEY,       // the name of function which be defined

  CLASS_NAME,
  BASE_CLASSES_NAME,
}
