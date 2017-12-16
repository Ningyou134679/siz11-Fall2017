# CS1555-Term-Project
GroupID = 5
Group repository for the CS1555 term project, Fall 2017  
* Login into unixs in terminal with the command `ssh <id>@unixs.cis.pitt.edu`
* run the following line `source /afs/pitt.edu/home/p/a/panos/1555/bash.env.unixs`
* copy `SocialPanther.java` and `SocialPanther.sql` to unixs
* Run `sqlplus`, enter id for username and peoplesoft number for password, run `@SocialPanther.sql`
* Change the username and password in the `main` method in `SocialPanther.java` to your username and password.
* In unixs, compile the file `javac SocialPanther.java` and run `java SocialPanther`
* Some expected behaviors of the program that may not be included in the requirement: 
   1. createUser
      1. User is prompted for userID, email address and date of birth 
      2. User can optionally enter username and/or password, which defaults to userID and "abc123"
   2. confirmFriendShip
      1. When user1 sends user2 a friend request, and user2 confirms it. user1's friend list does not automatically update until user selects display friends. Therefore, even if user2 confirms user1's friend request, user1 cannot send user2 a message until after user1 select display friendship.
      2. Any friend request that is not confirmed is not removed automatically, but the user will be prompt for options
   3. sendMessageToGroup
      1. When a user sends a group a message, every group member INCLUDING the sender can view the message when selecting display new messages. New messages are cleaned up when user logs out. (approved by instructor)
   4. sendMessageToUser/Group
      1. Should execute display friends or list groups beforehand to update the local lost from the database. (In order to not make too many queries, friends and groups are not updated per message sent)
      2. When user is prompt to enter a message, in order to accomodate for requirement 8 and 9, which says " the user should be prompted to enter the text of the message, which could be multi-lined". The user is first prompted with how many lines they want the message to have. Then they will enter input for n number of lines. If the final result exceeds the range (200 characters for message) then they wll be prompted to enter again.
* `SocialPanther.sql` Description:
   * Trigger
      * MSG_SET_ID
         * Auto increment msgID in the `messages` table when NULL is passed in for msgID
      * msg_to_recipient
         * When a new entry is inserted to the `mssages` table, insert msgID and userID to `messagerecipient` for displaying temporary message
         * Can be tested by running `@test_msg_to_recipient.sql`
      * cleanup_friend_request
         * Delete from pending friends when a user confirms friend request from another user, or when a entry is inserted into the `friends` table
      * set_date_friends
         * When an entry inserted into the `friends` table with the date being NULL, assign it the current date
      * msg_to_group
         * High-level description: When a user sends a group a message, the message is delivered to all members in the group in `messagerecipient`. 
         * Low-level description: When an entry is inserted into `MESSAGES` where toGroupID is not null. For all userID in `GROUPMEMBERSHIP` where gID matches the new toGroupID, insert the msgID and userID to `MESSAGERECIPIENT`.
         * Can be tested by running `@testMsgToGroup.sql`
      * confirm_group_member
         * When a group manager confirms a group request, delete the group request from pendingGroupMembers table
         * when an entry is being inserted into groupMemberShip, entries from pendingGroupMembers are removed with the same userID.
         * Can be tested by running `@test_confirm_group_member.sql`

* For benchmark and testing
   1. Remember to run `@SocialPanther.sql` before any testing
   2. Default test user has a userID of `1` and password of `abc123`, if database has a user with the same userID but different password, login will fail and test case will fail
   3. For unit test on methods: there is a method called `testMethod(String methodname)`, which takes a methodname, and run the corresponding test. (ex. in main method, call db.testMethod("sendMessageToUser");)
      1. Currently the method supports "createUser", "login", "sendMessageToUser", "initiateFriendship", "createGroup", "displayNewMessages", "displayMessages", "userInGroup", "sendMessageToGroup", "logout", "initiateAddingGroup", "confirmFriendships", "dropUser".
      2. Some of them will display success or fail, others will display result in the teminal, or need to run some commands in sqlplus, for more details see comments and result within each unit test.
      3. if a method name does not appear in the list, it does not mean that the method is not implemented, this test method is only used for the creater of testMethod, and the methods that creater implemented, but not any other members or their methods in the group.
      4. Any method that is not in the list may require manual testing, by running `db.mainMenu();` in the main method and enter commands.
   4. For test on triggers (again remember to run `@SocialPanther.sql` before any testing): 
      1. Trigger msg_to_recipient can be tested with `@test_msg_to_recipient.sql`
      2. Trigger msg_to_group can be tested with `@testMsgToGroup.sql`
      3. Trigger confirm_group_member can be tested with `@test_confirm_group_member.sql`
      4. Others may require manual testing, see the Trigger section from above and comments in SocialPanther.sql for more details 
