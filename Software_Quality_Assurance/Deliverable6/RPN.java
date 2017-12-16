import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.io.*;
public class RPN{

  static String[] keywords = {"LET", "PRINT", "QUIT"};
  static String[] errors = {"is not initialized", "applied to empty stack",
  "elements in stack after evaluation", "Could not evaluate expression", "Unknown keyword"};
  static ArrayList<String> keywordsList;
  static ArrayList<String> errorsList;
  HashMap<String, BigDecimal> variables;

  /**
  *Given a string, assume that it contains one of the messages
  *analize the error, exit the program with appropriate exit code
  *@param input, a string that should contain an error message
  */
  public void analizeErrorAndExit(String input) {
    //Requirement 13: if a variable is not initialized, exit with code1
    if (input.contains(errorsList.get(0))) {
      System.exit(1);
    } 
    //Requirement 15: operator applied to empty stack should exit with code2
    if (input.contains(errorsList.get(1))) {
      System.exit(2);
    } 
    //Requirement 17: n numbers in stack after evaluation, exit code 3
    if (input.contains(errorsList.get(2))) {
      System.exit(3);
    } 
    //Requirement 20: unknown keyword, exit code 4
    if (input.contains(errorsList.get(4))) {
      System.exit(4);
    } 
    //Requirement 22: any other error, exit code 5
    System.exit(5);
  }

  /**
  *Given a line, return the result after evaluation, or the error message
  *The one method that handles any line of input
  *Calls let, print, or exit, or just evaluate the expression
  *@param input, any line from a file or user input to be evaluated
  *@return a string of the result, or an error
  */
  public String analysis(String input){
    if (input == null || input.length() < 1 
      || input.equals("") || input.equals("\n") || input.trim().length() < 1) return "";
    String[] tokens = input.split("\\s+");

    //if input starts with empty spaces, need to get rid of them
    if (tokens[0].equalsIgnoreCase("")) {
      tokens = Arrays.copyOfRange(tokens, 1, tokens.length);
      StringBuilder builder = new StringBuilder();
      for(String token : tokens) {
          builder.append(token);
          builder.append(" ");
      }
      input = builder.toString();
    }
    //if the first token is more than one letter
    //meaning it could be a 2+ digits number, a keyword, or other
    if (tokens[0].length() > 1) {
      //Requirement10: if it is a keyword, case-insensitive
      if (keywordsList.contains(tokens[0].toUpperCase())) {
        //call any of the methods associated with the keyword
        if (tokens[0].equalsIgnoreCase("let")) return let(input);
        if (tokens[0].equalsIgnoreCase("print")) return print(input);
        if (tokens[0].equalsIgnoreCase("quit")) {
          System.exit(0);
        } 
      } else if (!tokens[0].matches("[+-]?([0-9]*[.])?[0-9]+")){
        //Here means that first token of a line
        //if not a keyword, not a number, but has a length > 1
        //return unknown keyword
        return errorsList.get(4) + " " + tokens[0];
      }
    } 
    //Here the first token is single letter, or a number
    //evaluate the expression and return
    return evalRPN(input.split("\\s+"));
  }

  /**
  *Given two BigDecimal and an operator, operate BigDecimal1 operator on BigDecimal2
  *ex. BigDecimal(4,2,/) will return in 2.000
  *Note that division has 2 decimal places precision
  *Any other behavior will throw an ArithmeticException
  *@param bigDec1, a BigDecimal number
  *@param bigDec2, another BigDecimal number
  *@param operator, the operator on the two numbers
  *@return BigDecimal number after the operation
  */
  public BigDecimal computeBigDecimal(BigDecimal bigDec1, BigDecimal bigDec2, String operator) throws ArithmeticException {
    
    String operators = "+-*/";
    if (bigDec1 == null || bigDec2 == null 
      || operator == null || operator.length() != 1 || !operators.contains(operator)) {
      throw new ArithmeticException();
    } else {
      int index = operators.indexOf(operator);
      switch (index) {
          case 0:
              return bigDec1.add(bigDec2);
          case 1:
              return bigDec1.subtract(bigDec2);
          case 2:
              return bigDec1.multiply(bigDec2);
          case 3:
              return bigDec1.divide(bigDec2, 2, RoundingMode.HALF_UP);
      }
    }
    return null;
  }

