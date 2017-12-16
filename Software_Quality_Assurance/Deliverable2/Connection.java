
public class Connection {
	/**
	 * The destination location on the other side of a path
	 */
	public Location destination;
	/**
	 * A path that connects to a location
	 */
	public String path;
	/**
	 * Initialize a connection between a location and a path
	 * @param destination a location
	 * @param path a path that connects the location
	 */
	public Connection(Location destination, String path){
		this.destination = destination;
		this.path = path;
	}
}
