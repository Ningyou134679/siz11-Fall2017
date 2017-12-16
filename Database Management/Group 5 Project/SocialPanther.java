import java.sql.*;  //import the file containing definitions for the parts
import java.text.ParseException;
//needed by java for database connection and manipulation
import java.util.*;
import java.io.*;
import java.util.Scanner;
public class SocialPanther {
  private Connection connection; //used to hold the jdbc connection to the DB
  private Statement statement; //used to create an instance of the connection
  private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
  private ResultSet resultSet; //used to hold the result of your query (if one
  // exists)
  private String query;  //this will hold the query we are using
  private String userID;
  public Scanner s;

  public static final String MainHelpMessage =
	"Here are the commands you can use:\n"        +
	"\t(l)ogin [to the SocialPanther program],\n" + // tryLogin
	"\t(c)reate [an account on SocialPanther],\n" + // tryCreateUser
	"\t(h)elp [e.g. show this message]\n"         + // System.out.println(MainHelpMessage)
	"\t(q)uit [the SocialPanther program].";
  
  public static final String ChatHelpMessage =
	"Here are the commands you can use:\n"    +
	"\t(i)nitiate [a friendship],\n"          + // initiateFriendship
	"\t(d)isplay [list of friends],\n"        + // displayFriends
	"\t(a)ccept [/ reject requests],\n"       + // confirmFriendships
	"\t(m)ake [yourself a new group],\n"      + // createGroup
	"\t(r)equst [to join a group],\n" 	      + // initiateAddingGroup
	"\t(l)ist [all groups you're in],\n"      + // displayGroups
	"\t(c)hat [with your] <friend>, remember to (d)isplay first\n"        + // sendMessageToUser
	"\t(g)roup [chat with] <groupname>, remember to (l)ist first\n"    + // sendMessageToGroup
	"\t(s)how [all received messages],\n"     + // displayMessages
	"\t(v)iew [messages since last logout],\n"+ // displayNewMessages 
	"\t(f)ind [other users by search],\n"     + // searchUsers
	"\t(3)deg [find link btwn. two users],\n" + // threeDegrees
	"\t(t)op  [, list top message-senders],\n"+ // topMessages
	"\t(q)uit [and logout of menu],\n"	      + // LogOut
	"\t(e)rase [your account],\n"             + // dropUser
	"\t(h)elp [e.g. show this message]";        // System.out.println(ChatHelpMessage)
  
  public static final String WelcomeMessage = "Welcome to SocialPanther!";
  
  public SocialPanther(String url, String username, String password) throws SQLException {
	s = new Scanner(System.in);
	connection = DriverManager.getConnection(url, username, password); 
  }

  public boolean createUser(String userID, String emailAddr, String dateOfBirth) {
    return createUser(userID, userID, emailAddr, dateOfBirth, "abc123");
  }
  
  public boolean createUser(String user, String name, String emailAddr, String dateOfBirth, String passwd){
    String tablename = "profile";
    if(name.length() > 20){
      System.out.println("createUser: Please enter a userID less than 21 characters.");
      return false;
    }
    if(emailAddr.length() > 50){
      System.out.println("createUser: Please enter a email address less than 51 characters.");
      return false;
    }
    if(!dateOfBirth.matches("\\d{4}-\\d{2}-\\d{2}")){
      System.out.println("createUser : dateOfBirth is not in the right format yyyy-MM-dd");
      return false;
    }
	if(passwd.length() > 50){
      System.out.println("createUser: Please enter a password less than 51 characters.");
      return false;
    }

    
    try {
      query = "insert into " + tablename + " values (?,?,?,?,?,?)";
      prepStatement = connection.prepareStatement(query);
	  
	  
      java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
      // This is how you format a date so that you can use the setDate format below
      java.sql.Date date_reg = new java.sql.Date (df.parse(dateOfBirth).getTime());
	  
      prepStatement.setString(1, user);
      prepStatement.setString(2, name);
      prepStatement.setString(3, emailAddr);
      prepStatement.setString(4, passwd);
      prepStatement.setDate(5, date_reg);
      prepStatement.setTimestamp(6, new Timestamp(0));
	  
      prepStatement.executeUpdate();
      System.out.println("Created user with userID: " + user + ", password: " + passwd + ", email address: " + emailAddr + ", date of birth: " + dateOfBirth + ".");
    } catch (SQLException Ex) {
      if(Ex.toString().contains("ORA-00001")){
        System.out.println("createUser: User with the userID " + user + " already exists.");
      } else if (Ex.toString().contains("ORA-01400")) {
        System.out.println("createUser: UserID cannot be empty.");
      } else {
        System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
      }
      return false;
    } 
    catch (ParseException e) {
      System.out.println("Error parsing the date. Machine Error: " + e.toString());
      return false;
    }
    finally{
      try {
        if (statement != null) statement.close();
        if (prepStatement != null) prepStatement.close();
      } catch (SQLException e) {
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
        return false;
      }
    }
	return true;
  }
	
  /* chatMenu
   * Provides a looping menu.
   * 
   * prereq: userID must be set.
   */
  public void chatMenu(){
  	//To enter here, we must be logged in.
	String selectQuery;
	Set<String> friends = new TreeSet<String>();
	Set<String> groups = new TreeSet<String>();
	PreparedStatement ps = null;
	ResultSet re = null;
  	try{
	  updateFriends(friends);
  	  updateGroups(groups);
	  
  	  boolean looping = true;
  	  String command = " ";
	  String[] tokens;
  	  
	  System.out.println(ChatHelpMessage);
	  
  	  do {
		System.out.print(userID + "# ");
		command = s.nextLine() + "#";
		tokens = command.split("\\s", -1);
		switch (command.charAt(0)) {
		case 'i': // initiateFriendship
		  initiateFriendship(friends);
		  break;
		case 'd': // displayFriends
		  displayFriends(friends);
		  break;
		case 'a': // confirmFriendships
		  confirmFriendships();
		  break;
		case 'm': // createGroup
		  //createGroup();
      int memLimit = 1;
      while (true) {
        try {
          memLimit = Integer.parseInt(promptString(4, "member limit"));
          break;
        } catch (NumberFormatException numEx) {
          System.out.println("Please enter a valid integer between 1 and 9999 for the limit");
          continue;
        }
      }
      createGroup(promptString(20, "groupID"), promptString(200, "group desctiption"), memLimit);
		  break;
		case 'r': // initiateAddingGroup
		  //initiateAddingGroup();
      initiateAddingGroup(promptString(20, "group id"), groups);
		  break;
		case 'l': // displayGroups
		  displayGroups(groups);
		  break;
		case 'c': // sendMessageToUser
		  sendMessageToUser(promptString(20, "username"), friends);
		  break;
		case 'g': // sendMessageToGroup
      sendMessageToGroup(promptString(20, "group id"), groups);
		  break;
		case 's': // displayMessages
		  displayMessages(groups);
		  break;
		case 'v': // displayNewMessages
		  displayNewMessages();
		  break;
		case 'f': // searchUsers
		  searchUsers();
		  break;
		case 't': // topMessages
		  topMessages();
		  break;
		case '3': // threeDegree
		  tryThreeDegrees();
		  break;
		case 'q': // LogOut
		  logOut();
		  looping = false;
		  break;
		case 'h': // System.out.println(ChatHelpMessage)
		  System.out.println(ChatHelpMessage);
		  break;
		case 'e': //erase/drop user
		  dropUser();
		  looping = false;
		  break;
		case ' ': // No command sent
		  System.out.println("You must enter a command. Try 'h' for help.");
		  break;
		default:
		  System.out.println("Unknown command! Try 'h' for help.");
		  break;
		}
  	  } while (looping);
    } catch (Exception e) {
      System.out.println(e);
      return;
    }
  }

