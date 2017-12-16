import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;

public class RPNTest {

  //rpn.analysis(String) is an attempt for a universal method
  //that takes in any line from a file or user input
  //and generate an appropriate output, or error 
  //line number or exiting the program is not covered

  /**
  *A general test attempting to cover any possible input from the user except quit
  *To check if the code breaks somewhere, and if the right output is generated
  */
  @Test
  public void testCustom1() {
    RPN rpn = new RPN();
    rpn.setup();
    rpn.analysis(" let x 843274529387504832978067058760.437598243572403925484507   ");
    rpn.analysis("");
    rpn.analysis(" LeT Y 9 ");
    rpn.analysis(" ");
    rpn.analysis("leT x     y ");
    rpn.analysis("    leT    ");
    rpn.analysis(" LeT    x    x  1   - ");
    rpn.analysis(" LeT    x    lET d   47842975823439443532533232328.452344532327 y /");
    rpn.analysis("  Let Z 10 ");
    rpn.analysis("\n");
    rpn.analysis("&$^%^&$%&%^$ç∂˙ˆ¨∂˙çåˆ");
    rpn.analysis("     leT   a       6    ");
    assertEquals(rpn.analysis(" prInT     x   y   z   +   -   A  *   "), "-66");
  }
  /**
  *Test Requirement 3
  *All numbers shall be arbitrary-precision (i.e., there shall be no integer overflow - 999999999999999999999999999 shall be considered a valid number and stored as such)
  */
  @Test
  public void testBigNumber() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("999999999999999999999999999"),"999999999999999999999999999");
  }

  /**
  *Test Requirement 4
  *Variable names can be a single letter (A-Z) and are case-insensitive (e.g., a and A refer to the same variable).
  */
  @Test
  public void testVariableIgnoreCase() {
    RPN rpn = new RPN();
    rpn.setup();
    rpn.analysis("LET x 8");
    assertEquals(rpn.analysis("X"),"8");
  }

  /**
  *Test Requirement 8
  *The keyword LET is followed by a single-letter variable, then an RPN expression. The RPN expression is evaluated and the value of the variable is set to it.
  */
  @Test
  public void testLet() {
    RPN rpn = new RPN();
    rpn.setup();
    rpn.analysis("LET a 1");
    assertEquals(rpn.analysis("a"),"1");
  }

  /**
  *Test Requirement 9
  *The keyword PRINT is followed by an expression, and the interpreter shall print the result of that expression to standard output (stdout).
  */
  @Test
  public void testPrint() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("PRINT 2"),"2");
  }

  /**
  *Test Requirement 10
  *Keywords shall be case-insensitive (e.g. print, PRINT, or pRiNt are interchangeable)
  */
  @Test
  public void testKeywordIgnoreCase() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("pRiNt 2"),"2");
  }

  /**
  *Test Requirement 11
  *Keywords shall only start a line (e.g., you cannot have a line such as "1 2 + PRINT 3")
  */
  @Test
  public void testKeywordInMiddle() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("1 2 + PRINT 3"),"Could not evaluate expression");
  }

  /**
  *Test Requirement 11
  *Keywords shall only start a line (e.g., you cannot have a line such as "1 2 + PRINT 3")
  */
  @Test
  public void testInvalidKeywordInMiddle() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("1 2 + ^&^&^* 3"),"Could not evaluate expression");
  }

  /**
  *Test Requirement 12
  *Variables shall be considered initialized once they have been LET. It shall be impossible to declare a variable without initializing it to some value.
  */
  @Test
  public void testUninitializedVariable() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("x 2"),"Variable x is not initialized");
  }

  /**
  *Test Requirement 13, 14
  *Referring to a variable which has not previously been LET (i.e. has not been declared) 
  *shall result in the program informing the user that the variable is uninitialized 
  *and QUIT the program with error code 1.
  *It should inform the user in the following format: "Line n: Variable x is not initialized." 
  *where x is the name of the variable and n is the line number of the file the error occurred in.
  */
  @Test
  public void testUninitializedVariable2() {
    RPN rpn = new RPN();
    rpn.setup();
    rpn.analysis("LET x 8");
    assertEquals(rpn.analysis("x y +"),"Variable y is not initialized");
  }

  /**
  *Test Requirement 15
  *If the stack for a given line is empty OR does not contain the correct number of elements on the stack for that operator,
  *and an operator is applied, the program shall inform the user and QUIT the program with error code 2. 
  *It should inform the user in the following format: "Line n: Operator o applied to empty stack" where o is the operator used and n is the line number the error occurred in.
  */
  @Test
  public void testApplyToEmptyStack() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("LET x"),"Operator LET applied to empty stack");
  }

  /**
  *Test Requirement 15
  *If the stack for a given line is empty OR does not contain the correct number of elements on the stack for that operator,
  *and an operator is applied, the program shall inform the user and QUIT the program with error code 2. 
  *It should inform the user in the following format: "Line n: Operator o applied to empty stack" where o is the operator used and n is the line number the error occurred in.
  */
  @Test
  public void testApplyToEmptyStack2() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("LET"),"Operator LET applied to empty stack");
  }

  /**
  *Test Requirement 15
  *If the stack for a given line is empty OR does not contain the correct number of elements on the stack for that operator,
  *and an operator is applied, the program shall inform the user and QUIT the program with error code 2. 
  *It should inform the user in the following format: "Line n: Operator o applied to empty stack" where o is the operator used and n is the line number the error occurred in.
  */
  @Test
  public void testApplyToEmptyStack3() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("PRINT"),"Operator PRINT applied to empty stack");
  }

  /**
  *Test Requirement 17
  *If the stack for a given line contains more than one element and the line has been evaluated,
  *the program shall inform the user and QUIT the program with error code 3. 
  *It should inform the user in the following format: "Line n: y elements in stack after evaluation"
  *where y is the number of elements in the stack and n is the line number the error occurred in.
  */
  @Test
  public void testMultipleElementInStack() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("1 2"),"2 elements in stack after evaluation");
  }

  /**
  *Test Requirement 19
  *If an expression used for initializing a LET variable is invalid, the variable is considered to have not been initialized.
  *For example, "LET A 1 2" is invalid, and A is not initialized.
  */
  @Test
  public void testInvalidLet() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("LET A 1 2"),"2 elements in stack after evaluation");
  }

  /**
  *Test Requirement 20
  *If an invalid keyword is used, the program shall inform the user and QUIT the program with error code 4.
  *It should inform the user in the following format: "Line n: Unknown keyword k" 
  *where k is the keyword and n is the line number the error occurred in.
  */
  @Test
  public void testInvalidKeyword() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("Var"),"Unknown keyword Var");
  }



  @Test
  public void testSingleNumber() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("20"),"20");
  }

  @Test
  public void testSingleNumberWithSpace() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("   20  "),"20");
  }

  @Test
  public void testBigDecOperation() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("999999999999999999 999999999999999999 *"),"999999999999999998000000000000000001");
  }

  @Test
  public void testLetMultipleTuple() {
    RPN rpn = new RPN();
    rpn.setup();
    assertEquals(rpn.analysis("LET a 0 999999999999999999999999999 -"),"-999999999999999999999999999");
  }






}