  /**
  *Given tokens of a RPN expression, evaluate the result, returns either the result or the error
  *A RPN expression in this case can only contain the following elements:
  *(1)a number that can be handled by BigDecimal
  *(2)a single alphabetical letter that is already stored as a variable
  *(3)a single operator in either of "+","-","*","/"
  *@param tokens, contains of tokens of a rpn expression (ex. {"1", "2", "+"})
  *@return result if the final stack size is 1, or error in any other case
  */
  public String evalRPN(String[] tokens) {
    String operators = "+-*/";
    //a stack stores either numbers or single letters that is a variable
    Stack<String> stack = new Stack<String>();

    for(String t : tokens){
        //if a token does not consist letter or number or "+-*/", (ex. ^%$#@!)
        //or if a token consists of only white spaces
        //or if a token consists of only alphabetical letters of size greater than 1 (ex. aesijfasi)
        //(note that the fist token is handled in analysis(String) for Unknown keyword)
        //return Could not evaluate expression
        if (!t.matches("[a-zA-Z]*") && !t.matches("[+-]?([0-9]*[.])?[0-9]+") && !operators.contains(t) || t.trim().length() < 1 || t.matches("[a-zA-Z]*") && t.length() > 1) {
          return errorsList.get(3);
        }
        //if a token contains only 1 alphabetical letter, but is not a declared vairable
        //return Variable <varname> is not initialized
        if (t.matches("[a-zA-Z]*") && variables != null && !variables.containsKey(t.toLowerCase())) {
          return "Variable " + t + " " + errorsList.get(0);
        }
        //if a token is not in the above categories
        //meaning that it is a declared variable, or a number, or any of the "+-*/"
        //if it is a declared variable, or a number, push to stack
        if (!operators.contains(t)) {
            try {
              stack.push(stringToBigDecimal(t).toString());
            } catch (NumberFormatException numEx) {
              //NumberFormatException is thrown by stringToBigDecimal(String), 
              //if token is a string that is not a variable and cannot turn into a number
              //ex. BigDecimal("s"), and s is not a variable, will throw a NumberFormatException
              //return "Could not evaluate expression"
              return errorsList.get(3);
            }
            
        } else {       
            try {
              //encountered an operator, pop off num2, pop off num1 operate num1 <operator> num2
              BigDecimal bigDec2 = stringToBigDecimal(stack.pop());
              BigDecimal bigDec1 = stringToBigDecimal(stack.pop());
              stack.push(computeBigDecimal(bigDec1, bigDec2, t).toString());
            } catch (EmptyStackException emptyStackEx) {
              //if a operator is encountered but there are less than 2 elements in the stack
              //ex. "1 *" will result in this exception
              //return "Operator <operator> applied to empty stack"
              return "Operator " + t + " " + errorsList.get(1);
            } catch (NumberFormatException numEx) {
              //NumberFormatException is thrown by stringToBigDecimal(String), 
              //if token is a string that is not a variable and cannot turn into a number
              //ex. BigDecimal("s"), and s is not a variable, will throw a NumberFormatException
              //return "Could not evaluate expression"
              return errorsList.get(3);
            } catch (ArithmeticException arithEx) {
              //ArithmeticException is thrown by computeBigDecimal(BigDecimal, BigDecimal, String)
              //when any error occurs during operating on two BigDecimal number
              //ex. new BigDecimal("1").divide(new Decimal("4")) will throw a ArithmeticException
              //return "Could not evaluate expression"
              return errorsList.get(3);
            } catch (NullPointerException nullEx) {
              //computeBigDecimal(BigDecimal may return null, although logically it is impossible
              return errorsList.get(3);
            }
        }
    }
    //In the end, two behaviors are expectd:
    //1) if there is only 1 element in the stack return the last element in the stack
    //2) if there is more or less than 1 element, return "n elements in stack after evaluation"
    //the second case result if the rpn expression is like "1 1 1", not operated by an operator
    return (stack.size() == 1) ? stack.pop() : (stack.size() + " " + errorsList.get(2));
  }


  /**
  *Given an input, assume that it starts with let, case-insensitive
  *Assign to the second token the rpn expression that follows, if no error is generated
  *@param input, that starts with let, case-insensitive
  *@return output, or error
  */
  public String let(String input) {
    if (input == null || input.length() < 1 
      || input.equals("") || input.equals("\n") || input.trim().length() < 1) return "";
    String[] tokens = input.split("\\s+");
    //if the first token is not "let", case-insensitive, return ""
    if (!tokens[0].equalsIgnoreCase("let")) { return ""; }
    //if there are less or equal to 2 tokens
    //ex. LET x
    //return "Operator LET applied to empty stack"
    if (tokens.length <= 2) { return "Operator LET " + errorsList.get(1);}
    //variable can only be a single letter
    //if it is multiple letters like "LET ab ..."
    //return "Could not evaluate expression"
    if (tokens[1].length() > 1) { return errorsList.get(3);}
    //variable cannot contain non-alphabetical letters
    if (!tokens[1].matches("[a-zA-Z]*")) {return errorsList.get(3);}
    //evaluate the rpn expression to assign to the variable
    String[] rpnToken = Arrays.copyOfRange(tokens, 2, tokens.length);
    String result = evalRPN(rpnToken);
    //if only a number is returned, assign it to the variable
    if (result.matches("[+-]?([0-9]*[.])?[0-9]+")) {
      try {
        //store all variables in lowercase 
        //because the requirement requires case-insensitive variables
        variables.put(tokens[1].toLowerCase(), new BigDecimal(result));
      } catch (NumberFormatException numEx) {
        return errorsList.get(3);
      } catch (NullPointerException nullEx) {
        return errorsList.get(3);
      }
    }
    return result;
  }