  /**
  *Do not call if not necessary
  */
  public void dropUser() {
    System.out.println("Dropping user " + userID);
    PreparedStatement ps = null;
    try{
      
      System.out.println("Removing all new messages.");
      String selectQuery = "delete from messagerecipient where userID=? or msgID in (select msgID from MESSAGES where fromID=?)";
      ps = connection.prepareStatement(selectQuery); //create an instance

      ps.setString(1, userID);
      ps.setString(2, userID);
      ps.executeUpdate();

      System.out.println("Removing all group requests.");
      selectQuery = "delete from PENDINGGROUPMEMBERS where userID=?";
      ps = connection.prepareStatement(selectQuery); //create an instance

      ps.setString(1, userID);
      ps.executeUpdate();

      System.out.println("Removing from all groups.");
      selectQuery = "delete from GROUPMEMBERSHIP where userID=?";
      ps = connection.prepareStatement(selectQuery); //create an instance

      ps.setString(1, userID);
      ps.executeUpdate();

      System.out.println("Removing from all friends.");
      selectQuery = "delete from FRIENDS where userID1=? or userID2=?";
      ps = connection.prepareStatement(selectQuery); //create an instance

      ps.setString(1, userID);
      ps.setString(2, userID);
      ps.executeUpdate();

      System.out.println("Removing all friend requsts.");
      selectQuery = "delete from PENDINGFRIENDS where fromID=? or toID=?";
      ps = connection.prepareStatement(selectQuery); //create an instance

      ps.setString(1, userID);
      ps.setString(2, userID);
      ps.executeUpdate();

      System.out.println("Removing all messages.");
      selectQuery = "delete from MESSAGES where fromID=? or toUserID=?";
      ps = connection.prepareStatement(selectQuery); //create an instance

      ps.setString(1, userID);
      ps.setString(2, userID);
      ps.executeUpdate();

      System.out.println("Removing from profile.");
      selectQuery = "delete from profile where userID=?";
      ps = connection.prepareStatement(selectQuery); //create an instance

      ps.setString(1, userID);
      ps.executeUpdate();


      // clean up username 
      //userID = null;
      System.out.println("Setting current userID to null");
      userID = null;

      System.out.println("Logging out.");
      return;
    } catch (SQLException Ex) {
      System.out.println("Drop user failed.");
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
      return;
    } finally {
      try {
        if (ps != null) ps.close();
      } catch (SQLException e) {
        System.out.println("Drop user failed.");
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
      }
    }
  }

  public void logOut() {
	  // Tell server to set last logout to current time
    // clean up old messages
    if(userID == null) {
      System.out.println("");
      return;
    }
    System.out.println("Logging out");
    PreparedStatement ps = null;
    try{
      System.out.println("Updating user last login time.");
      String selectQuery = "update profile set lastLogin=? where userID=?";
      ps = connection.prepareStatement(selectQuery); //create an instance

      ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
      ps.setString(2, userID);
      
      ps.executeUpdate(); 

      System.out.println("Removing all new messages.");
      selectQuery = "delete from messagerecipient where userID=?";
      ps = connection.prepareStatement(selectQuery); //create an instance

      ps.setString(1, userID);
      ps.executeUpdate();

      // clean up username 
      //userID = null;
      System.out.println("Setting current userID to null");
      userID = null;

      System.out.println("Logout succecceded.");
      return;
    } catch (SQLException Ex) {
      System.out.println("Logout failed.");
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
      return;
    } finally {
      try {
        if (ps != null) ps.close();
      } catch (SQLException e) {
        System.out.println("Logout failed.");
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
      }
    }
  }
	

	


  public void mainMenu() {
    System.out.println(WelcomeMessage);
	
	
	boolean looping = true;
	String command = " ";
	
	do {
    System.out.println("\n" + MainHelpMessage);
	  System.out.print("# ");
	  command = s.nextLine() + "#";

	  switch (command.charAt(0)) {
	  case 'l':
		if (tryLogin()) {
		  chatMenu();
		}
		break;
	  case 'c':
		tryCreateUser();
		break;
	  case 'q':
		looping = false;
		break;
	  case '#':
		System.out.println("Please enter a command! (Type 'h' for help.)");
		break;
	  case 'h':
		System.out.println(MainHelpMessage);
		break;
	  }
	} while (looping);
	System.out.println("Thank you!");
	// XXX: Send a query that sets lastLogin
  }

  public boolean tryCreateUser() {
	String inUser;
	String inName = "";
	String inEmail;
	String inPass = "";
	String inDOB = "";
	
	//System.out.print("Enter a username: ");
	inUser = promptString(20, "userID");
  inEmail = promptString(50, "email address");
  while(!inDOB.matches("\\d{4}-\\d{2}-\\d{2}")) {
    System.out.print("Enter your date of birth in the format yyyy-MM-dd: ");
    inDOB = s.nextLine();
  }

  System.out.print("Would you like to enter your name?"
    + "(Enter 'Y' to enter, or any other key to default to \"" + inUser + "\") > ");
  if (s.nextLine().equalsIgnoreCase("y")) {
    inName = promptString(50, "name");
  }
  System.out.print("Would you like to enter your password?"
    + "(Enter 'Y' to enter, or any other key to default to \"abc123\") > ");
  if (s.nextLine().equalsIgnoreCase("y")) {
    inPass = promptString(50, "password");
  }

  if (inName.length() > 1) {
    if (inPass.length() > 1)
      return createUser(inUser, inName, inEmail, inDOB, inPass);
    return createUser(inUser, inName, inEmail, inDOB, "abc123");
  } 

  if (inName.length() < 1) {
    if (inPass.length() > 1)
      return createUser(inUser, inUser, inEmail, inDOB, inPass);
    return createUser(inUser, inUser, inEmail, inDOB, "abc123");
  }
  return createUser(inUser, inEmail, inDOB);
  /*
	System.out.print("Enter your real name: ");
	inName = s.nextLine();
	System.out.print("Enter your email: ");
	inEmail = s.nextLine();
	System.out.print("Enter your date of birth in the format yyyy-MM-dd: ");
	inDOB = s.nextLine();
	System.out.print("Choose a password: ");
	inPass = s.nextLine();
	
	if(createUser(inUser, inName, inEmail, inDOB, inPass)) {
	  System.out.println("Account created. Welcome to the SocialPanther app!");
	  return true;
	}
	return false;
  */
  }

