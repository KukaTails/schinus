package interpreter;

import static intermediate.ICodeNodeType.EXPRESSION_NODE;
import static objectmodel.predefined.PredefinedType.FLOAT;
import static objectmodel.predefined.PredefinedType.STRING;
import static objectmodel.predefined.PredefinedType.INTEGER;
import static objectmodel.predefined.PredefinedType.BOOLEAN;
import static objectmodel.predefined.PredefinedConstant.NONE;
import static objectmodel.predefined.PredefinedConstant.NO_PRINT;

import message.Message;
import message.MessageHandler;
import message.MessageListener;
import intermediate.ICodeNode;
import objectmodel.baseclasses.Instance;
import objectmodel.dictionary.Dictionary;
import interpreter.executors.StmtExecutor;

import java.util.ArrayList;

/**
 * <h1>Executor</h1>
 * <p>
 * <p>The executor for an interpreter back end.</p>
 */
public class Executor {
  protected static MessageHandler messageHandler;          // message handler delegate
  protected static int executionCount;                     // count of execution statements
  protected static RuntimeErrorHandler errorHandler;
  protected static ArrayList<Instance> constantInstance;
  protected static int constantId = 0;

  static {
    executionCount = 0;
    errorHandler = new RuntimeErrorHandler();
    messageHandler = new MessageHandler();
    constantInstance = new ArrayList<>();
  }

  public Executor() {
  }

  public RuntimeErrorHandler getErrorHandler() {
    return errorHandler;
  }

  /**
   * Execute the source program by processing the intermediate code and the environment.
   * @param iCodeNode   the intermediate code.
   * @param environment the intermediate code executed in the environment.
   */
  public void process(ICodeNode iCodeNode, Dictionary environment) {
    StmtExecutor stmtExecutor = new StmtExecutor();

    try {
      Object result = stmtExecutor.execute(iCodeNode, environment);
      if (iCodeNode.getType() == EXPRESSION_NODE) {
        printObject(result);
      }
    } catch(Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Print the value of object.
   * @param object the object will be printed.
   */
  public void printObject(Object object) {
    ExecuteResultPrinter executeResultPrinter = new ExecuteResultPrinter();

    executeResultPrinter.printObject(object);
    if (object != NO_PRINT) {
      System.out.println();
    }
  }

  /**
   * Create constant instance in environment.
   * @param value       the value of instance.
   * @param environment the environment which contains the instance.
   * @return the instance created.
   */
  public Instance createConstantInstance(Object value, Dictionary environment, Dictionary existedEnv) {
    Instance instance = NONE;
    int id = constantId++;

    if (value instanceof String) {
      instance = new Instance(STRING, new Dictionary(), environment, existedEnv);
    } else if (value instanceof Integer) {
      instance = new Instance(INTEGER, new Dictionary(), environment, existedEnv);
    } else if (value instanceof Float || value instanceof Double) {
      instance = new Instance(FLOAT, new Dictionary(), environment, existedEnv);
    } else if (value instanceof Boolean) {
      instance = new Instance(BOOLEAN, new Dictionary(), environment, existedEnv);
    }
    instance.setExistedEnv(existedEnv);
    instance.writeAttr("__value__", value);
    instance.writeAttr("__name__", "constant" + id);
    constantInstance.add(instance);

    return instance;
  }

  /**
   * @return the message handler.
   */
  public MessageHandler getMessageHandler() {
    return messageHandler;
  }

  /**
   * @param message the message to set.
   */
  public void sendMessage(Message message) {
    messageHandler.sendMessage(message);
  }

  /**
   * @param listener the message listener to add.
   */
  public void addMessageListener(MessageListener listener) {
    messageHandler.addListener(listener);
  }

  /**
   * @param listener the message listener to remove.
   */
  public void removeMessageListener(MessageListener listener) {
    messageHandler.removeListener(listener);
  }
}