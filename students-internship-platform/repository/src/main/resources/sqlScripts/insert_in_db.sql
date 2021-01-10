DELETE FROM application;

DELETE FROM internship_announcement;

DELETE FROM account;

DELETE FROM user_authentication;

DELETE FROM role;

-- ROLE
INSERT INTO role (id, role) VALUES (1, 'APPLICANT');
INSERT INTO role (id, role) VALUES (2, 'RECRUITER');

-- UserAUTHENTICATION
INSERT INTO user_authentication (id, email, first_name, last_name, role_id, availability, status, education, work_history, volunteer_experience, domains_of_interest, hobbies, contact)
VALUES (1, 'applicant@yahoo.com', 'Patricia', 'Truta', 1, TRUE, 'single', 'faculty', '1 year', '1 year', 'JAVA, Python', 'volleyball', '0756084396');
INSERT INTO user_authentication (id, email, first_name, last_name, role_id, availability, status, education, work_history, volunteer_experience, domains_of_interest, hobbies, contact)
VALUES (2, 'recruiter@yahoo.com', 'Patricia', 'Truta', 2, TRUE, 'single', 'faculty', '1 year', '1 year', 'JAVA, Python', 'volleyball', '0756084396');


-- ANNOUNCEMENT
INSERT INTO internship_announcement (id, title, company, user_authentication_id, location, duration, domains, possibility_of_remote_work, paid_or_not, working_time, number_of_positions, requirements, availability_of_training_course, possibility_of_contract, benefits, needed_skills, start_date, end_date, posting_date, deadline, username, availability)
VALUES (1, 'Junior-Looking for a junior java developer', 'Generix Group Romania', 2, 'Cluj-Napoca', '3 months', 'JAVA', 'Possible to work remote', 1, 'full time', 4, '0 years of experience', 'Available of training courde', 'Possibility of contract after', 'Benefits: going to teambuildings', 'To have skills of Java, of OOP', '2021-05-15 12:00:00', '2021-08-15 18:00:00', '2021-01-16 18:00:00', '2021-02-16 18:00:00', 'applicant', 1);
INSERT INTO internship_announcement (id, title, company, user_authentication_id, location, duration, domains, possibility_of_remote_work, paid_or_not, working_time, number_of_positions, requirements, availability_of_training_course, possibility_of_contract, benefits, needed_skills, start_date, end_date, posting_date, deadline, username, availability)
VALUES (2, 'Internship-Looking for a junior python developer', 'Generix Group Romania', 2, 'Cluj-Napoca', '3 months', 'JAVA', 'Possible to work remote', 1, 'full time', 4, '0 years of experience', 'Available of training courde', 'Possibility of contract after', 'Benefits: going to teambuildings', 'To have skills of Java, of OOP', '2021-05-15 12:00:00', '2021-08-15 18:00:00', '2021-01-16 18:00:00', '2021-02-16 18:00:00', 'applicant', 1);

INSERT INTO internship_announcement (id, title, company, user_authentication_id, location, duration, domains, possibility_of_remote_work, paid_or_not, working_time, number_of_positions, requirements, availability_of_training_course, possibility_of_contract, benefits, needed_skills, start_date, end_date, posting_date, deadline, username, availability)
VALUES (3, 'Internship-Looking for a junior front-end developer', 'SDL Solutions', 2, 'Bucuresti', '1 months', 'JAVA', 'Possible to work remote', 1, 'full time', 4, '0 years of experience', 'Available of training courde', 'Possibility of contract after', 'Benefits: going to teambuildings', 'To have skills of Java, of OOP', '2021-05-15 12:00:00', '2021-08-15 18:00:00', '2021-01-16 18:00:00', '2021-02-16 18:00:00', 'applicant', 1);


INSERT INTO internship_announcement (id, title, company, user_authentication_id, location, duration, domains, possibility_of_remote_work, paid_or_not, working_time, number_of_positions, requirements, availability_of_training_course, possibility_of_contract, benefits, needed_skills, start_date, end_date, posting_date, deadline, username, availability)
VALUES (4, 'Internship-Looking for a freshly graduate passionate about web', 'SDL Solutions', 2, 'Bucuresti', '1 months', 'WEB', 'Possible to work remote', 1, 'part time', 4, '0 years of experience', 'Available of training courde', 'Possibility of contract after', 'Benefits: going to teambuildings', 'To have skills of Java, of OOP', '2021-07-15 12:00:00', '2021-09-15 18:00:00', '2021-01-16 18:00:00', '2021-02-16 18:00:00', 'applicant', 1);

-- APPLICATION
INSERT INTO application (id, user_authentication_id, internship_announcement_id)
VALUES (1, 1, 1);
INSERT INTO application (id, user_authentication_id, internship_announcement_id)
VALUES (2, 2, 2);

-- ACCOUNT
-- plaintext password: student
-- to generate bcrypt password goto link: https://www.devglan.com/online-tools/bcrypt-hash-generator
-- and select number of rounds: 6
INSERT INTO account (id, password, registration_date, username, user_authentication_id, active)
VALUES (1, '$2a$06$tkNXLd2Gm8mbdH.yp/n1gu4mf9ON3e0/eqMSTcCvaxlQdMmLqnt0y', '2021-01-09 17:25:19', 'applicant', 1, TRUE);
INSERT INTO account (id, password, registration_date, username, user_authentication_id, active)
VALUES (2, '$2a$06$.EHZS1/yEMkVovXJ.sN2XOQOEai01Awc0ykHHuirXBxOT3jr5iEXO', '2021-01-09 13:43:11', 'recruiter', 2, TRUE);