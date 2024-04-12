SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE event;
truncate table user ;

SET FOREIGN_KEY_CHECKS = 1;



insert into event(id,name,date,available_attendees_count,event_description,current_attendee_count,category)values
(10, 'SWIT',CURRENT_DATE, 100, 'Tech event for women',20,Category),
(11, 'houseparty',CURRENT_DATE,50, 'party for vibes alone',13,Category);







insert into user(id,name,password,email)  values
(20,'Qudus','qudus@12','adeshina1@gmail'),
(21,'Ope','ope@12','ope11@gmail');