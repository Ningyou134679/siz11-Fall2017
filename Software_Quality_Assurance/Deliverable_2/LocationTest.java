import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.mockito.*;

public class LocationTest{
  /**
  *Test if addConnections returns false
  *when connection added is null
  */
  @Test
  public void testAddConnectionsNull(){
    Location location = new Location("coffee");
    Connection connection = null;
    assertFalse(location.addConnections(connection));
  }

 /**
  *Test if addConnections returns false
  *when connection added has no destination
  */
  @Test
  public void testAddConnectionsNullDestination(){
    Location location = new Location("coffee");
    Connection connection = Mockito.mock(Connection.class);
    connection.destination = null;
    connection.path="Fifth Ave.";
    assertFalse(location.addConnections(connection));
  }

  /**
  *Test if addConnections returns false
  *when connection added has empty path
  */
  @Test
  public void testAddConnectionsEmptyPath(){
    Location location = new Location("coffee");
    Location location2 = new Location("library");
    Connection connection = Mockito.mock(Connection.class);
    connection.destination = location2;
    connection.path="";
    assertFalse(location.addConnections(connection));
  }

  /**
  *Test if addConnections returns false
  *when one of the connections is invalid
  */
  @Test
  public void testAddConnectionsSkipConnection(){
    Location location = new Location("coffee");
    Location location2 = new Location("library");
    Location location3 = new Location("park");
    Connection connection = Mockito.mock(Connection.class);
    connection.destination = location2;
    connection.path="";
    Connection connection2 = Mockito.mock(Connection.class);
    connection2.destination = location3;
    connection2.path="Fifth Ave.";
    assertFalse(location.addConnections(connection,connection2));
  }

  /**
  *Test if addConnections returns true
  *when all of the connections are valid
  */
  @Test
  public void testAddConnectionsValidConnection(){
    Location location = new Location("coffee");
    Location location2 = new Location("library");
    Location location3 = new Location("park");
    Connection connection = Mockito.mock(Connection.class);
    connection.destination = location2;
    connection.path="Fourth Ave.";
    Connection connection2 = Mockito.mock(Connection.class);
    connection2.destination = location3;
    connection2.path="Fifth Ave.";
    assertTrue(location.addConnections(connection,connection2));
  }




}