  public boolean tryLogin() {
	boolean looping = true;
	boolean loggedIn = false;
	
	String userIn;
	String passIn;
	do {
	  System.out.print("Please enter your username (nothing to exit): ");
	  userIn = s.nextLine();
	  if(!userIn.isEmpty()) {
  		System.out.println(userIn + " WAS ENTERED");
  		System.out.print("Please enter your password: ");
  		passIn = s.nextLine();
  	    loggedIn = login(userIn, passIn);
  		if(!loggedIn)
  		  System.out.println("Login failed -- please try again.");
  		looping = !loggedIn;
	  }
	  else {
		  looping = false;
	  }
	} while (looping);
	return loggedIn;
  }

  //Assume that str contains "'", put another "'" infront of every "'"
  /*
  public String handleSingleQuotes(String str){
    if (!str.contains("'")) {
      return str;
    }
    StringBuilder builder = new StringBuilder(str);
    int newLength = str.length();
    for(int i = 0; i < newLength; i++){
      if(builder.charAt(i)=='\''){
        builder.insert(i++,'\'');
        newLength++;
      }
    }
    return builder.toString();
  }*/

  /**
  *
  */
  public boolean initiateFriendship(Set<String> friends){
    
    //prompt the user to enter a target user to send friend request to
    String toUser = promptString(20, "userID");
    //assuming that userID is case sensitive
    //forbidden users to send friend request to themselves
    
    if (toUser.equals(userID)) {
      System.out.println("Cannot send friend request to yourself");
      return false;
    }
    
    //check if use is already friend with the target user
    if (friends != null && friends.contains(toUser)) {
      System.out.println("initiateFriendship: User " + userID + " is already friend with user " + toUser);
      return false;
    }

    if (!userExists(toUser)) {
      System.out.println("initiateFriendship: user " + toUser + " does not exist.");
      return false;
    }
    //prompt the user to enter a message for friends request
    String message = promptString(200, "message");

    //confirmation
    System.out.println("Confimation: Do you want to sent a friend requiest to " + toUser 
      + " with the message \"" + message + "\"? (Enter 'Y' to process)");
    if(!s.nextLine().equalsIgnoreCase("Y")){
      System.out.println("Friend request is cancelled.");
      return false;
    }

    try{
      
      //update pendingfriends table
      query = "insert into pendingfriends values (?,?,?)";
      prepStatement = connection.prepareStatement(query);

      prepStatement.setString(1, userID);
      prepStatement.setString(2, toUser);
      prepStatement.setString(3, message);

      prepStatement.executeUpdate();
      System.out.println("Friend request is completed.");
      return true;
    } 
    /*catch (ParseException e) {
      System.out.println("Error parsing the date. Machine Error: " + e.toString());
      return false;
    } */
    catch (SQLException Ex) {
      System.out.println("You have already sent a friend request to " + toUser);
      return false;
    } finally {
      try {
        if (statement != null) statement.close();
        if (prepStatement != null) prepStatement.close();
      } catch (SQLException e) {
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
        return false;
      }
    }
  }

  /**
  *Takes in a userID, a password
  *check if they are in the profile table
  *if yes return true, if not, return false
  */
  public boolean login(String myUserID, String password){
    if(myUserID.length() > 20){
      System.out.println("login: Please enter a username less than 20 characters.");
      return false;
    }
    if(password.length() > 50){
      System.out.println("login: Please enter a password less than 50 characters.");
      return false;
    }

	PreparedStatement ps = null;
    try{
	  String selectQuery = "SELECT count(*) FROM profile where userID=? and password=?";
	  ps = connection.prepareStatement(selectQuery); //create an instance

	  ps.setString(1, myUserID);
	  ps.setString(2, password);
      
      ResultSet rs = ps.executeQuery(); //run the query on the DB table

      if (rs.next()) {
        if (rs.getInt(1) > 0){
          System.out.println("userID: " + myUserID + ", password: " + password + " is found. Logging in.");
          userID = myUserID;
          if (statement != null) statement.close();
          if (prepStatement != null) prepStatement.close();
          rs.close();
          return true;
        }
      }
      rs.close();
      System.out.println("Login failed.");
      return false;
    } catch (SQLException Ex) {
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
      return false;
    } finally {
      try {
        if (ps != null) ps.close();
      } catch (SQLException e) {
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
      }
    }
  }

  //Prompt the user to enter a string, by a certain type(ex. username), 
  //in a certain range, from 1 to range
  public String promptString(int range, String type) {
    System.out.print("Enter a " + type + ": \n> ");
    String returnString = "";
    do{
       returnString = s.nextLine();
       if(returnString.length() > range || returnString.length() <= 0){
          System.out.print("Please enter a(n)" + type + " between 1 and " + range + " characters.\n> ");
       }
    } while (returnString.length() <= 0 || returnString.length() > range);
    return returnString;
  }

  /**
  *Given a range and a type, return a string in that range and of that type
  *Created to handle multi-line user input
  *prompt the user to enter n number of lines for the input, then prompt for n lines of imput
  */
  public String promptMultiLineString(int range, String type) {
    if (range < 1 || type == null || type.length() < 1) {
      System.out.println("promptMultiLineString: Please enter a valid range(1~) and a valid type.");
      return "";
    }
    System.out.print("Please enter the number of lines of " + type + " that you want to enter: \n> ");
    int numLines = -1;
    while (numLines < 0) {
      try {
        numLines = Integer.parseInt(s.nextLine());
      } catch (NumberFormatException numEx) {
        System.out.print("Please enter a valid integer greater than 0 for the number of lines of input:\n> ");
      }
    }
    if (numLines <= 0) return "";
    return promptMultiLineString(range, type, numLines);
  }

  /**
  *Given a range, a type, and a number of lines, 
  *return a string in that range and of that type, containing numLines lines of input, seperated by new line characters
  *Created to handle multi-line user input
  *prompt the user to enter n number of lines for the input, then prompt for n lines of imput
  */
  public String promptMultiLineString(int range, String type, int numLines) {
    if (range < 1 || type == null || type.length() < 1 || numLines < 1) {
      System.out.println("promptMultiLineString: Please enter a valid range(1~), a valid type, and a valid number of lines(1~).");
      return "";
    }
    System.out.println("Start entering " + type + " within " + (range - numLines) + " characters: ");
    StringBuilder builder = new StringBuilder();
    do {
      for (int i = 0; i < numLines; i++) {
        System.out.print("> ");
        builder.append(s.nextLine());
        builder.append("\n");
      } 
      if (builder.length() <= range) break;
      System.out.println("Input has exceeded the range " + (range - numLines) + " characters. Please re-enter: ");
      builder = new StringBuilder();
    } while (true);
    System.out.println("You entered: ");
    for (String token : builder.toString().split("\n")) {
      System.out.println(token);
    }
    return builder.toString();

  }

