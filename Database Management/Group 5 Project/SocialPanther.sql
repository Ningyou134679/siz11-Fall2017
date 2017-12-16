DROP TABLE PROFILE CASCADE CONSTRAINTS;
DROP TABLE FRIENDS CASCADE CONSTRAINTS;
DROP TABLE PENDINGFRIENDS CASCADE CONSTRAINTS;
DROP TABLE MESSAGES CASCADE CONSTRAINTS;
DROP TABLE MESSAGERECIPIENT CASCADE CONSTRAINTS;
DROP TABLE GROUPS CASCADE CONSTRAINTS;
DROP TABLE GROUPMEMBERSHIP CASCADE CONSTRAINTS;
DROP TABLE PENDINGGROUPMEMBERS CASCADE CONSTRAINTS;
--DROP SEQUENCE PROFILE_ID_SEQ;
--DROP SEQUENCE GROUP_ID_SEQ;
DROP SEQUENCE MSG_ID_SEQ;

CREATE TABLE PROFILE(
userID        varchar2(20) not null deferrable,
name          varchar2(50),
email         varchar2(50),
password      varchar2(50),
date_of_birth date,
lastlogin     timestamp,
CONSTRAINT PROFILE_PK PRIMARY KEY(userID)
);

CREATE TABLE FRIENDS(
userID1       varchar2(20) not null deferrable,
userID2       varchar2(20) not null deferrable,
JDate         date,
message       varchar2(200),
CONSTRAINT FRIENDS_PK PRIMARY KEY(userID1, userID2) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT FRIENDS_FK1 FOREIGN KEY(userID1) REFERENCES PROFILE(userID) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT FRIENDS_FK2 FOREIGN KEY(userID2) REFERENCES PROFILE(userID) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT FRIENDS_CHECK1 CHECK(USERID1 <> USERID2)
);

CREATE TABLE PENDINGFRIENDS(
fromID       varchar2(20) not null deferrable,
toID         varchar2(20),
message      varchar2(200),
CONSTRAINT PENDINGFRIENDS_PK PRIMARY KEY(fromID, toID) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT PENDINGFRIENDS_FK1 FOREIGN KEY(fromID) REFERENCES PROFILE(userID) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT PENDINGFRIENDS_FK2 FOREIGN KEY(toID) REFERENCES PROFILE(userID) INITIALLY IMMEDIATE DEFERRABLE
);

CREATE TABLE GROUPS(
gID          varchar2(20) not null deferrable,
name         varchar2(50) not null deferrable,
mem_limit    number not null,
description  varchar2(200),
CONSTRAINT GROUPS_PK PRIMARY KEY(gID) INITIALLY IMMEDIATE DEFERRABLE
);

CREATE TABLE MESSAGES(
msgID        number not null deferrable,
fromID       varchar2(20) not null deferrable,
message      varchar2(200),
toUserID     varchar2(20),
toGroupID    varchar2(20),
dateSent     date,
CONSTRAINT MESSAGES_PK PRIMARY KEY(msgID) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT MESSAGES_FK1 FOREIGN KEY(fromID) REFERENCES PROFILE(userID) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT MESSAGES_FK2 FOREIGN KEY(toUserID) REFERENCES PROFILE(userID) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT MESSAGES_FK3 FOREIGN KEY(toGroupID) REFERENCES GROUPS(gID) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT msg_toUsr CHECK (toUserID is not null or toGroupID is not null)
);

CREATE TABLE MESSAGERECIPIENT(
msgID        number not null deferrable,
userID       varchar2(20) not null deferrable,
CONSTRAINT MESSAGERECIPENT_PK PRIMARY KEY(msgID, userID) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT MESSAGERECIPENT_FK1 FOREIGN KEY(msgID) REFERENCES MESSAGES(msgID) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT MESSAGERECIPENT_FK2 FOREIGN KEY(userID) REFERENCES PROFILE(userID) INITIALLY IMMEDIATE DEFERRABLE
);

CREATE TABLE GROUPMEMBERSHIP(
gID          varchar2(20) not null deferrable,
userID       varchar2(20) not null deferrable,
role         varchar2(20),
CONSTRAINT GROUPMEMBERSHIP_PK PRIMARY KEY(gID, userID) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT GROUPMEMBERSHIP_FK1 FOREIGN KEY(userID) REFERENCES PROFILE(userID) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT GROUPMEMBERSHIP_FK2 FOREIGN KEY(gID) REFERENCES GROUPS(gID) INITIALLY IMMEDIATE DEFERRABLE
);

CREATE TABLE PENDINGGROUPMEMBERS(
gID          varchar2(20) not null deferrable,
userID       varchar2(20) not null deferrable,
message      varchar2(200),
CONSTRAINT PENDINGGROUPMEMBERS_PK PRIMARY KEY(gID, userID) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT PENDINGGROUPMEMBERS_FK1 FOREIGN KEY(userID) REFERENCES PROFILE(userID) INITIALLY IMMEDIATE DEFERRABLE,
CONSTRAINT PENDINGGROUPMEMBERS_FK2 FOREIGN KEY(gID) REFERENCES GROUPS(gID) INITIALLY IMMEDIATE DEFERRABLE
);

