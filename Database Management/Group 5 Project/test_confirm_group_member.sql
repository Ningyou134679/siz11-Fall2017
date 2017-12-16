--A test file to test out confirm_group_member trigger in SocialPanther.sql

insert into profile values ('1', 'user1', 'abc', 'psw1', sysdate, current_timestamp);
insert into groups values ('group1', 'g1', 100, 'This is group 1');
insert into pendinggroupmembers values ('group1', '1', 'Hi');
insert into GROUPMEMBERSHIP values ('group1', '1', 'manager');

commit;

--Should not see any row displayed
select * from pendinggroupmembers;
--Should see user 1 in group group1
select * from GROUPMEMBERSHIP;