  public void readTable(String tablename){
    //System.out.println("selectFromProfile called.");
    int counter = 1;
    try{
      statement = connection.createStatement(); //create an instance
      String selectQuery = "SELECT * FROM " + tablename; //sample query
      
      resultSet = statement.executeQuery(selectQuery); //run the query on the DB table

      while(resultSet.next()) {
        if(tablename.equalsIgnoreCase("profile")){
          System.out.println(tablename + counter + ": " + 
            resultSet.getString(1) + ", " +
            resultSet.getString(2) + ", " +
            resultSet.getString(3) + ", " +
            resultSet.getString(4) + ", " +
            resultSet.getDate(5) + ", " +
            resultSet.getTimestamp(6) + ", " 
          );
        }
        counter++;
      }
	  
      resultSet.close();
	  
    } catch (SQLException Ex) {
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
    } 
    /*
	  catch (ParseException e) {
      System.out.println("Error parsing the date. Machine Error: " + e.toString());
	  }*/
    finally{
      try {
        if (statement != null) statement.close();
        if (prepStatement != null) prepStatement.close();
      } catch (SQLException e) {
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
      }
    }
  }

  // Update the set of friends
  private void updateFriends(Set<String> friends) {
	Set<String> newFriends = new TreeSet<String>();
    try {
      Statement st = connection.createStatement(); //create an instance
      String selectQuery = "SELECT * FROM FRIENDS WHERE USERID1='" + userID + "'" + " or userID2='" + userID + "'";
      ResultSet re = st.executeQuery(selectQuery);
	  
      while(re.next()) {
        if (re.getString(1).equalsIgnoreCase(userID)) {
          newFriends.add(re.getString(2));
        } else if (re.getString(2).equalsIgnoreCase(userID)) {
          newFriends.add(re.getString(1));
        }
      }
	    
	  
  	  if (st != null) st.close();
	  re.close();
	} 
    /*catch (ParseException e) {
      System.out.println("Error parsing the date. Machine Error: " + e.toString());
      return false;
	  } */
    catch (SQLException Ex) {
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
    }
	
	friends.retainAll(newFriends);
	friends.addAll(newFriends);
  }

  /**
  *given a userID, check if it exists in the profile table
  */
  public boolean userExists(String id){
    try {
      statement = connection.createStatement(); //create an instance
      String selectQuery = "SELECT count(*) FROM profile where userID='" + id + "'"; //sample query
      resultSet = statement.executeQuery(selectQuery);
      
      //check if target user exists in the profile table
      while(resultSet.next()) {
        if(resultSet.getInt(1) > 0){
          System.out.println("userExists: userID: " + id +  " is found.");
          if (statement != null) statement.close();
          if (prepStatement != null) prepStatement.close();
          resultSet.close();  
          return true; 
        }
      }
      return false;
    } catch (SQLException Ex) {
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
      return false;
    } finally {
      try {
        if (statement != null) statement.close();
        if (prepStatement != null) prepStatement.close();
      } catch (SQLException e) {
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
        return false;
      }
    }
  }
  
  private void displayFriends(Set<String> friends) {
	// Update the list of friends.
	updateFriends(friends);

	int i = 0; // number of friends
	// Print the list of friends
	for(String f : friends) {
	  System.out.print(f + ", ");
	  i++;
	}

	if(i == 0) {
	  System.out.println("It doesn't appear you have any friends.");
	} else {
	  System.out.println("... are all your friends.");
	}
  }

  private boolean acceptOrRejectFriendship(String fromID, boolean isAccepting, String requestMessage) throws SQLException {
	PreparedStatement ps = null;
	try {
	  if(isAccepting) {
		String insertQuery = "INSERT INTO FRIENDS VALUES(?,?,?,?)";
		ps = connection.prepareStatement(insertQuery);
		ps.setString(1, fromID);
		ps.setString(2, userID);
		ps.setNull(3, Types.DATE);
		ps.setString(4, requestMessage);
		ps.executeUpdate();

		// This hits a trigger that should remove the other request.
	  }
	  else {
		String deleteQuery = "DELETE FROM PENDINGFRIENDS WHERE FROMID=? AND TOID =?";
		ps = connection.prepareStatement(deleteQuery);
		ps.setString(1, fromID);
		ps.setString(2, userID);
		ps.executeUpdate();
	  } 
	}
	catch (SQLException e) {
	  System.out.println("SQL Exception: " + e.toString());
	  return false;
	}
	finally {
	  if (ps != null) ps.close();
	}
	return true;
  }
	
