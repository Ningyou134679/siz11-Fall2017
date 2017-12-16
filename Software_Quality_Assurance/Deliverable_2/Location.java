import java.util.ArrayList;

public class Location {
	/**
	 * The name of the location.
	 */
	public String name;
	/**
	 * A list of connections that point to another location via a path
	 */
	public ArrayList<Connection> connections;
	
	/**
	 * Initialize name for the location, and an empty list
	 * @param name
	 */
	public Location(String name){
		this.name = name;
		this.connections = new ArrayList<Connection>();
	}
	
	/**
	 * Add an arbitrary number of connections to the list
	 * Will skip if a connection is null, or the destination location is null, or if the destination does not have a name, or if a path does not exist between these locations
	 * @param newConnections
	 * @return true if all connections are added, or false if any connection is skipped
	 */
	public boolean addConnections(Connection...newConnections){
		boolean allConnectionAdded = true;
		for(Connection connection : newConnections){
			if(connection==null || connection.destination==null || connection.destination.name.length()<1 || connection.path==null ||connection.path.length()<1){
				allConnectionAdded = false;
				continue;
			}
			connections.add(connection);
		}
		return allConnectionAdded;
	}
	
}
