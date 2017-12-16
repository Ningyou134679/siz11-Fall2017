import java.util.*;

public class Pairwise {
  public static void main(String[] args) {
    System.out.println(output(args));
  }

  /**
  *Given a number of arguments, generate a covering array
  *@param numArgs - number of argument
  *@return an int array that covers all the test cases
  */
  public static int[] coveringArray(int numArgs) {
    //create an array list, for each pair of test cases, add an unredundant test to the list
    ArrayList<Integer> coveringArrayList = new ArrayList<Integer>();
    insertToList(numArgs - 2, numArgs - 1, numArgs, 0, coveringArrayList);
    //sort the list, convert the list into an array, return the array
    Collections.sort(coveringArrayList);
    return intListToArray(coveringArrayList);
  }

  /**
  *Given an array of titles/arguments, and an int array, order them in a specific order to output to screen
  *@param titles - an array of arguments
  *@param coveringArray - an array of ints
  *@return the formatted string containing the arguments and the int elements
  */
  public static String formatOutput(String[] titles, int[] coveringArray) {
    if (titles == null || coveringArray == null) {
      return null;
    }
    StringBuilder builder = new StringBuilder();
    //for each argument, put a tab between them, and put them in a single line
    for (String title : titles) {
      builder.append(truncateString(title,10) + "\t");
    }
    builder.append("\n");
    //for each int, convert them to binary, put a tab between every bit, and put all bits in a single line
    for (int row : coveringArray) {
      int bitOffset = titles.length - 1;
      for (int bitIndex = 0; bitIndex < titles.length; bitIndex++, bitOffset--) {
        int bit = getNthBit(row, bitOffset);
        builder.append(bit + "\t");
      }
      builder.append("\n");
    }
    return builder.toString();
  }

  /**
  *Given an int, and an index, get the bit value of the int at that index
  *index counts from right to left, starts from 0 
  *(ex. 6 will be 110, 1 at index 2, 1 at index 1, and 0 at index 0)
  *@param num - an integer
  *@param n - a position, or index
  *@return the single bit at that index
  */
  public static int getNthBit(int num, int n) {
    return (num >> n) & 1;
  }

  /**
  *Given two bit positions, which is a pair of argument to test, number of arguments, the test case, a list
  *determine which tests are to be included to the covering array, by testing every pair of argument/indexes
  *for example, given 3 arguments, "a", "b", "c", in the index order ot 2, 1, 0
  *I want to test 010, on "a", "b", then bit1 would be 1 for "b", bit2 will be 2 for "a"
  *it will recursively go on testing every index pair for the test, test + 1, test + 2 ... 2^(numArgs) - 1
  *@param bit1 - the first bit location, one of the two arguments in a pair
  *@param bit2 - the second bit location, one of the two arguments in a pair, should always come after bit1
  *@param numArgs - number of arguments, used to determine the max number of test cases, defined by 2^(numArgs)-1
  *@param test - the row of test case, for example 001, when numArgs is 3
  *@param coveringArrayList - the list to store the covering array for test
  */
  public static void insertToList(int bit1, int bit2, int numArgs, int test, ArrayList<Integer> coveringArrayList) {
    //System.out.println("Pairwise.insertToList : " + bit1 + " " + bit2 + " at test " + Integer.toBinaryString(test));
    if (!coveringArrayList.contains(Integer.valueOf(test))) {
       if (!redundantTest(bit1, bit2, test, coveringArrayList)) {
          //System.out.println("Pairwise.insertToList : add test " + Integer.toBinaryString(test));
          coveringArrayList.add(test);
       }
    }

    if (bit1 > 0) {
      insertToList(bit1 - 1, bit2, numArgs, test, coveringArrayList);
      if (bit1 == (bit2 - 1)) {
        insertToList(bit1 - 1, bit2 - 1, numArgs, test, coveringArrayList);
      }
    }
    if (test < (Math.pow(2, numArgs) - 1) && (bit2 == numArgs - 1) && (bit1 == bit2 - 1)) {
      insertToList(bit1, bit2, numArgs, test + 1, coveringArrayList);
    }
    
    
  }

  /**
  *converts a list of integers to an array
  *@param integers - an ArrayList <\Integer>
  *@return an int array
  */
  public static int[] intListToArray(ArrayList<Integer> integers) {
      if (integers == null) {
        return new int[0];
      }
      int[] ret = new int[integers.size()];
      for (int i = 0; i < ret.length; i++) {
          ret[i] = integers.get(i).intValue();
      }
      return ret;
  }

  /**
  *Given an array of arguments, compute a covering array, format them in a order and return to display
  *@param args - an array of arguments
  *@return the formatted string to be displayed
  */
  public static String output(String[] args) {
    if (args == null) {
      return null;
    }
    if (args.length < 2) {
      return "Please enter at least two parameters!";
    }
    int[] coveringArray = coveringArray(args.length);
    
    return formatOutput(args, coveringArray);
  }
  /**
  *Given two bit indexes, determine if this pair already has a corresponding test case in the covering array
  *index comes right to left, starting from 0, 
  *for example, 110 would be [2]1, [1]1, [0]0
  *for example, bit1 is 0 and bit2 is 2, 
  *then 110 and 100 are redundant, because index 0 and index 2 contain the same bits
  *@param bit1 - the first bit location, one of the two arguments in a pair
  *@param bit2 - the second bit location, one of the two arguments in a pair, should always come after bit1
  *@param test - the row of test case, for example 001, when numArgs is 3
  *@param coveringArrayList - the list to store the covering array for test
  */
  
  public static boolean redundantTest(int bit1, int bit2, int test, ArrayList<Integer> coveringArrayList) {
    for (Integer tested : coveringArrayList) {
      if (getNthBit(tested, bit1) == getNthBit(test, bit1) && getNthBit(tested ,bit2) == getNthBit(test, bit2)) {
        //System.out.println("Pairwise.redundantTest : " + Integer.toBinaryString(tested) 
        //+ " = " + Integer.toBinaryString(test) + " for [" + bit1 + "] and [" + bit2 + "]");
        return true;
      }
    }
    return false;
  } 

  /**
  *Given a string and a range, truncate the string to a given range
  *for example, at a range of 10, "12345678910" would return "1234567891"
  *@param inputString - a string to be truncated
  *@param range - the range, starting from 0
  *@return,null if input is null, empty string if range is invalid, or the truncated string 
  */
  public static String truncateString(String inputString, int range) {
    if (inputString == null) {
      return null;
    } else if (range < 0) {
      return "";
    } else if (inputString.length() <= range) {
      return inputString;
    }
    return inputString.substring(0, range);
  }
}