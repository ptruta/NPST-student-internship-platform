DELETE FROM application;

DELETE FROM internshipAnnouncement;

DELETE FROM account;

DELETE FROM person;

DELETE FROM role;

-- ROLE
INSERT INTO role (id, role) VALUES (1, 'STUDENT');
INSERT INTO role (id, role) VALUES (2, 'COMPANY');
INSERT INTO role (id, role) VALUES (3, 'ADMIN');

-- PERSON
INSERT INTO person (id, address, birth_date, email, first_name, gender, last_name, active, role_id)
VALUES (1, 'Cluj', '2009-04-20 00:00:00', 'student@yahoo.com', 'First', 1, 'Student', TRUE, 1);
INSERT INTO person (id, address, birth_date, email, first_name, gender, last_name, active, role_id)
VALUES (2, 'Cluj', '2009-09-15 00:00:00', 'company@yahoo.ro', 'First', 1, 'Company', TRUE, 2);
INSERT INTO person (id, address, birth_date, email, first_name, gender, last_name, active, role_id)
VALUES (3, 'Oradea', '2010-01-24 00:00:00', 'dianadenisa25@gmail.ro', 'Diana', 1, 'Truta', TRUE, 3);

-- ANNOUNCEMENT
INSERT INTO internshipAnnouncement (id, title, description, technologies, start_date, end_date, deadline, active, person_id)
VALUES (1, 'Junior', 'Looking for a junior java developer', 'SpringBoot, JDK11', '2018-09-15 00:00:00',
        '2018-09-21 00:00:00', '2018-09-14 00:00:00', TRUE, 2);
INSERT INTO internshipAnnouncement (id, title, description, technologies, start_date, end_date, deadline, active, person_id)
VALUES (2, 'Senior', 'Looking for a senior java developer', 'SpringBoot, JDK11, GraphQL', '2019-09-15 00:00:00',
        '2019-09-21 00:00:00', '2018-09-14 00:00:00', TRUE, 2);

-- APPLICATION
INSERT INTO application (id, person_id, announcement_id)
VALUES (1, 1, 1);
INSERT INTO application (id, person_id, announcement_id)
VALUES (2, 1, 2);

-- ACCOUNT
INSERT INTO account (id, password, registration_date, username, person_id, active)
VALUES (1, '$2a$10$GsWW1F2uTh5pGcR13Ryi2uElEEwSZHod34/eqPJq5nubKId80QzCy', '2017-09-19 17:25:19', 'student', 1, TRUE);
INSERT INTO account (id, password, registration_date, username, person_id, active)
VALUES (2, '$2a$10$j60zcceESnCobXMyV4CoaOi7yg15Amn3Q1jK09.fk1nVbkRrT/S7m', '2017-09-20 13:43:11', 'company', 2, TRUE);
INSERT INTO account (id, password, registration_date, username, person_id, active)
VALUES (3, '$2a$10$LBxobmOqWT8xZaLLkZlKx.CTwn4Nl4SuYclEsE6zVQ2Aj.l4nYgoe', '2017-09-20 10:59:01', 'admin', 3, TRUE);