  private void confirmFriendships() {
	ArrayList<String> newFriends = new ArrayList<String>();
	ArrayList<String> newFReqMsg = new ArrayList<String>();
	ArrayList<String> newGroupMs = new ArrayList<String>();
	ArrayList<String> newGroupTo = new ArrayList<String>();
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
      String selectQuery = "SELECT * FROM PENDINGFRIENDS WHERE toID=?";
	  
	  ps = connection.prepareStatement(selectQuery); //create an instance
	  ps.setString(1, userID);
	  rs = ps.executeQuery();
	  
	  // print out friendships to user
	  int index = 0;
	  System.out.println("Here are your pending friend requests:");
      while(rs.next()) {
		System.out.println(index + " '" + rs.getString(1) + "', who says, '" + rs.getString(3) + "'");
		newFriends.add(rs.getString(1));
		newFReqMsg.add(rs.getString(3));
		index++;
	  }

	  // Setup acceptance array for friends
	  int totalFs = index;
	  boolean[] acceptedFriend = new boolean[totalFs];
	  for(index = 0; index < totalFs; index++) { 
		acceptedFriend[index] = false;
	  }

	  String gSelectQuery = "SELECT * FROM PENDINGGROUPMEMBERS WHERE gid in (select gid from groupmembership where userid=? and role='manager')";
	  
	  ps = connection.prepareStatement(gSelectQuery); //create an instance
	  ps.setString(1, userID);
      rs = ps.executeQuery();
	  
	  // print out new group members to user
	  int gIndex = 0;
	  System.out.println("Here are your pending group addition requests:");
      while(rs.next()) {
		System.out.println((gIndex + totalFs) + " '" + rs.getString(2) + "'->" + rs.getString(1) +  ", who says, '" + rs.getString(3) + "'");
		newGroupMs.add(rs.getString(2));
		newGroupTo.add(rs.getString(1));
		gIndex++;
	  }

	  // Setup acceptance array for friends
	  int totalGs = gIndex;
	  boolean[] acceptedGroup = new boolean[totalGs];
	  for(gIndex = 0; gIndex < totalGs; gIndex++) { 
		acceptedGroup[gIndex] = false;
	  }

	  if(totalFs + totalGs > 0) {
		int count = 0;
		System.out.println("Give the numbers of the friends you want to add, separated by spaces.");
		System.out.println("Enter 'none' to reject all, and enter 'all' to accept all.");
		System.out.println("All other friends will be dropped. Enter nothing to cancel.");

		String response = s.nextLine();
		int uAccepted = 0;
		
		if(response.equals("all")) {
		  for(int n = 0; n < acceptedFriend.length; n++) {
			acceptedFriend[n] = true;
		  }
		  for(int n = 0; n < acceptedGroup.length; n++) {
			acceptedGroup[n] = true;
		  }
		  uAccepted += acceptedFriend.length + acceptedGroup.length;
		}
		else if(response.equals("none")) { uAccepted = -1; }
		else {	  
		  Scanner i = new Scanner(response);
		  while(i.hasNext()) {
			if(i.hasNextInt()) {
			  int acceptNext = i.nextInt();
			  if(acceptNext >= 0 && acceptNext < totalFs) {
				acceptedFriend[acceptNext] = true;
				uAccepted++;
			  }
			  else if(acceptNext >= totalFs && acceptNext < totalFs + totalGs) {
				acceptedGroup[acceptNext - totalFs] = true;
				uAccepted++;
			  }
			}
			else { i.next(); }
		  }
		}
		if(uAccepted != 0) {
		  boolean success = false;
		  for(index = 0; index < totalFs; index++) {
			success = (acceptOrRejectFriendship(newFriends.get(index), acceptedFriend[index], newFReqMsg.get(index)));
			System.out.println((success ? "Succeeded to " : "Failed to ") + (acceptedFriend[index] ? "accept" : "reject") + " request from " + newFriends.get(index) + ".");
		  }
		  for(index = 0; index < totalGs; index++) {
			success = (acceptOrRejectMembership(newGroupMs.get(index), newGroupTo.get(index), acceptedGroup[index]));
			System.out.println((success ? "Succeeded to " : "Failed to ") + (acceptedGroup[index] ? "accept" : "reject") + " membership request from " + newGroupMs.get(index) + " into " + newGroupTo.get(index) + ".");
		  }
		}
		else {
		  System.out.println("Taking no action ...");
		}
	  }
	  else {
		System.out.println("   ... actually, it doesn't appear you have any outstanding requests.");
	  }

	  if (ps != null) ps.close();
	  rs.close();
	} 
    /*catch (ParseException e) {
      System.out.println("Error parsing the date. Machine Error: " + e.toString());
      return false;
	  } */
    catch (SQLException Ex) {
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
    }	
  }
  
  //given a group name, a desctiption, a membership limie
  //create a group, assign the creator a role of manager
  private void createGroup(String id, String desc, int memLimit){
    createGroup(id, id, desc, memLimit);
  }

  private void createGroup(String id, String name, String desc, int memLimit) {
    if (id == null || id.length() < 1 || id.length() > 20) {
      System.out.println("createGroup: Please enter a valid id of length between 1 and 20");
    }
    if (name == null || name.length() < 1 || name.length() > 50) {
      System.out.println("createGroup: Please enter a valid name of length between 1 and 50");
    }
    if (desc == null || desc.length() < 1 || desc.length() > 200) {
      System.out.println("createGroup: Please enter a valid desctiption of length between 1 and 20");
    }
    try{
      query = "insert into groups values (?,?,?,?)";
      prepStatement = connection.prepareStatement(query);

      prepStatement.setString(1, id);
      prepStatement.setString(2, name);
      prepStatement.setInt(3, memLimit);
      prepStatement.setString(4, desc);

      prepStatement.executeUpdate();
      System.out.println("createGroup: Group of id " + id + " is added.");

      query = "insert into groupmembership values (?,?,?)";
      prepStatement = connection.prepareStatement(query);

      prepStatement.setString(1, id);
      prepStatement.setString(2, userID);
      prepStatement.setString(3, "manager");

      prepStatement.executeUpdate();
      System.out.println("createGroup: User " + userID + " is assigned the role of \"manager\" in group " + id);

    } catch (SQLException Ex) {
      if(Ex.toString().contains("ORA-00001")) {
        System.out.println("createGroup: Group " + id + " has already been created.");
      } else {
        System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
      }
      
      return;
    } finally {
      try {
        if (statement != null) statement.close();
        if (prepStatement != null) prepStatement.close();
      } catch (SQLException e) {
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
        return;
      }
    }
  }

  /**
  *Given a group id, check if the user is already in the group
  *if yes, user cannot send a request to a group he is currently in
  *determine if member limit in the group has reached
  *if not, send a group request to group, add an entry in pendingGroupMembers
  *@param gID, the groupID to send request to
  *@param groups, to determine if a user is already in the group
  */
  private void initiateAddingGroup(String gID, Set<String> groups) {
    if (gID == null || gID.length() < 1) {
      System.out.println("initiateAddingGroup: Please enter a valid gID");
      return;
    }
    if (groups != null) {
      updateGroups(groups);
    }
    if (groups != null && groups.contains(gID)) {
      System.out.println("initiateAddingGroup: User " + userID + " is already in the group " + gID);
      return;
    }

    PreparedStatement ps = null;
    try {
      System.out.println("initiateAddingGroup: checking membership limit");
      // Setting up query
      String selectQuery = "SELECT count(*) from groupmembership where gID=?";
      ps = connection.prepareStatement(selectQuery);
      ps.setString(1, gID);
      ResultSet rs = ps.executeQuery();

      // Get the current number of group members
      int currentGroupMember = 0;
      while (rs.next()) {
        currentGroupMember += rs.getInt(1);
      }
      System.out.println("initiateAddingGroup: group " + gID + " currently has " + currentGroupMember + " members.");

      // Get the maximum limit of the group
      selectQuery = "SELECT mem_limit from groups where gID=?";
      ps = connection.prepareStatement(selectQuery);
      ps.setString(1, gID);
      rs = ps.executeQuery();
      int memLimit = 0;
      while (rs.next()) {
        memLimit = rs.getInt(1);
      }

      // Check if current group members have reached the capacity of group limit
      if (currentGroupMember >= memLimit) {
        System.out.println("initiateAddingGroup: Group membership limit for group " + gID + " has reached. Cannot send more request.");
        return;
      }

      //prompt the user for a message to send in group request
      System.out.println("Initiating group request. Enter a message for group request.");
      String message = promptString(200, "group request message");

      System.out.println("initiateAddingGroup: Sending group request.");
      selectQuery = "insert into PENDINGGROUPMEMBERS values (?,?,?)";
      ps = connection.prepareStatement(selectQuery);
      ps.setString(1, gID);
      ps.setString(2, userID);
      ps.setString(3, message);
      
      ps.executeUpdate();

      System.out.println("Group request succussfully sended to group " + gID);
      // cleaning up
      rs.close(); 
      return;
    
    } catch (SQLException Ex) {
      if (Ex.toString().contains("ORA-00001")) {
        System.out.println("initiateAddingGroup: User " + userID + " has already sent a request to group " + gID + ". Cannot sent another request.");
        return;
      }
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
      return;
    } finally {
      try {
        if (ps != null) ps.close();
      } catch (SQLException e) {
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
        return;
      }
    }
  }

  private void updateGroups(Set<String> groups) {
	  Set<String> newGroups = new TreeSet<String>();
    try {
      Statement st = connection.createStatement(); //create an instance
      String selectQuery = "SELECT * FROM groupmembership where userID='" + userID + "'";
	    ResultSet re = st.executeQuery(selectQuery);
	  
      while(re.next()) {
	    newGroups.add(re.getString("gID"));
	  }

	  if (st != null) st.close();
	  re.close();
	} 
    /*catch (ParseException e) {
      System.out.println("Error parsing the date. Machine Error: " + e.toString());
      return false;
	  } */
    catch (SQLException Ex) {
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
    }
	
    groups.retainAll(newGroups);
	groups.addAll(newGroups);
  }
  
  private void displayGroups(Set<String> groups) {
	// Update the list of groups.
	updateGroups(groups);

	int i = 0; // number of groups
	// Print the list of groups
	for(String g : groups) {
	  System.out.print(g + ", ");
	  i++;
	}

	if(i == 0) {
	  System.out.println("It doesn't appear you are a part of any groups.");
	} else {
	  System.out.println("... are all groups you're part of.");
	}
  }
  
  private boolean acceptOrRejectMembership(String requester, String group, boolean isAccepting) throws SQLException {
	PreparedStatement ps = null;
	try {
	  if(isAccepting) {
		String insertQuery = "INSERT INTO GROUPMEMBERSHIP VALUES(?,?,?)";
		ps = connection.prepareStatement(insertQuery);
		ps.setString(1, group);
		ps.setString(2, requester);
		ps.setString(3, "member");
		ps.executeUpdate();

		// This update hits a trigger that should remove the other request.
	  }
	  else {
		String deleteQuery = "DELETE FROM PENDINGGROUPMEMBERS WHERE gID=? AND userID=?";
		ps = connection.prepareStatement(deleteQuery);
		ps.setString(1, group);
		ps.setString(2, requester);
		ps.executeUpdate();
	  } 
	}
	catch (SQLException e) {
	  System.out.println("SQL Exception: " + e.toString());
	  return false;
	}
	finally {
	  if(ps != null) { ps.close(); }
	}
	return true;
  }
  
  private boolean sendMessageToUser(String toUser, Set<String> friends) {
    boolean returnVal = false;
    //check is userID passed in is null, or outside range
    if(toUser == null || toUser.length() < 1 || toUser.length() > 20) {
      System.out.println("sendMessageToUser: Please enter a valid userID between 1 and 20 characters.");
      return false;
    }
    System.out.println("You are trying to send a message to user " + toUser);
    if(friends == null || !friends.contains(toUser)){
      System.out.println("sendMessageToUser: user " + userID + " is not a friend with user " + toUser);
      return false;
    }
    //String message = promptString(200, "message");
    String message = promptMultiLineString(200, "message");
    try {
      query = "insert into messages values (?,?,?,?,?,?)";
      prepStatement = connection.prepareStatement(query);
    
    
      //java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
      // This is how you format a date so that you can use the setDate format below
      //java.sql.Date date_reg = new java.sql.Date (df.parse(dateOfBirth).getTime());
    
      prepStatement.setNull(1, Types.INTEGER);
      prepStatement.setString(2, userID);
      prepStatement.setString(3, message);
      prepStatement.setString(4, toUser);
      prepStatement.setString(5, "");
      prepStatement.setDate(6, new java.sql.Date(System.currentTimeMillis()));
    
      prepStatement.executeUpdate();
      returnVal = true;
      System.out.println("sendMessageToUser: User " + userID + " has sent to user " + toUser + " message: \"" + message + "\"");
    } catch (SQLException Ex) {
        System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
        return false;
    } finally {
      try {
        if (statement != null) statement.close();
        if (prepStatement != null) prepStatement.close();
        return returnVal;
      } catch (SQLException e) {
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
        return false;
      }
    }

  }
  
  private void sendMessageToGroup(String gID, Set<String> groups) {
    if (gID == null || gID.length() < 1) {
      System.out.println("sendMessageToGroup: The group specifid does not exist.");
    }
    if (userID == null || userID.length() < 1) {
      System.out.println("sendMessageToGroup: User is not logged in yet.");
    }
    if (groups == null || !groups.contains(gID) || groups.isEmpty()) {
      if (userID != null)
        System.out.println("sendMessageToGroup: User " + userID + " is not a member of group " + gID);
      return;
    }

    String message = promptMultiLineString(200, "message");
    try {
      query = "insert into messages values (?,?,?,?,?,?)";
      prepStatement = connection.prepareStatement(query);
    
    
      //java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
      // This is how you format a date so that you can use the setDate format below
      //java.sql.Date date_reg = new java.sql.Date (df.parse(dateOfBirth).getTime());
    
      prepStatement.setNull(1, Types.INTEGER);
      prepStatement.setString(2, userID);
      prepStatement.setString(3, message);
      prepStatement.setString(4, "");
      prepStatement.setString(5, gID);
      prepStatement.setDate(6, new java.sql.Date(System.currentTimeMillis()));
    
      prepStatement.executeUpdate();
      System.out.println("sendMessageToGroup: User " + userID + " has sent to group " + gID + " message: \"" + message + "\"");
    } catch (SQLException Ex) {
        System.out.println("sendMessageToGroup: User " + userID + " failed to send message to group " + gID);
        System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
        return;
    } finally {
      try {
        if (prepStatement != null) prepStatement.close();
      } catch (SQLException e) {
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
        return;
      }
    }

  }
  
  /**
  *Display all message history for a specific user
  *The messages from users and those from a group differs
  */
  private void displayMessages(Set<String> groups) {
	  PreparedStatement ps = null;
    try {
      String selectQuery = "SELECT * FROM MESSAGES WHERE TOUSERID=?";
  	  ps = connection.prepareStatement(selectQuery);
  	  ps.setString(1, userID);
      ResultSet rs = ps.executeQuery();	 
	  
      //get messages from other users
  	  String msg;
  	  int count = 1;
      System.out.println("\nHere is a list of messages from other users: ");
      while(rs.next()) {
		    System.out.println(count + ". " + rs.getDate("dateSent") + " " + rs.getString("fromID") + ": " + rs.getString("message"));
        count++;
      }
      //get messages from groups
      if (groups == null || groups.isEmpty()) {
        rs.close();
        return;
      }
      System.out.println("\nHere is a list of messages from the groups you are in: ");
      updateGroups(groups);
      for (String group : groups) {
        selectQuery = "select * from messages where toGroupID=?";
        ps = connection.prepareStatement(selectQuery);
        ps.setString(1, group);
        rs = ps.executeQuery(); 
        count = 1;
        System.out.println("\nMessages from group " + group + ": ");
        while (rs.next()) {
          System.out.println(count + ". " + rs.getDate("dateSent") + " " + rs.getString("fromID") + ": " + rs.getString("message"));
        }
      }
      


	  
      rs.close();
    } catch (SQLException Ex) {
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
    } 
    finally{
      try {
        if (ps != null) ps.close();
      } catch (SQLException e) {
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
      }
    }
  }
  

  private void displayNewMessages() {
  	PreparedStatement ps = null;
  	
  	try {
  	  int count = 1;

  	  // Setting up query
      String selectQuery = "SELECT messages.message, messages.fromID, messages.dateSent FROM messages natural join messagerecipient where messagerecipient.userID=?";
  	  ps = connection.prepareStatement(selectQuery);
  	  ps.setString(1, userID);
      ResultSet rs = ps.executeQuery();

  	  // Loop through and print results
      while (rs.next()) {
		    System.out.println(count + " " + rs.getDate("dateSent") + " " + rs.getString("fromID") + ": " + rs.getString("message"));
        count++;
      }

	  // cleaning up
      rs.close(); 
	  
    } catch (SQLException Ex) {
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
      return;
    } finally {
      try {
        if (ps != null) ps.close();
      } catch (SQLException e) {
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
        return;
      }
    }
  }

  // XXX: Finish implementation
  private void searchUsers() {
	// this should be done by searching the table ....
	// Otherwise, it will be a lot of work for the server ...
	PreparedStatement ps = null;
	
    try {

	  // Construct the start of the query

	  // Input
	  System.out.print("Enter a word: ");
	  String query = s.nextLine();

	  String[] queries = query.split(" ", 0);

      String baseQuery = "SELECT userID, name, email, date_of_birth FROM PROFILE " +
		"WHERE userID LIKE ? OR name LIKE ? OR email LIKE ? OR date_of_birth LIKE ?";

	  String selectQuery = baseQuery;
	  for(int count = 1; count < queries.length; count++) {
		selectQuery = selectQuery + " UNION " + baseQuery;
	  }
	  
	  // prepare
	  ps = connection.prepareStatement(selectQuery);
	  
	  // Attach input query and send

	  for(int count = 0; count < queries.length; count++) {
		ps.setString(1 + count * 4, queries[count]);
		ps.setString(2 + count * 4, queries[count]);
		ps.setString(3 + count * 4, queries[count]);
		ps.setString(4 + count * 4, queries[count]);
	  }
	  
      ResultSet rs = ps.executeQuery();

	  int count = 1;
      while(rs.next()) {
		System.out.println(count + " u:" + rs.getString("userID") + " name:" + rs.getString("name") + " e:" + rs.getString("email") + " birthday:" + rs.getDate("date_of_birth"));
        count++;
      }
	  
      rs.close();
    } catch (SQLException Ex) {
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
    } 
    finally{
      try {
        if (ps != null) ps.close();
      } catch (SQLException e) {
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
      }
    }	
  }
  
  private void threeDegrees(String userOne, String userTwo) {
    PreparedStatement ps = null;
	ResultSet rs = null;
	try {
	  String selectQuery = "SELECT x.id2, x.lev, x.pth FROM ( " +
		"SELECT mu.id2,	LEVEL as lev, SYS_CONNECT_BY_PATH(mu.id2, ' is friends with ') pth " +
		"FROM mutual mu WHERE LEVEL < 4 " +
		"START WITH mu.id1 = ? " +
		"CONNECT BY NOCYCLE PRIOR mu.id2 = mu.id1 " +
		") x " + 
		"WHERE id2=? " +
		"ORDER BY lev ASC " +
		"FETCH FIRST 1 ROWS ONLY";

	  ps = connection.prepareStatement(selectQuery);
	  ps.setString(1, userOne);
	  ps.setString(2, userTwo);
	  rs = ps.executeQuery();

	  if(rs.next()) {
		System.out.println(userOne + rs.getString(3) + ".");
	  }	else {
		System.out.println("There don't appear to be any close connections.");
	  }
    } catch (SQLException Ex) {
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
      return;
    } finally {
      try {
        if (ps != null) ps.close();
        if (rs != null) rs.close();
      } catch (SQLException e) {
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
        return;
      }
    }
  }

  private void tryThreeDegrees() {
	System.out.println("Which pair of users would you like to try and find a path between?");
	String userOne = promptString(20, "username");
	String userTwo = promptString(20, "username");
	threeDegrees(userOne, userTwo);
  }
  
  private void topMessages() {
    // input K
	System.out.print("Enter a number K to find the top K message-senders: ");
	
	int k = 1;
	boolean hasNat = false;
	
	do {
	  hasNat = s.hasNextInt();
	  if(hasNat) {
		k = s.nextInt();
		if(k <= 0) {
		  hasNat = false;
		  System.out.print("Enter a *positive* integer K: ");
		}
	  }
	  else 
		System.out.println("Enter an *integer* K: ");
	} while (!hasNat);

	s.nextLine(); // clears any extra excess
    
	try {	
	  Statement st = connection.createStatement(); //create an instance
	  String selectQuery = "SELECT FROMID, COUNT(*) AS MSGCOUNT " +
		"FROM MESSAGES GROUP BY FROMID " +
		"ORDER BY MSGCOUNT DESC " +
		"FETCH FIRST " + k + " ROWS ONLY";
	  ResultSet re = st.executeQuery(selectQuery);
	  
	  int i = 0; // count for the top k
	  while(re.next() && i < k) {
		System.out.println("#" + (++i) + " (" + re.getInt("MSGCOUNT") + " msgs.): " + re.getString("FROMID"));
	  }
	  if(i < k) {
		System.out.println("It doesn't look like there were that many message-senders ...");
	  }
	} catch (SQLException Ex) {
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
    } 
  }
  
  // public static void main(String args[]) {
  // 	// TEST VERSION
  // 	SocialPanther p = new SocialPanther();
  // 	p.chatMenu();
  // }

  /**
  *a function called to test individual methods
  *run @SocialPanther.sql before every test
  *@param methodName, the method name to be tested
  */
  public void testMethod(String methodName){
   if (methodName == null) return;

   if (methodName.equalsIgnoreCase("createUser")) {
      System.out.println("---Testing createUser---\n");
      createUser("1","1","abc","2000-08-01","abc123");
      //go to sqlplus, select * from profile;
   }

   if (methodName.equalsIgnoreCase("login")) {
       System.out.println("---Testing Login---\n");
       createUser("1","1","abc","2000-08-01","abc123");
       login("1","abc123");
       //Should display user found, logging in message
   }
   if (methodName.equals("sendMessageToUser")) {
      System.out.println("---Testing sendMessageToUser---\n");
      createUser("1","1","abc","2000-08-01","abc123");
      login("1","abc123");
      Set<String> friends = new TreeSet<String>();
      createUser("2","2","abc","2000-08-02","abc123");
      friends.add("2");
      if(sendMessageToUser("2",friends)){
      System.out.println("---Testing sendMessageToUser succedded---\n");
      } else {
      System.out.println("---Testing sendMessageToUser failed---\n");
      }
   }

   if (methodName.equals("initiateFriendship")) {
     System.out.println("---Testing initiateFriendship---\n");
     createUser("1","1","abc","2000-08-01","abc123");
     login("1","abc123");
     Set<String> friends = new TreeSet<String>();
     createUser("2","2","abc","2000-08-02","abc123");
     friends.add("3");
     if(initiateFriendship(friends)){
       System.out.println("---Testing initiateFriendship succedded---\n");
     } else {
       System.out.println("---Testing initiateFriendship failed---\n");
     }
   }

   if (methodName.equals("createGroup")) {
      System.out.println("---Testing createGroup---\n");
      createUser("1","1","abc","2000-08-01","abc123");
      login("1","abc123");
      createGroup("group1","1","This is group1",20);
   }

   if (methodName.equals("displayNewMessages")) {
      System.out.println("---Testing displayNewMessages---\n");
      createUser("1","1","abc","2000-08-01","abc123");
      login("1","abc123");
      Set<String> friends = new TreeSet<String>();
      createUser("2","2","abc","2000-08-02","abc123");
      friends.add("2");
      sendMessageToUser("2",friends);
      sendMessageToUser("2",friends);
      login("2","abc123");
      displayNewMessages();
   }

   if (methodName.equals("displayMessages")) {
      System.out.println("---Testing displayMessages---\n");
      createUser("1","1","abc","2000-08-01","abc123");
      login("1","abc123");
      //display messages from groups of user 1
      createGroup("group1","1","This is group1",20);
      Set<String> groups = new TreeSet<String>();
      groups.add("group1");
      sendMessageToGroup("group1",groups);
      displayMessages(groups);
      //display messages from friends
      Set<String> friends = new TreeSet<String>();
      createUser("2","2","abc","2000-08-02","abc123");
      friends.add("2");
      sendMessageToUser("2",friends);
      sendMessageToUser("2",friends);
      login("2","abc123");
      displayMessages(null);
   }

   if (methodName.equals("userInGroup")) {
      System.out.println("---Testing userInGroup---\n");
      createUser("1","1","abc","2000-08-01","abc123");
      login("1","abc123");
      createGroup("group1","1","This is group1",20);
      if (userInGroup("1","group1")) {
        System.out.println("---Testing userInGroup succecceded---");
      } else {
        System.out.println("---Testing userInGroup failed---");
      }
   }

   if (methodName.equalsIgnoreCase("sendMessageToGroup")) {
      System.out.println("---Testing userInGroup---\n");
      createUser("1","1","abc","2000-08-01","abc123");
      login("1","abc123");
      createGroup("group1","1","This is group1",20);
      Set<String> groups = new TreeSet<String>();
      groups.add("group1");
      sendMessageToGroup("group1",groups);
      //in sqlplus run "select * from messages;" and "select * from messagerecipient;"
      //should see one entry sent to 1
   }

   if (methodName.equalsIgnoreCase("logout")) {
      System.out.println("---Testing logOut---\n");
      createUser("1","1","abc","2000-08-01","abc123");
      login("1","abc123");
      Set<String> friends = new TreeSet<String>();
      createUser("2","2","abc","2000-08-02","abc123");
      friends.add("2");
      sendMessageToUser("2",friends);
      login("2","abc123");
      displayNewMessages();
      logOut();
      //select * from profile; user2's lastlogin should be current date
      //select * from message recipient; no userID of 2 shall be displayed
      System.out.println("userID == null: " + (userID == null));
   }

   if (methodName.equalsIgnoreCase("initiateAddingGroup")) {
      System.out.println("---Testing initiateAddingGroup---\n");
      createUser("1","1","abc","2000-08-01","abc123");
      createUser("2","2","abc","2000-08-02","abc123");
      login("1","abc123");
      createGroup("group1","1","This is group1",20);
      login("2","abc123");
      initiateAddingGroup("group1",null);
      //In sqlplus, select * from pendinggroupmembers; you should see user2 sending group1 a request
   }

   if (methodName.equalsIgnoreCase("confirmFriendships")) {
      System.out.println("---Testing confirmFriendships---\n");
      createUser("1","1","abc","2000-08-01","abc123");
      createUser("2","2","abc","2000-08-02","abc123");
      login("1","abc123");
      createGroup("group1","1","This is group1",20);
      login("2","abc123");
      Set<String> friends = new TreeSet<String>();
      friends.add("3");
      initiateFriendship(friends);
      initiateAddingGroup("group1",null);
      //In sqlplus, select * from pendinggroupmembers; you should see user2 sending group1 a request
      login("1","abc123");
      confirmFriendships();
   }

   if (methodName.equalsIgnoreCase("dropUser")) {
      System.out.println("---Testing dropUser---\n");
      createUser("1","1","abc","2000-08-01","abc123");
      login("1","abc123");
      Set<String> friends = new TreeSet<String>();
      createUser("2","2","abc","2000-08-02","abc123");
      friends.add("2");
      sendMessageToUser("2",friends);
      dropUser();
   }
  } 
  /**
  *given a userID and gID,
  *return true if userID is found in groupmembership where gID matches the argument
  *@param userId, the userID to search in group
  *@param gID, the group to search fot the userID
  *@return true if userID if found in the groupmembership table
  */
  private boolean userInGroup(String userId, String gID){
    PreparedStatement ps = null;
    try{
      String selectQuery = "select count(*) from groupmembership where userID=?";
      ps = connection.prepareStatement(selectQuery);
      ps.setString(1, userID);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        if(rs.getInt(1) > 0){
          rs.close();
          return true;
        }
      }
      rs.close();
      return false;
      
    } catch (SQLException Ex) {
      System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
      return false;
    } finally {
      try {
        if (ps != null) ps.close();
      } catch (SQLException e) {
        System.out.println("Cannot close Statement. Machine error: "+e.toString());
        return false;
      }
    }
  }

  public void close() throws SQLException {
	connection.close();
	s.close();
  }
  
  public static void main(String args[]) throws SQLException
  {
  	/* Making a connection to a DB causes certain exceptions.  In order to handle
  	   these, you either put the DB stuff in a try block or have your function
  	   throw the Exceptions and handle them later.  For this demo I will use the
  	   try blocks */
	
    String username, password;
	//username = "siz11"; //This is your username in oracle
	//password = "3877183"; //This is your password in oracle
	username = "mik91";
	password = "3945069";
	
	//This is the location of the database.  This is the database in oracle
	//provided to the class
	String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
	SocialPanther db = null;
	
    try{
      // Register the oracle driver.  
      DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
	  
  	  //Setup db
      db = new SocialPanther(url, username, password);
	  
	  db.mainMenu();
      //db.testMethod("createUser");
      //db.testMethod("login");
      //db.testMethod("sendMessageToUser");
      //db.testMethod("initiateFriendship");
      //db.testMethod("createGroup");
      //db.testMethod("displayNewMessages");
      //db.testMethod("userInGroup");
      //db.promptMultiLineString(200, "message");
      //db.testMethod("sendMessageToGroup");
      //db.testMethod("logout");
      //db.testMethod("displayMessages");
      //db.testMethod("initiateAddingGroup");
      //db.testMethod("confirmFriendships");
      //db.testMethod("dropUser");
    }
    catch(Exception Ex)  {
  	  System.out.println("Error connecting to database.  Machine Error: " +
  						 Ex.toString());
  	}
  	finally
  	{
  	  /*
  	   * NOTE: the connection should be created once and used through out the whole project;
  	   * Is very expensive to open a connection therefore you should not close it after every operation on database
  	   */
	  db.close();
  	}
  }
}
