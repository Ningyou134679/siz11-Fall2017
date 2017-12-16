insert into profile values('a','Karla Golden','KarlaGolden@gmail.com','a','11-JUN-99',TO_TIMESTAMP('04-NOV-17:12:07','DD-MON-RR:HH24:MI'));
insert into profile values('b','Jazmin Sandoval','JazminSandoval@gmail.com','b','06-JAN-94',TO_TIMESTAMP('11-NOV-17:04:07','DD-MON-RR:HH24:MI'));
insert into profile values('c','Aubrey Mccormick','AubreyMccormick@gmail.com','c','04-APR-95',TO_TIMESTAMP('20-OCT-17:02:07','DD-MON-RR:HH24:MI'));
insert into profile values('d','Dino Man','RARR@gmail.com','d','04-APR-95',TO_TIMESTAMP('20-OCT-17:02:07','DD-MON-RR:HH24:MI'));
insert into profile values('e','Dino Man','RARR@gmail.com','e','04-APR-95',TO_TIMESTAMP('20-OCT-17:02:07','DD-MON-RR:HH24:MI'));
insert into profile values('f','Dino Man','RARR@gmail.com','f','04-APR-95',TO_TIMESTAMP('20-OCT-17:02:07','DD-MON-RR:HH24:MI'));

insert into friends values('b','a','04-JAN-15','no errors!!');
insert into friends values('a','d','04-JAN-15','no errors here');
insert into friends values('d','e','04-JAN-15','no');
insert into friends values('e','f','04-JAN-15','no');
insert into friends values('b','f','04-JAN-15','no');
insert into friends values('c','f','04-JAN-15','no');
insert into friends values('d','c','04-JAN-15','no');
insert into friends values('b','c','04-JAN-15','no');

commit;
