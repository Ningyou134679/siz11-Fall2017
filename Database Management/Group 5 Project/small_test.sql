--This file is the smaller one for testing Social@Panther.sql
insert into profile values(NULL,'Karla Golden','KarlaGolden@gmail.com','AiNg6ieTUu2phahY','11-JUN-99',TO_TIMESTAMP('04-NOV-17:12:07','DD-MON-RR:HH24:MI'));
insert into profile values(NULL,'Jazmin Sandoval','JazminSandoval@gmail.com','xak3Ip2OuChu8Vai','06-JAN-94',TO_TIMESTAMP('11-NOV-17:04:07','DD-MON-RR:HH24:MI'));
insert into profile values(NULL,'Aubrey Mccormick','AubreyMccormick@gmail.com','tah1DaikAijaey0c','04-APR-95',TO_TIMESTAMP('20-OCT-17:02:07','DD-MON-RR:HH24:MI'));

insert into friends values('1','2', '25-OCT-17', 'Hello!');
insert into friends values('1','3', '28-OCT-17', 'Hi, be my friend?');
insert into friends values('2','3', '06-NOV-17', 'Dont I remember you from bowling??');

commit;
