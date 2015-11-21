package frontend.parsers;

import static frontend.TokenType.*;
import static intermediate.ICodeKey.*;
import static intermediate.ICodeNodeType.*;
import static frontend.ErrorCode.UNEXPECTED_TOKEN;
import static frontend.ErrorCode.MISSING_RIGHT_PAREN;
import static frontend.ErrorCode.MISSING_END_OF_LINE;

import frontend.Token;
import frontend.Parser;
import frontend.TokenType;
import intermediate.ICodeNode;
import intermediate.ICodeFactory;
import intermediate.ICodeNodeType;

import java.util.EnumSet;
import java.util.HashMap;

/**
 * <h1>ExpressionParser</h1>
 * <p>
 * <p>Parse a Clang expression.</p>
 */
public class ExprStmtParser extends StatementParser {
  /**
   * @param parent the parent parser.
   */
  public ExprStmtParser(Parser parent) {
    super(parent);
  }

  /**
   * Parse an expression.
   *
   * @param token the initial token.
   * @return the root node of the generated parse tree.
   * @throws Exception if an error occurred.
   */
  @Override
  public ICodeNode parse(Token token) throws Exception {
    ICodeNode expressionNode = parseTest(token);

    if (currentToken().getType() == ASSIGN) {
      ICodeNode assignExpressionNode = ICodeFactory.createICodeNode(ASSIGN_EXP_NODE);

      token = match(ASSIGN);  // consume ASSIGN
      assignExpressionNode.addChild(expressionNode);
      assignExpressionNode.addChild(parseTest(token));
      expressionNode = assignExpressionNode;
    } else {
      ICodeNode exprNode = ICodeFactory.createICodeNode(EXPRESSION_NODE);
      exprNode.addChild(expressionNode);
      expressionNode = exprNode;
    }

    if (currentToken().getType() == END_OF_LINE) {
      match(END_OF_LINE); // consume END_OF_LINE token
    } else {
      nextToken();  // TODO
      errorHandler.flag(token, MISSING_END_OF_LINE, this);
    }
    return expressionNode; // return expressionNode with expression statement flag
  }

  /**
   * Parse expression.
   * @param token the token used to start to parse.
   * @return intermediate code node of expression.
   * @throws Exception if an error occurred.
   */
  public ICodeNode parseExpr(Token token) throws Exception {
    ICodeNode exprNode = ICodeFactory.createICodeNode(EXPRESSION_NODE);

    exprNode.addChild(parseTest(token));
    return exprNode;
  }

  /**
   * Parse test.
   *
   * @param token the token used to parse.
   * @return the intermediate code node of test expression.
   * @throws Exception if an error occurred.
   */
  public ICodeNode parseTest(Token token) throws Exception {
    return parseOrTest(token);
  }

