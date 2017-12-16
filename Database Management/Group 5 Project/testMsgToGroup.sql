/**
*A file to test out the msg_to_group trigger
*High level description: 
*   When a user sends a message to a group,
*   All members including the sender in the group shall be able to login,
*   and see the message sent by display new messages
*Low level description: 
*   When an entry is inserted into MESSAGES where toGroupID is not null
*   For all userID in GROUPMEMBERSHIP where gID matches the new toGroupID
*   insert the msgID and userID to MESSAGERECIPIENT
*/

insert into profile values ('1', 'user1', 'abc', 'psw1', sysdate, current_timestamp);
insert into profile values ('2', 'user2', 'abc', 'psw1', sysdate, current_timestamp);
insert into groups values ('group1', 'group1', 100, 'custom group1');
insert into groupmembership values ('group1','1',NULL);
insert into groupmembership values ('group1','2',NULL);
insert into messages values (NULL, '1', 'a', NULL, 'group1', sysdate);

commit;

select * from MESSAGERECIPIENT;