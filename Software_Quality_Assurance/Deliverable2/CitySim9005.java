import java.util.ArrayList;
import java.util.Random;

public class CitySim9005 {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/**
		*Get a seed from the commandline to pass into the random method
		*/
		Random rnd = new Random();
		int seed = 0;
		try{
			seed = Integer.parseInt(args[0]);
		}catch(Exception e){
			System.out.println("Usage: java CitySim9005 <integer>");
			System.exit(0);
		}
		
		rnd.setSeed(seed);

		//Initialize all locations, including the ones outside the city
		Location hotel = new Location("Hotel");
		Location diner = new Location("Diner");
		Location philadelphia = new Location("Philadelphia");
		Location coffee = new Location("Coffee");
		Location library = new Location("Library");
		Location cleveland = new Location("Cleveland");
		
		//Initizlize all connections to a destination via a path
		Connection hotelToDiner = new Connection(diner,"Fourth Ave.");
		Connection hotelToLibrary = new Connection(library, "Bill St.");
		Connection dinerToPhiladelphia = new Connection(philadelphia, "Fourth Ave.");
		Connection dinerToCoffee = new Connection(coffee, "Phil St.");
		Connection coffeeToDiner = new Connection(diner, "Phil St.");
		Connection coffeeToLibrary = new Connection(library, "Fifth Ave.");
		Connection libraryToHotel = new Connection(hotel, "Bill St.");
		Connection libraryToCleveland = new Connection(cleveland, "Fifth Ave.");
		
		//Add those connections to the proper locations
		hotel.addConnections(hotelToDiner, hotelToLibrary);
		diner.addConnections(dinerToPhiladelphia, dinerToCoffee);
		coffee.addConnections(coffeeToDiner, coffeeToLibrary);
		library.addConnections(libraryToHotel,libraryToCleveland);
		
		//Initialize a list of valid start locations
		ArrayList<Location> startLocations = new ArrayList<Location>();
		addLocations(startLocations, hotel, diner, coffee, library);
		
		//Driver 1 to 5 will drive through the city
		for(int i = 1; i < 6; i++){
			//Choose a random start location based on the size of valid start locations
			Location startLocation = startLocations.get(Math.abs(rnd.nextInt()%startLocations.size()));
			//Check if a, index returns null
			if(startLocation==null){
				System.out.println("Start location does not exist");
				break;
			}
			//Assign start location to current location, which will change as driver moves
			Location currentLocation = startLocation;
			
			//Loop until an end destination is reached
			while(true){
				//get the next location index based on the number of connections a locaion has
				int nextLocationIndex = Math.abs(rnd.nextInt()%currentLocation.connections.size());
				//if an end destination is reached, print out the result and break the loop
				if(currentLocation.connections.get(nextLocationIndex).destination.name.equals("Philadelphia")||
						currentLocation.connections.get(nextLocationIndex).destination.name.equals("Cleveland")){
					System.out.println("Driver " + i + " has gone to " + currentLocation.connections.get(nextLocationIndex).destination.name + "!");
					System.out.println("-----");
					break;
				}
				//print out the current location and destination that the driver moves to
				System.out.println("Driver " + i + " heading from " + currentLocation.name 
						+ " to " + currentLocation.connections.get(nextLocationIndex).destination.name 
						+ " via " + currentLocation.connections.get(nextLocationIndex).path);
				//driver heads to the next location
				currentLocation = currentLocation.connections.get(nextLocationIndex).destination;
			}
		}
	}
	/**
	 * Add an arbitrary number of locations to a list of type Location
	 * Will not add null or any location without a name
	 * @param ArrayList<Location> locationList, the list to be added
	 * @param Location locations contains an arbitrary number of locations
	 * @return -1 if the list is null, or 1 otherwise, regardless of the number of locations successfully added
	 */
	public static boolean addLocations(ArrayList<Location> locationList, Location...locations){
		boolean allLocationsAdded = true;
		if(locationList==null){
			return false;
		}
		for(Location location : locations){
			if(location==null || location.name.length()<1){
				allLocationsAdded = false;
				continue;
			}
			locationList.add(location);
		}
		return allLocationsAdded;
	}
	
}
