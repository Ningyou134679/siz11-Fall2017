import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.mockito.*;

public class RentACatTest {
  //A test on the returnCat method in the RentACat class
  //Mock a cat c to pass in the method
  //assume that cat c is rented
  //test passes when returnCat returns true
  @Test
  public void testReturnCat(){
    RentACat rent = new RentACat();
    Cat c = Mockito.mock(Cat.class);
    Mockito.when(c.getRented()).thenReturn(true);
    assertTrue(rent.returnCat(c));
  }

  //A test on the rentCat method in the RentACat class
  //Mock a cat c to pass in the method
  //assume that cat c is not rented
  //test passes when rentCat returns true
  @Test
  public void testRentCat(){
    RentACat rent = new RentACat();
    Cat c = Mockito.mock(Cat.class);
    Mockito.when(c.getRented()).thenReturn(false);
    assertTrue(rent.rentCat(c));
  }

  //A test on the listCats method in the RentACat class
  //Create an ArrayList of type Cat, Mock two cats c1, c2 to add into the ArrayList
  //assume that cat c2 is rented, and c1 is not, c1 and c2 have different id and names
  //test passes when listCats returns the value of c1.toString()
  @Test
  public void testListCats(){
    RentACat rent = new RentACat();
    ArrayList<Cat> cats = new ArrayList<Cat>();
    Cat c1 = Mockito.mock(Cat.class);
    Mockito.when(c1.getRented()).thenReturn(false);
    Mockito.when(c1.toString()).thenReturn("ID 1. Jennyanydots");
    Cat c2 = Mockito.mock(Cat.class);
    Mockito.when(c2.getRented()).thenReturn(true);
    Mockito.when(c2.toString()).thenReturn("ID 2. Old Deuteronomy");
    cats.add(c1);
    cats.add(c2);
    //System.out.println(rent.listCats(cats));
    assertEquals(rent.listCats(cats),"ID 1. Jennyanydots\n");
  }

  //A test on the listCats method in the RentACat class
  //Create an ArrayList of type Cat with the value null
  //test passes when listCats returns ""
  @Test
  public void testListCatsNULL(){
    RentACat rent = new RentACat();
    ArrayList<Cat> cats = null;
    assertEquals(rent.listCats(cats),"");
  }
  //A test on the catExists method in the RentACat class
  //Create an ArrayList of type Cat, add a mocked cat c
  //assume that c has an id of 1, and an id of 1 is passed to the method
  //test passes when catExists returns true
  @Test
  public void testCatExists(){
    RentACat rent = new RentACat();
    ArrayList<Cat> cats = new ArrayList<Cat>();
    Cat c = Mockito.mock(Cat.class);
    Mockito.when(c.getId()).thenReturn(1);
    cats.add(c);
    assertTrue(rent.catExists(1,cats));
  }

  //A test on the catExists method in the RentACat class
  //Create an ArrayList of type Cat with the value null
  //test passes when listCats returns false
  @Test
  public void testCatExistsNULL(){
    RentACat rent = new RentACat();
    ArrayList<Cat> cats = null;
    assertFalse(rent.catExists(1,cats));
  }

  //A test on the catExists method in the RentACat class
  //Create an empty ArrayList of type Cat
  //test passes when listCats returns false
  @Test
  public void testCatExistsEmpty(){
    RentACat rent = new RentACat();
    ArrayList<Cat> cats = new ArrayList<Cat>();
    assertFalse(rent.catExists(1,cats));
  }  


}