  /**
   * Parse or test expression.
   *
   * @param token the token to start parse.
   * @return the intermediate code node of or test expression.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseOrTest(Token token) throws Exception {
    ICodeNode andTestExpNode = parseAndTest(token);

    if (currentToken().getType() == LOGICAL_OR) {
      ICodeNode orTestExpNode = ICodeFactory.createICodeNode(LOGICAL_OR_OP);

      orTestExpNode.addChild(andTestExpNode);
      while (currentToken().getType() == LOGICAL_OR) {
        token = match(LOGICAL_OR);  // consume LOGICAL_OR
        andTestExpNode = parseAndTest(token);
        orTestExpNode.addChild(andTestExpNode);
      }
      return orTestExpNode;
    } else {
      return andTestExpNode;
    }
  }

  /**
   * Parse and test expression.
   *
   * @param token the token to start parse.
   * @return the intermediate code node of and test expression.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseAndTest(Token token) throws Exception {
    ICodeNode notTestExpNode = parseNotTest(token);

    if (currentToken().getType() == LOGICAL_AND) {
      ICodeNode andTestExpNode = ICodeFactory.createICodeNode(LOGICAL_AND_OP);

      andTestExpNode.addChild(notTestExpNode);
      while (currentToken().getType() == LOGICAL_AND) {
        token = match(LOGICAL_AND);  // consume LOGICAL_AND
        notTestExpNode = parseNotTest(token);
        andTestExpNode.addChild(notTestExpNode);
      }
      return andTestExpNode;
    } else {
      return notTestExpNode;
    }
  }

  /**
   * Parse not test expression.
   *
   * @param token the token to start parse.
   * @return the intermediate code node of not test expression.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseNotTest(Token token) throws Exception {
    if (token.getType() == LOGICAL_NOT) {
      ICodeNode notTestExpNode = ICodeFactory.createICodeNode(LOGICAL_NOT_OP);

      token = match(LOGICAL_NOT);  // consume LOGICAL NOT
      notTestExpNode.addChild(parseNotTest(token));
      return notTestExpNode;
    } else {
      return parseComparisonExpression(token);
    }
  }

  // Set of relational operators.
  private static final EnumSet<TokenType> REL_OPS = EnumSet.of(EQUAL, NOT_EQUAL, LESS_THAN, LESS_EQUAL, GREAT_THAN, GREAT_EQUAL);

  // Map relational operator tokens to node types.
  private static final HashMap<TokenType, ICodeNodeType> REL_OPS_MAP = new HashMap<>();

  static {
    REL_OPS_MAP.put(EQUAL, EQ_OP);
    REL_OPS_MAP.put(NOT_EQUAL, NE_OP);
    REL_OPS_MAP.put(LESS_THAN, LT_OP);
    REL_OPS_MAP.put(LESS_EQUAL, LE_OP);
    REL_OPS_MAP.put(GREAT_THAN, GT_OP);
    REL_OPS_MAP.put(GREAT_EQUAL, GE_OP);
  }

  /**
   * Parse an comparison expression.
   *
   * @param token the token used to start parse.
   * @return the root of the generated parse subtree.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseComparisonExpression(Token token) throws Exception {
    ICodeNode rootNode = parseExpression(token);

    TokenType tokenType = currentToken().getType();
    // Look for a relational operator.
    if (REL_OPS.contains(tokenType)) {

      // Create a new operator node and adopt the current tree as its first child.
      ICodeNodeType nodeType = REL_OPS_MAP.get(tokenType);
      ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
      opNode.addChild(rootNode);

      token = nextToken();  // consume the operator
      // Parse the second simple expression.  The operator node adopts
      // the simple expression's tree as its second child.
      opNode.addChild(parseExpression(token));

      // The operator node becomes the new root node.
      rootNode = opNode;
    }
    return rootNode;
  }

  /**
   * parse expression.
   *
   * @param token the token used to start parse.
   * @return the intermediate code node of expression.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseExpression(Token token) throws Exception {
    return parseArithExpression(token);
  }


  // Set of additive operators.
  private static final EnumSet<TokenType> ADD_OPS = EnumSet.of(ADD, SUB);

  // Map additive operator tokens to node types.
  private static final HashMap<TokenType, ICodeNodeType> ADD_OPS_OPS_MAP = new HashMap<>();

  static {
    ADD_OPS_OPS_MAP.put(ADD, ADD_OP);
    ADD_OPS_OPS_MAP.put(SUB, SUB_OP);
  }

  /**
   * Parse a arithmetic expression.
   *
   * @param token the token used to parse.
   * @return the root of the generated parse subtree.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseArithExpression(Token token) throws Exception {
    // Parse a term and make the root of its tree the root node.
    ICodeNode rootNode = parseTerm(token);

    TokenType tokenType = currentToken().getType();
    // Loop over additive operators.
    while (ADD_OPS.contains(tokenType)) {

      // Create a new operator node and adopt the current tree
      // as its first child.
      ICodeNodeType nodeType = ADD_OPS_OPS_MAP.get(tokenType);
      ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
      opNode.addChild(rootNode);

      token = nextToken();  // consume the operator

      // Parse another term.
      // The operator node adopts the term's tree as its second child.
      opNode.addChild(parseTerm(token));

      // The operator node becomes the new root node.
      rootNode = opNode;

      token = currentToken();
      tokenType = token.getType();
    }

    return rootNode;
  }

  // Set of multiplicative operators.
  private static final EnumSet<TokenType> MULT_OPS = EnumSet.of(MUL, FLOAT_DIV, INTEGER_DIV, MOD);

  // Map multiplicative operator tokens to node types.
  private static final HashMap<TokenType, ICodeNodeType> MULT_OPS_OPS_MAP = new HashMap<>();

  static {
    MULT_OPS_OPS_MAP.put(MUL, MULTIPLY_OP);
    MULT_OPS_OPS_MAP.put(FLOAT_DIV, FLOAT_DIVIDE_OP);
    MULT_OPS_OPS_MAP.put(INTEGER_DIV, INTEGER_DIVIDE_OP);
    MULT_OPS_OPS_MAP.put(MOD, MOD_OP);
  }

  /**
   * Parse a term.
   *
   * @param token the initial token.
   * @return the root of the generated parse subtree.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseTerm(Token token) throws Exception {
    // Parse a factor and make its node the root node.
    ICodeNode rootNode = parseFactor(token);

    TokenType tokenType = currentToken().getType();
    // Loop over multiplicative operators.
    while (MULT_OPS.contains(tokenType)) {
      // Create a new operator node and adopt the current tree
      // as its first child.
      ICodeNodeType nodeType = MULT_OPS_OPS_MAP.get(tokenType);
      ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
      opNode.addChild(rootNode);

      token = nextToken();  // consume the operator

      // Parse another factor.  The operator node adopts
      // the term's tree as its second child.
      opNode.addChild(parseFactor(token));

      // The operator node becomes the new root node.
      rootNode = opNode;

      token = currentToken();
      tokenType = token.getType();
    }
    return rootNode;
  }

  /**
   * Parse a factor.
   *
   * @param token the initial token.
   * @return the root of the generated parse subtree.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseFactor(Token token) throws Exception {
    ICodeNode factorNode;

    if (token.getType() == ADD || token.getType() == SUB) {
      if (token.getType() == SUB) {
        factorNode = ICodeFactory.createICodeNode(NEGATE_OP);

        token = match(SUB);  // consume SUB
        factorNode.addChild(parseFactor(token));
      } else {
        token = match(ADD);  // consume ADD
        factorNode = parseFactor(token);
      }
    } else {
      factorNode = parsePower(token);
    }

    return factorNode;
  }

  /**
   * Parse a power.
   *
   * @param token the token used to get scanner to start to parse.
   * @return intermediate code node of power expression.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parsePower(Token token) throws Exception {
    ICodeNode rootNode = parseAtomExpr(token);

    if (currentToken().getType() == POWER) {
      ICodeNode opNode = ICodeFactory.createICodeNode(POWER_OP);

      opNode.addChild(rootNode);
      token = match(POWER);  // consume POWER
      opNode.addChild(parseAtomExpr(token));

      rootNode = opNode;
    }
    return rootNode;
  }

  /**
   * Parse a atom expression.
   *
   * @param token the token to start to parse.
   * @return the intermediate code node of atom expression.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseAtomExpr(Token token) throws Exception {
    ICodeNode rootNode = ICodeFactory.createICodeNode(ATOM_EXPR_NODE);

    rootNode.addChild(parseAtom(token));
    while ((token = currentToken()).getType() == LEFT_PAREN || token.getType() == DOT
        || token.getType() == LEFT_BRACKET) {
      rootNode.addChild(parseTrailer(token));
    }
    return rootNode;
  }

  /**
   * Parse a atom.
   *
   * @param token the token to start to parse.
   * @return the intermediate code node of atom.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseAtom(Token token) throws Exception {
    ICodeNode rootNode = null;

    switch (token.getType()) {
      case LEFT_PAREN: {
        token = match(LEFT_PAREN);  // consume the (

        // Parse an expression and make its node the root node.
        rootNode = parseTest(token);

        // Look for the matching ) token.
        if (currentToken().getType() == RIGHT_PAREN) {
          match(RIGHT_PAREN);  // consume the )
        } else {
          errorHandler.flag(token, MISSING_RIGHT_PAREN, this);
        }
        break;
      }

      case IDENTIFIER: {
        String name = token.getText();

        rootNode = ICodeFactory.createICodeNode(IDENTIFIER_OPERAND);
        rootNode.setAttribute(IDENTIFIER_NAME, name);

        match(IDENTIFIER);  // consume the identifier
        break;
      }

      case INTEGER_CONSTANT: {
        // Create an INTEGER_CONSTANT node as the root node.
        rootNode = ICodeFactory.createICodeNode(INTEGER_CONSTANT_OPERAND);
        rootNode.setAttribute(VALUE, token.getValue());

        match(INTEGER_CONSTANT);  // consume the number
        break;
      }

      case FLOAT_CONSTANT: {
        // Create an FLOAT_CONSTANT node as the root node.
        rootNode = ICodeFactory.createICodeNode(FLOAT_CONSTANT_OPERAND);
        rootNode.setAttribute(VALUE, token.getValue());

        match(FLOAT_CONSTANT);  // consume the float number
        break;
      }

      case STRING_LITERAL: {
        String value = (String)token.getValue();

        // Create a STRING_CONSTANT node as the root node.
        rootNode = ICodeFactory.createICodeNode(STRING_LITERAL_OPERAND);
        rootNode.setAttribute(VALUE, value);

        match(STRING_LITERAL);  // consume the string
        break;
      }

      case TRUE:
      case FALSE: {
        rootNode = ICodeFactory.createICodeNode(BOOLEAN_CONSTANT_OPERAND);

        if (token.getType() == TRUE) {
          rootNode.setAttribute(VALUE, true);
        } else {
          rootNode.setAttribute(VALUE, false);
        }
        nextToken();  // consume true or false
        break;
      }

      case NONE: {
        rootNode = ICodeFactory.createICodeNode(NONE_OPERAND);
        match(NONE);
        break;
      }

      default: {
        errorHandler.flag(token, UNEXPECTED_TOKEN, this);
        break;
      }
    }

    return rootNode;
  }

  /**
   * Parse trailer.
   *
   * @param token the token to start to parse.
   * @return the intermediate code node of trailer.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseTrailer(Token token) throws Exception {
    if (token.getType() == LEFT_PAREN) {
      ICodeNode argsTrailerNode = ICodeFactory.createICodeNode(ARGUMENTS_TRAILER);

      token = match(LEFT_PAREN);  // consume LEFT_PAREN
      if (token.getType() != RIGHT_PAREN) {
        argsTrailerNode.addChild(parseArguList(token));
      }
      match(RIGHT_PAREN);  // consume RIGHT_PAREN

      return argsTrailerNode;
    } else if (token.getType() == LEFT_BRACKET) {
      ICodeNode subscriptTrailerNode = ICodeFactory.createICodeNode(SUBSCRIPT_TRAILER);

      token = match(LEFT_BRACKET);  // consume left bracket
      if (token.getType() != RIGHT_BRACKET) {
        subscriptTrailerNode.addChild(parseSubscriptList(token));
      }
      match(RIGHT_BRACKET);  // consume RIGHT_BRACKET

      return subscriptTrailerNode;
    } else if (token.getType() == DOT) {
      ICodeNode fieldTrailerNode = ICodeFactory.createICodeNode(FIELD_TRAILER);

      token = match(DOT);  // consume DOT
      fieldTrailerNode.setAttribute(FIELD_NAME, token.getText());
      match(IDENTIFIER);  // consume field name
      return fieldTrailerNode;
    } else {
      errorHandler.flag(token, UNEXPECTED_TOKEN, this);
      return ICodeFactory.createICodeNode(ERROR_NODE);
    }
  }

  /**
   * parse argument list of function call.
   *
   * @param token the token used to get scanner to start parse.
   * @return intermediate code node of argument list.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseArguList(Token token) throws Exception {
    ICodeNode arguListNode = ICodeFactory.createICodeNode(ARGUMENTS_LIST_NODE);

    arguListNode.addChild(parseArgument(token));
    while (currentToken().getType() == COMMA) {
      token = match(COMMA);  // consume COMMA
      arguListNode.addChild(parseArgument(token));
    }
    return arguListNode;
  }

  /**
   * parse an argument of function call.
   *
   * @param token the token to start parse.
   * @return intermediate code node of argument.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseArgument(Token token) throws Exception {
    ICodeNode argumentNode = ICodeFactory.createICodeNode(ARGUMENTS_NODE);

    argumentNode.addChild(parseTest(token));
    if (currentToken().getType() == ASSIGN) {
      argumentNode.addChild(parseTest(token));
    }
    return argumentNode;
  }

  /**
   * Parse subscript list.
   *
   * @param token the token to start to parse.
   * @return the intermediate code node of subscript list.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseSubscriptList(Token token) throws Exception {
    ICodeNode subscriptListNode = ICodeFactory.createICodeNode(SUBSCRIPT_LIST_NODE);

    subscriptListNode.addChild(parseSubscript(token));
    return subscriptListNode;
  }

  /**
   * Parse subscript.
   *
   * @param token the token to get start to parse.
   * @return the intermediate code node of subscript.
   * @throws Exception if an error occurred.
   */
  private ICodeNode parseSubscript(Token token) throws Exception {
    ICodeNode subscriptNode = ICodeFactory.createICodeNode(SUBSCRIPT_NODE);

    subscriptNode.addChild(parseTest(token));
    return subscriptNode;
  }
}