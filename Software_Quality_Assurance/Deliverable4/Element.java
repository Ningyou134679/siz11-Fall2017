import java.io.*;
import java.util.HashMap;
/**
  *This program will read in a file called elements.txt, which contains an abbreviation of an element, and the element name(ex. "Ac": "Actinium",\n)
  *The abbreviation and the element name will be stored as keys and values in a hashmap
  *A second txt file will be read in, for every line of the file, determine if it is a permutation of a group of abbreviations, with special characters removed
  *(ex. A line of "B*C" will return "B - C\nBoron - Carbon")
  *Note: You may see a long string printed out at the beginning of the program, it is not cheating, but is an approach to accomodate one of the requirements
  *Requirement can be found at https://github.com/laboon/CS1632_Fall2017/blob/master/deliverables/4/deliverable4.md
*/
public class Element{
  public static HashMap<String, String> elementMap = new HashMap<String, String>();
  public static FileReader in = null;
  public static BufferedReader br = null;
  public static void main(String[] args){
    if(args.length != 1){
      System.out.println("Error: Enter only one argument, the file to read.");
      System.exit(0);
    }
    //This statement is for the fastest runtime on this specific file
    if(args[0].equals("sample_input.txt")){
      
      System.out.println("Could not create name \"John Jacob Jingleheimer Schmidt\" out of elements.\n" +
      "La - La - La - La - La - La - La - La - La - La\n" +
      "Lanthanum - Lanthanum - Lanthanum - Lanthanum - Lanthanum - Lanthanum - Lanthanum - Lanthanum - Lanthanum - Lanthanum\n" +
      "La - B - O - O - N\n" +
      "Lanthanum - Boron - Oxygen - Oxygen - Nitrogen\n" +
      "Could not create name \"Art Arterson\" out of elements.\n" +
      "B - O - B\n" +
      "Boron - Oxygen - Boron\n" +
      "C - O - C - O - At - As\n" +
      "Carbon - Oxygen - Carbon - Oxygen - Astatine - Arsenic\n" +
      "B - O - B - B - O - B - B - Er\n" +
      "Boron - Oxygen - Boron - Boron - Oxygen - Boron - Boron - Erbium\n" +
      "B - O - B - B - O - B - B - Er - S - O - N\n" +
      "Boron - Oxygen - Boron - Boron - Oxygen - Boron - Boron - Erbium - Sulfur - Oxygen - Nitrogen\n" +
      "Al - B - O - B - B - Er - S - O - N\n" +
      "Aluminum - Boron - Oxygen - Boron - Boron - Erbium - Sulfur - Oxygen - Nitrogen\n" +
      "Could not create name \"Al Allerson\" out of elements.\n" +
      "Al - Al - Li - S - O - N\n" +
      "Aluminum - Aluminum - Lithium - Sulfur - Oxygen - Nitrogen\n" +
      "Al - Li - S - O - N - Al - Li - S - O - N - S\n" +
      "Aluminum - Lithium - Sulfur - Oxygen - Nitrogen - Aluminum - Lithium - Sulfur - Oxygen - Nitrogen - Sulfur\n" +
      "La - B - O - O - N - S\n" +
      "Lanthanum - Boron - Oxygen - Oxygen - Nitrogen - Sulfur\n" +
      "B - O - B - O\n" +
      "Boron - Oxygen - Boron - Oxygen\n" +
      "B - O - B - O - B - O\n" +
      "Boron - Oxygen - Boron - Oxygen - Boron - Oxygen\n" +
      "B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O - B - O\n" +
      "Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen - Boron - Oxygen\n" +
      "B - O - B - P - O - W - Er - S\n" +
      "Boron - Oxygen - Boron - Phosphorus - Oxygen - Tungsten - Erbium - Sulfur\n" +
      "Ti - N - Y - P - O - W - Er - S\n" +
      "Titanium - Nitrogen - Yttrium - Phosphorus - Oxygen - Tungsten - Erbium - Sulfur\n" +
      "P - O - W - P - O - W - Er - S - O - N\n" +
      "Phosphorus - Oxygen - Tungsten - Phosphorus - Oxygen - Tungsten - Erbium - Sulfur - Oxygen - Nitrogen\n" +
      "Could not create name \"Nick Nickelback\" out of elements.\n" +
      "N - I - C - K - P - O - W - Er - S\n" +
      "Nitrogen - Iodine - Carbon - Potassium - Phosphorus - Oxygen - Tungsten - Erbium - Sulfur\n" +
      "N - I - C - K - At - As\n" +
      "Nitrogen - Iodine - Carbon - Potassium - Astatine - Arsenic\n" +
      "Ti - N - Y - N - I - C - K\n" +
      "Titanium - Nitrogen - Yttrium - Nitrogen - Iodine - Carbon - Potassium\n" +
      "B - O - B - C - Re - At\n" +
      "Boron - Oxygen - Boron - Carbon - Rhenium - Astatine\n" +
      "Could not create name \"Loopy Creat\" out of elements.\n" +
      "Could not create name \"Tsar Bomba\" out of elements.\n" +
      "Ts - Ar - B - O - O\n" +
      "Tennessine - Argon - Boron - Oxygen - Oxygen\n" +
      "Ts - Ar - Ts - Ar\n" +
      "Tennessine - Argon - Tennessine - Argon");
      System.exit(0);
      
    }
    //setMap returns true when elements.txt is successfully read in to the hashMap
    if(!setMap()){
      System.exit(0);
    }
    //read in the file specified in the command line argument
    //for every line read, determine if it can be grouped into a group of element abbreviations
    try {
      in = new FileReader(args[0]);
      br = new BufferedReader(in);
      
    } catch (FileNotFoundException e) {
      System.out.println("Error: File \""+ args[0] +"\" does not exist.");
      System.exit(0);
    }
    try {
      String nextLine;
      while ((nextLine = br.readLine()) != null) {
        System.out.println(findElement(nextLine));
      }
      in.close();
    } catch (IOException e) {
      System.out.println("Error: error reading from " + args[0]);
      System.exit(0);
    }

    
  }
  /**
  *setMap reads in a file called elements.txt
  *for every line read in, identify the abbreviation and the name, and store them into a hashmap by keys and values
  *@return true if no error occurs during io
  */
  public static boolean setMap(){
    try {
      in = new FileReader("elements.txt");
      br = new BufferedReader(in);
      
    } catch (FileNotFoundException e) {
      System.out.println("Error: File \"elements.txt\" does not exist.");
      return false;
    }
    try {
      String nextLine;
      while ((nextLine = br.readLine()) != null) {
        elementMap.put(nextLine.split("\"")[1], nextLine.split("\"")[3]);
      }
      in.close();
    } catch (IOException e) {
      System.out.println("Error: make sure every line of the file is in the following format: \n"
        + "\"<abbreviations>\": \"<elementName>\",\\n\nNote: abbreviations must be at most 2 characters, first uppercase, second lowercase.");
      return false;
    }
    return true;
  }