  /**
  *Given a string, evaluate it, return the output
  *Called when the first token of the line is print, case-insensitive
  *@param input, a line from the user or the file, starts with print
  *@return output after analizing the string
  */
  public String print(String input) {
    if (input == null || input.length() < 1 
      || input.equals("") || input.equals("\n") || input.trim().length() < 1) return "";
    String[] tokens = input.split("\\s+");
    //if the first token is not "let", case-insensitive, return ""
    if (!tokens[0].equalsIgnoreCase("print")) { return ""; }
    if (tokens.length <= 1) { return "Operator PRINT " + errorsList.get(1);}
    //evaluate the rpn expression and return the result
    String[] rpnToken = Arrays.copyOfRange(tokens, 1, tokens.length);
    return evalRPN(rpnToken);
  }

  /**
  *Initialize list of keywords, errors, variables to be used in the program
  */
  public void setup() {
    keywordsList = new ArrayList<>(Arrays.asList(keywords));
    errorsList = new ArrayList<>(Arrays.asList(errors));
    variables = new HashMap<>();
  }

  /**
  *see if a string contains a expected error
  *ex. "Variable x is not initialized" contains "is not initialized"
  *@param str, the input string
  *@return true if input string contains error, 
  *or false if input is null or does not contain error
  */
  public boolean stringContainsError(String str) {
    if (str == null) {return false;}
    for (String error: errorsList) {
      if (str.contains(error)) {
        return true;
      }
    }
    return false;
  }

  /**
  *Given a string, turn it into a BigDecimal
  *If a string is a declared variable, return the value it associates with
  *Throws NumberFormatException, to be handled in evalRPN
  *@param str, the string to convert to BigDecimal, expected to be a number or variable
  *@return BigDecimal number
  */
  public BigDecimal stringToBigDecimal(String str) throws NumberFormatException {
    if (variables != null && variables.containsKey(str.toLowerCase())) {
      return variables.get(str.toLowerCase());
    } 
    return new BigDecimal(str);
  }
  /**
  *Given a filename and a line number, read and analize each line in the file
  *Is called when command line argument is more than 1
  *@param fileName, the name of the file
  *@param lineNum, the line number before the file is read
  *@return the line number after the file is read
  */
  public int readFile(String fileName, int lineNum) {
    try {
      File file = new File(fileName);
      if (!file.exists()) {
        System.exit(5);
      }

      FileReader fileReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line;
      //read line by line from the file
      while ((line = bufferedReader.readLine()) != null) {
        //Continue onto the next line when an empty line is encountered
        if (line.length() < 1 || line.trim().length() < 1) {
          continue;
        }
        //get the first token from the line
        String firstToken = line.split("\\s+")[0];
        //a line start with white spaces cannot be eliminated by split
        //redirect first token to second token
        if (firstToken.equalsIgnoreCase("")) {
          firstToken = line.split("\\s+")[1];
        }
        //if line does not completely consist of white spaces
        if (line.trim().length() > 0) {
          //Requirement 6&7: if the first token is "quit", quit the program
          if (firstToken.equalsIgnoreCase("quit")) {
            System.exit(0);
          }
          //analize the line, return either the output or the error
          String output = analysis(line);
          //if the output is only a number, then no error was produced
          if (output.matches("[+-]?([0-9]*[.])?[0-9]+")) {
            //Requirement 9: print to stdout only when first token is "print"
            if (firstToken.equalsIgnoreCase("print")) {
              System.out.println(output);
            }
          } else if (output.trim().length() > 0) {
            //here in the program means that a requirement is violated
            //so that an error is returned
            System.err.println("Line " + lineNum + ": " + output);
            analizeErrorAndExit(output);
          }
        }
        lineNum++;
      }
      fileReader.close();
      return lineNum;
    } catch (IOException e) {
      return lineNum;
    } catch (NullPointerException nullEx) {
      return lineNum;
    }
  }
  /**
  *The read-eval-print-loop mode
  *called when there is 0 command line argument
  *Takes a line from a user, analize it, print the output, or error
  */
  public void repl() {
    Scanner reader = new Scanner(System.in);
    String input = "";
    int lineNum = 1;
    while (!input.equalsIgnoreCase("QUIT")) {
      System.out.print("> ");
      input = reader.nextLine();
      if (input.trim().length() > 0) {
        String output = analysis(input);
        if (output.matches("[+-]?([0-9]*[.])?[0-9]+")) {
          //if return value is a number, there is no error
          System.out.println(output);
        } else if (output.trim().length() > 0) {
          //Requirement 18: an error occured, inform user but not exit
          System.err.println("Line " + lineNum + ": " + output);
        }
        lineNum++;
      }
    }
  }
  /**
  *Given an array of arguments
  *if theere is more than 1 argument, enter readFile mode
  *else, enter repl mode
  */
  public void run(String[] args) {
    if (args == null) return;
    if (keywordsList == null || errorsList == null || variables == null) {
      setup();
    }
    if (args.length > 0) {
      int lineNum = 1;
      for (String arg : args) {
        lineNum = readFile(arg, lineNum);
      }
    } else {
      repl();
    }
  }


  public static void main(String args[]) {
    RPN rpn = new RPN();
    rpn.setup();
    rpn.run(args);

    
  }
}