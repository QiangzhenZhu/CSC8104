-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(nextval('hibernate_sequence'), 'field-1');
-- insert into myentity (id, field) values(nextval('hibernate_sequence'), 'field-2');
-- insert into myentity (id, field) values(nextval('hibernate_sequence'), 'field-3');
INSERT INTO CUSTOMER (first_name,last_name,email,phone_number) VALUES ( 'zzq', 'zzq', 'bbb@gmail.com', '00000000000');
INSERT INTO CUSTOMER (first_name,last_name,email,phone_number) VALUES ( 'zqw', 'zqw', 'ccc@gmail.com', '00000000001');
INSERT INTO CUSTOMER (first_name,last_name,email,phone_number) VALUES ( 'zqx', 'zqx', 'ddd@gmail.com', '00000000002');

INSERT INTO HOTEL (name,phone_number,post_code) VALUES ( 'alihotel', '00000000010','a12345');
INSERT INTO HOTEL (name,phone_number,post_code) VALUES ( 'baihotel', '00000000020','a12346');
INSERT INTO HOTEL (name,phone_number,post_code) VALUES ( 'dyihotel', '00000000030','a12347');

INSERT INTO HOTEL_BOOKING (customer_id,booking_date,hotel_id) VALUES ( 1, '2022-11-15', 1);
INSERT INTO HOTEL_BOOKING (customer_id,booking_date,hotel_id) VALUES ( 2, '2022-11-16', 2);
INSERT INTO HOTEL_BOOKING (customer_id,booking_date,hotel_id) VALUES ( 3, '2022-11-17', 3);