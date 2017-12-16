import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.mockito.*;
import java.io.*;
import java.util.HashMap;

public class ElementTest{
  //Test wherther setMap returns true, when a valid file name is passed into the command line
  @Test
  public void testSetMapCalled(){
    Element element = Mockito.mock(Element.class);
    String[] args = {"small.txt"};
    element.main(args);
    assertTrue(element.setMap()); 
  }

  //Test wherther the hashmap stores proper value from elements.txt, when a valid file name is passed into the command line
  @Test
  public void testSetMap(){
    Element element = Mockito.mock(Element.class);
    String[] args = {"small.txt"};
    element.main(args);
    assertTrue(element.elementMap.containsKey("B")); 
  }

  //Test wherther the hashmap does not store improper value from elements.txt, when a valid file name is passed into the command line
  @Test
  public void testSetMapNotContainedElement(){
    Element element = Mockito.mock(Element.class);
    String[] args = {"small.txt"};
    element.main(args);
    assertFalse(element.elementMap.containsKey("Z")); 
  }

  //Test whether findElement could not find a match when input is null
  @Test
  public void testFindElementNull(){
    Element e = Mockito.mock(Element.class);
    String[] args = {"small.txt"};
    e.main(args);
    assertEquals(e.findElement(null),"Could not create name \"\" out of elements.");
  }

  //Test whether findElement could not find a match when input is empty
  @Test
  public void testFindElementEmpty(){
    Element e = Mockito.mock(Element.class);
    String[] args = {"small.txt"};
    e.main(args);
    assertEquals(e.findElement(null),"Could not create name \"\" out of elements.");
  }

  //Test whether findElement could find a match when input is a single character
  @Test
  public void testFindElementSingle(){
    Element e = Mockito.mock(Element.class);
    String[] args = {"small.txt"};
    e.main(args);
    assertEquals(e.findElement("c"),"C\nCarbon");
  }

  //Test whether findElement could find a match when input are two characters
  @Test
  public void testFindElementDouble(){
    Element e = Mockito.mock(Element.class);
    String[] args = {"small.txt"};
    e.main(args);
    assertEquals(e.findElement("cb"),"C - B\nCarbon - Boron");
  }

  //Test whether findElement could find a match when input contains both upper and lower case characters
  @Test
  public void testFindElementUpperLowerCase(){
    Element e = Mockito.mock(Element.class);
    String[] args = {"small.txt"};
    e.main(args);
    assertEquals(e.findElement("Cbc"),"C - B - C\nCarbon - Boron - Carbon");
  }

  //Test whether findElement could find a match when input contains space
  @Test
  public void testFindElementSpace(){
    Element e = Mockito.mock(Element.class);
    String[] args = {"small.txt"};
    e.main(args);
    assertEquals(e.findElement("c b"),"C - B\nCarbon - Boron");
  }

  //Test whether findElement could find a match when input contains new line
  @Test
  public void testFindElementNewline(){
    Element e = Mockito.mock(Element.class);
    String[] args = {"small.txt"};
    e.main(args);
    assertEquals(e.findElement("c\nb"),"C - B\nCarbon - Boron");
  }

  //Test whether findElement could find a match when input contains special characters like *^&(...
  @Test
  public void testFindElementSpecialChar(){
    Element e = Mockito.mock(Element.class);
    String[] args = {"small.txt"};
    e.main(args);
    assertEquals(e.findElement("c*^&b"),"C - B\nCarbon - Boron");
  }

  //Test whether findElement could not find a match when input is not in elements.txt
  @Test
  public void testFindElementFail(){
    Element e = Mockito.mock(Element.class);
    String[] args = {"small.txt"};
    e.main(args);
    assertEquals(e.findElement("z"),"Could not create name \"z\" out of elements.");
  }



}