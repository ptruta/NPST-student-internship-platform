use npst;
create table account
(
  id bigint auto_increment
    primary key,
  password varchar(255) null,
  registration_date datetime null,
  active bit not null,
  username varchar(255) null,
  user_authentication_id bigint null
)
;

create index FKd9dhia7smrg88vcbiykhofxee
	on account (user_authentication_id)
;

create table application
(
	id bigint auto_increment
		primary key,
	user_authentication_id bigint null,
	application_status_id bigint null
)
;

create index FKh74dg5tgqi0nybfwudce2ha5g
	on application (user_authentication_id)
;

create index FKm3d0q9s1hos02eamx9wrsupaq
	on application (application_status_id)
;

create table application_status
(
	id bigint auto_increment
		primary key,
	status varchar(255) null
)
;

alter table application
	add constraint FKm3d0q9s1hos02eamx9wrsupaq
foreign key (application_status_id) references application_status (id)
;

create table internshipAnnouncement
(
	id bigint auto_increment
		primary key,
	title varchar(255) null,
	company varchar(255) null,
	location varchar(255) null,
	duration varchar(255) null,
	start_date date null,
	end_date date null,
	posting_date date null,
	deadline date null,
	username varchar(255) null,
    domains varchar(255) null,
    paid_or_not bit not null,
    working_time varchar(255) null,
	announcement_facility_id bigint null,
	possibility_of_remote_work varchar(255) null,
	number_of_positions bigint null,
	requirements varchar(255) null,
	availability_of_training_course varchar(255) null,
	possibility_of_contract varchar(255) null,
	benefits varchar(255) null,
	availability bit not null,
	needed_skills varchar(255) null,
	chain_id bigint null
)
;

create index FKkwoeal1hhe0a80a672sgpelhw
	on internship_announcement (announcement_facility_id)
;

create index FKp1jtr0llhh9593spkfm9ggrxc
	on internship_announcement (chain_id)
;

create table announcement_chain
(
	id bigint auto_increment
		primary key,
	user_authentication_id bigint null
)
;

create index FKtdpsy9gwugidtxav9rnxycw5a
	on announcement_chain (user_authentication_id)
;

alter table internship_announcement
	add constraint FKp1jtr0llhh9593spkfm9ggrxc
foreign key (chain_id) references announcement_chain (id)
;

create table announcement_facility
(
	id bigint auto_increment
		primary key,
	pictures varchar(255) null
)
;

create table user_authentication
(
	id bigint auto_increment
		primary key,
	email varchar(255) null,
	first_name varchar(255) null,
	last_name varchar(255) null,
	status varchar(255) null,
	education varchar(255) null,
	work_history varchar(255) null,
	volunteer_experience varchar(255) null,
	domains_of_interest varchar(255) null,
	availability bit not null,
	hobbies varchar(255) null,
	contact varchar(255) null,
	role_id bigint null
)
;

create index FKfqfeq5nokuewxxtb44t9lw012
	on user_authentication (role_id)
;

alter table account
	add constraint FKd9dhia7smrg88vcbiykhofxee
foreign key (user_authentication_id) references user_authentication (id)
;

alter table application
	add constraint FKh74dg5tgqi0nybfwudce2ha5g
foreign key (user_authentication_id) references user_authentication (id)
;

alter table announcement_chain
	add constraint FKtdpsy9gwugidtxav9rnxycw5a
foreign key (user_authentication_id) references user_authentication (id)
;

create table user_authentication_announcement
(
	user_authentication_id bigint not null,
	internship_announcement_id bigint not null,
	constraint FKfemi28ugn9jb0imoe79ty4sxq
	foreign key (user_authentication_id) references user_authentication (id),
	constraint FKaeq0i4a5jgpawsdw2ytqh44u8
	foreign key (internship_announcement_id) references internship_announcement (id)
)
;

create index FKaeq0i4a5jgpawsdw2ytqh44u8
	on user_authentication_announcement (internship_announcement_id)
;

create index FKfemi28ugn9jb0imoe79ty4sxq
	on user_authentication_announcement (user_authentication_id)
;

create table role
(
	id bigint auto_increment
		primary key,
	role varchar(255) null
)
;

alter table user_authentication
	add constraint FKfqfeq5nokuewxxtb44t9lw012
foreign key (role_id) references role (id)
;