--assumption1: Anyone can be friend with anyone
--assumption2: message in friends, pendingFriends, pendingGroupMembers are not count as a record in message because they don't hold an id
--assumption3: profile can send to another profile even if they are not friends (ex. direct message on Twitter)
--assumption4: a profile can send as many friend request to another profile
--assumption5: a profile can send as many group request to a group

/*
CREATE SEQUENCE PROFILE_ID_SEQ 
  START WITH 1 
  INCREMENT BY 1;

CREATE SEQUENCE GROUP_ID_SEQ
  START WITH 1
  INCREMENT BY 1;

CREATE OR REPLACE TRIGGER PROFILE_SET_ID
BEFORE INSERT ON PROFILE
FOR EACH ROW
 WHEN (new.userID IS NULL) 
BEGIN
  SELECT PROFILE_ID_SEQ.NEXTVAL
  INTO :new.userID
  FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER GROUP_SET_ID
BEFORE INSERT ON GROUPS
FOR EACH ROW
 WHEN (new.gID IS NULL) 
BEGIN
  SELECT GROUP_ID_SEQ.NEXTVAL
  INTO :new.gID
  FROM DUAL;
END;
/ 
*/


/**
*Auto increment msgID in the messages table when NULL is passed in for msgID
*/
CREATE SEQUENCE MSG_ID_SEQ 
  START WITH 1 
  INCREMENT BY 1;

CREATE OR REPLACE TRIGGER MSG_SET_ID
BEFORE INSERT ON MESSAGES
FOR EACH ROW
 WHEN (new.msgID IS NULL) 
BEGIN
  SELECT MSG_ID_SEQ.NEXTVAL
  INTO :new.msgID
  FROM DUAL;
END;
/


/**
*When a new entry is inserted to the mssages table, 
*insert msgID and userID to messagerecipient for displaying new message
*/
create or replace trigger msg_to_recipient
after insert on MESSAGES
for each row
when (new.toUserID is not NULL)
BEGIN
  insert into MESSAGERECIPIENT
  values (:new.msgID, :new.toUserID);
end;
/

/**
*Delete from pending friends when a user confirms friend request from another user, 
*or when a entry is inserted into the friends table
*/
create or replace trigger cleanup_friend_request
after insert on FRIENDS
for each row
BEGIN
  DELETE FROM PENDINGFRIENDS
  WHERE fromID=:new.userID2 AND toID=:new.userID1;
  DELETE FROM PENDINGFRIENDS
  WHERE toID=:new.userID2 AND fromID=:new.userID1;
END;
/

/**
*When an entry inserted into the friends table with the date being NULL, assign it the current date
*/
create or replace trigger set_date_friends
before insert on FRIENDS
for each row
when (new.Jdate is NULL)
BEGIN
  :new.Jdate := sysdate;
END;
/

create or replace trigger set_order_friends
before insert on FRIENDS
for each row
when (new.userID1 > new.userID2)
  declare temp varchar(20);
BEGIN
  temp:=:new.userID2;
  :new.userID2:=:new.userID1;
  :new.userID1:=temp;
END;
/
  
/**
*The msg_to_group trigger is created
*to accomodate the sendMessageToGroup requirement
*High level description: 
*   When a user sends a message to a group,
*   All members including the sender in the group shall be able to login,
*   and see the message sent by display new messages
*Low level description: 
*   When an entry is inserted into MESSAGES where toGroupID is not null
*   For all userID in GROUPMEMBERSHIP where gID matches the new toGroupID
*   insert the msgID and userID to MESSAGERECIPIENT
*/
create or replace trigger msg_to_group
after insert on MESSAGES
for each row
  when (new.toGroupID is not NULL)
DECLARE
  cursor userIDs is 
  select userID
  from GROUPMEMBERSHIP
  where gID=:new.toGroupID;
BEGIN
  for usrID in userIDs loop
    insert into MESSAGERECIPIENT
    values (:new.msgID, usrID.userID);
  end loop;
end;
/  

/**
*Trigger confirm_group_member is used when a group manager confirms a group request
*remove the entry from pendingGroupMembers when an entry is inserted into groupMemberShip with the same userID
*/
create or replace trigger confirm_group_member
after insert on GROUPMEMBERSHIP
for each row
BEGIN
  DELETE FROM PENDINGGROUPMEMBERS
  WHERE userID=:new.userID AND gID=:new.gID;
END;
/

create or replace view mutual as
  select userID1 as id1, userID2 as id2 from friends
  union
  select userID2 as id1, userID1 as id2 from friends;
