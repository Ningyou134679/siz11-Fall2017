import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.mockito.*;

public class CitySim9005Test{

  /**
  *Test if addLocations returns false when ArrayList is null
  */
  @Test
  public void testAddLocationsNull(){
    ArrayList<Location> locations = null;
    assertFalse(CitySim9005.addLocations(locations));
  }

  /**
  *Test if addLocations returns false when ArrayList is not null
  *but an added location is null
  */
  @Test
  public void testAddLocationsNullLocation(){
    ArrayList<Location> locations = new ArrayList<Location>();
    Location location1 = Mockito.mock(Location.class);
    location1 = null;
    assertFalse(CitySim9005.addLocations(locations,location1));
  }

  /**
  *Test if addLocations returns false when ArrayList is not null
  *but an added location has no name
  */
  @Test
  public void testAddLocationsEmptyLocation(){
    ArrayList<Location> locations = new ArrayList<Location>();
    Location location1 = Mockito.mock(Location.class);
    location1.name = "";
    assertFalse(CitySim9005.addLocations(locations,location1));
  }

  /**
  *Test if addLocations returns false when ArrayList is not null
  *but an added location has no name, while others have
  */
  @Test
  public void testAddLocationsSkipLocation(){
    ArrayList<Location> locations = new ArrayList<Location>();
    Location location1 = Mockito.mock(Location.class);
    location1.name = "";
    Location location2 = Mockito.mock(Location.class);
    location2.name = "book store";
    assertFalse(CitySim9005.addLocations(locations,location1,location2));
  }

  /**
  *Test if addLocations returns true when ArrayList is not null
  *and all the locations have a valid name
  */
  @Test
  public void testAddLocationsValidLocations(){
    ArrayList<Location> locations = new ArrayList<Location>();
    Location location1 = Mockito.mock(Location.class);
    location1.name = "park";
    Location location2 = Mockito.mock(Location.class);
    location2.name = "book store";
    assertTrue(CitySim9005.addLocations(locations,location1,location2));
  }




}