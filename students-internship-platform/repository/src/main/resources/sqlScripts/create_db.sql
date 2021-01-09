create database npst;

use npst;

create table account
(
  id bigint auto_increment
    primary key,
  password varchar(255) null,
  registration_date datetime null,
  active bit not null,
  username varchar(255) null,
  person_id bigint null
)
;

create index FKd9dhia7smrg88vcbiykhofxee
	on account (person_id)
;

create table application
(
	id bigint auto_increment
		primary key,
	end_date datetime null,
	start_date datetime null,
	total_price double null,
	person_id bigint null,
	room_id bigint null,
	application_status_id bigint null
)
;

create index FKh74dg5tgqi0nybfwudce2ha5g
	on application (person_id)
;

create index FKm3d0q9s1hos02eamx9wrsupaq
	on application (application_status_id)
;

create index FKq83pan5xy2a6rn0qsl9bckqai
	on application (room_id)
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


create table announcement
 (
  id bigint auto_increment
      primary key,
  address varchar(255) null,
  name varchar(255) null,
  chain_id bigint(20) null,
  announcement_facility_id bigint(20) null,
  active bit not null,
  deadline datetime null,
  description varchar(255) null,
  end_date datetime null,
  start_date datetime null,
  technologies varchar(255) null,
  title varchar(255) null,
  person_id bigint(20) null
)
;


create index FKkwoeal1hhe0a80a672sgpelhw
	on announcement (announcement_facility_id)
;

create index FKp1jtr0llhh9593spkfm9ggrxc
	on announcement (chain_id)
;

create table announcement_chain
(
	id bigint auto_increment
		primary key,
	person_id bigint null
)
;

create index FKtdpsy9gwugidtxav9rnxycw5a
	on announcement_chain (person_id)
;

alter table announcement
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

alter table announcement
	add constraint FKkwoeal1hhe0a80a672sgpelhw
foreign key (announcement_facility_id) references announcement_facility (id)
;

create table person
(
	id bigint auto_increment
		primary key,
	address varchar(255) null,
	birth_date datetime null,
	email varchar(255) null,
	first_name varchar(255) null,
	gender int null,
	last_name varchar(255) null,
	active bit not null,
	role_id bigint null
)
;

create index FKfqfeq5nokuewxxtb44t9lw012
	on person (role_id)
;

alter table account
	add constraint FKd9dhia7smrg88vcbiykhofxee
foreign key (person_id) references person (id)
;

alter table application
	add constraint FKh74dg5tgqi0nybfwudce2ha5g
foreign key (person_id) references person (id)
;

alter table announcement_chain
	add constraint FKtdpsy9gwugidtxav9rnxycw5a
foreign key (person_id) references person (id)
;

create table person_announcement
(
	person_id bigint not null,
	announcement_id bigint not null,
	constraint FKfemi28ugn9jb0imoe79ty4sxq
	foreign key (person_id) references person (id),
	constraint FKaeq0i4a5jgpawsdw2ytqh44u8
	foreign key (announcement_id) references announcement (id)
)
;

create index FKaeq0i4a5jgpawsdw2ytqh44u8
	on person_announcement (announcement_id)
;

create index FKfemi28ugn9jb0imoe79ty4sxq
	on person_announcement (person_id)
;

create table role
(
	id bigint auto_increment
		primary key,
	role varchar(255) null
)
;

alter table person
	add constraint FKfqfeq5nokuewxxtb44t9lw012
foreign key (role_id) references role (id)
;

create table room
(
	id bigint auto_increment
		primary key,
	capacity int not null,
	price double not null,
	number int not null,
	disabled bit not null,
	announcement_id bigint null,
	room_facility_id bigint null,
	constraint FKdosq3ww4h9m2osim6o0lugng8
	foreign key (announcement_id) references announcement (id)
)
;

create index FK6yqj2t3ul99mihaoksikptgmo
	on room (room_facility_id)
;

create index FKdosq3ww4h9m2osim6o0lugng8
	on room (announcement_id)
;

alter table application
	add constraint FKq83pan5xy2a6rn0qsl9bckqai
foreign key (room_id) references room (id)
;

create table room_facility
(
	id bigint auto_increment
		primary key,
	pet_friendly bit not null,
	smoking bit not null
)
;

alter table room
	add constraint FK6yqj2t3ul99mihaoksikptgmo
foreign key (room_facility_id) references room_facility (id)
;

