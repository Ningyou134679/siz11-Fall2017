import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;

public class PairwiseTest {
  /**
  *test if the coveringArray method gives the right array for a given number of arguments
  */
  @Test 
  public void testCoveringArray() {
    Pairwise pairwise = Mockito.mock(Pairwise.class);
    int[] intArray = {0, 1, 2, 3};
    int[] coveringArray = pairwise.coveringArray(2);
    assertArrayEquals(intArray, coveringArray);
  }

  /**
  *test if the formatOutput method gives the right formatted output
  */
  @Test
  public void testFormatOutput() {
    Pairwise pairwise = Mockito.mock(Pairwise.class);
    String[] titles = {"cat","dog"};
    int[] ints = {0,1};
    assertEquals(pairwise.formatOutput(titles, ints), "cat\tdog\t\n0\t0\t\n0\t1\t\n");
  }

  /**
  *test if the getNthBit method gives the right bit for the index
  */
  @Test
  public void testGetNthBit() {
    Pairwise pairwise = Mockito.mock(Pairwise.class);
    assertEquals(pairwise.getNthBit(5,2),1);
  }

  /**
  *test if the redundantTest method does get rid of a redundant test case,
  *given the same bits in two indexes in two numbers
  */
  @Test
  public void testRedundantTest() {
    Pairwise pairwise = Mockito.mock(Pairwise.class);
    ArrayList<Integer> intList = new ArrayList<Integer>();
    intList.add(0);
    assertTrue(pairwise.redundantTest(0, 1, 4, intList));
  }

  /**
  *test if the truncateString method truncates a string to a given range
  */
  @Test
  public void testTruncateString() {
    Pairwise pairwise = Mockito.mock(Pairwise.class);
    assertEquals(pairwise.truncateString("12345678910",10) ,"1234567891");
  }

  /**
  *test if the insertToList method inserts proper test cases to the covering array
  */
  @Test
  public void testInsertToList() {
    Pairwise pairwise = Mockito.mock(Pairwise.class);
    ArrayList<Integer> intList = new ArrayList<Integer>();
    pairwise.insertToList(1, 0, 2, 0, intList);
    boolean testPassed = true;
    int test = 0;
    for (int ints : intList) {
      if (ints != test) {
        testPassed = false;
      }
      test++;
    }
    assertTrue(testPassed);
  }

  /**
  *given insufficient arguments (less than 2)
  *test if the output method returns proper output 
  */
  @Test
  public void testInsufficientParmeter() {
    Pairwise pairwise = Mockito.mock(Pairwise.class);
    String[] args = {"cat"};
    assertEquals(pairwise.output(args) ,"Please enter at least two parameters!");
  }

  /**
  *test if the intListToArray method returns the right array containing all elements in the list
  */
  @Test
  public void testIntListToArray() {
    Pairwise pairwise = Mockito.mock(Pairwise.class);
    int[] intArray = {1, 2};
    ArrayList<Integer> intList = new ArrayList<Integer>();
    intList.add(1);
    intList.add(2);
    assertArrayEquals(pairwise.intListToArray(intList),intArray);
  }

  /**
  *given an array of arguments
  *test if the output method returns a properly displayed covering array
  */
  @Test
  public void testOutput() {
    Pairwise pairwise = Mockito.mock(Pairwise.class);
    String[] args = {"cat","dog"};
    assertEquals(pairwise.output(args), "cat\tdog\t\n0\t0\t\n0\t1\t\n1\t0\t\n1\t1\t\n");
  }

}