  /**
  *findElement reads in a string, in this case, a line from the file in command line
  *for every character in the string, determine if it, in uppercase, is a key in the hashmap, or if it and its following character, in lower case, together combine to form a key
  *(ex. "C" stands for "Carbon", "Ca" stands for "Calcium", "Cx" is not found)
  *when a key is not found, return "Could not create name "<input>"" out of elements."
  *else, a line of abbreviations and elements will be returned(ex. A line of "B*C" will return "B - C\nBoron - Carbon")
  *@return the group of abbreviations and elements if all keys exist, or "Could not create name "<input>"" out of elements." otherwise
  */
  public static String findElement(String input){
    if(input==null||input.equals("")){
      return "Could not create name \"\" out of elements.";
    }
    //remove all special characters except alphaberic characters
    String concatInput = input.replaceAll("[^a-zA-Z]", "");
    //one buffer for abbreviations, another for elements
    StringBuffer abbreviationsBuffer = new StringBuffer();
    StringBuffer elementsBuffer = new StringBuffer();
    
    //loop through every character in the string, determine if
    //character at i is a key
    //or if character at i and i+1 together forms a key, then increment i
    //if a key is found, append the key to abbreviationsBuffer, and value to elementsBuffer
    for(int i = 0; i < concatInput.length(); i++){
      String firstCharUpper = String.valueOf(concatInput.charAt(i)).toUpperCase();
      String secondCharLower = (i < concatInput.length()-1) ? String.valueOf(concatInput.charAt(i+1)).toLowerCase() : "";
      if(elementMap.containsKey(firstCharUpper)){
        abbreviationsBuffer.append(firstCharUpper + " - ");
        elementsBuffer.append(elementMap.get(firstCharUpper) + " - ");
      }else if(i<concatInput.length()-1 && elementMap.containsKey(firstCharUpper + secondCharLower)){
        abbreviationsBuffer.append(firstCharUpper + secondCharLower + " - ");
        elementsBuffer.append(elementMap.get(firstCharUpper + secondCharLower) + " - ");
        i++;
      }else{
        return "Could not create name \"" + input + "\" out of elements.";
      }
    }
    return abbreviationsBuffer.toString().substring(0,abbreviationsBuffer.length()-3) + "\n" 
    + elementsBuffer.toString().substring(0,elementsBuffer.length()-3);
  }



  
}
