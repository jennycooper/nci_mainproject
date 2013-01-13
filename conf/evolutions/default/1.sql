# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table guest (
  guest_id                  bigint not null,
  name                      varchar(255),
  email                     varchar(255),
  country                   varchar(255),
  constraint pk_guest primary key (guest_id))
;

create table res_costs (
  costs_id                  bigint not null,
  deposit_paid              double,
  date_paid                 timestamp,
  pay_method                varchar(255),
  constraint pk_res_costs primary key (costs_id))
;

create table resdetail (
  detail_id                 bigint not null,
  room_num                  integer,
  res_reservation_id        bigint,
  constraint pk_resdetail primary key (detail_id))
;

create table reserve (
  reservation_id            bigint not null,
  checkin                   timestamp,
  checkout                  timestamp,
  num_adults                integer,
  num_child                 integer,
  pickup_time               varchar(255),
  dropoff_time              varchar(255),
  pickup                    varchar(255),
  dropoff                   varchar(255),
  room_rate                 double,
  meal_adult                double,
  meal_child                double,
  transfer                  double,
  whale_rate                double,
  notes                     varchar(255),
  my_guest_guest_id         bigint,
  costs_costs_id            bigint,
  updtime                   timestamp not null,
  constraint pk_reserve primary key (reservation_id))
;

create table account (
  email                     varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255),
  role                      varchar(255),
  constraint pk_account primary key (email))
;

create sequence guest_seq;

create sequence res_costs_seq;

create sequence resdetail_seq;

create sequence reserve_seq;

create sequence account_seq;

alter table resdetail add constraint fk_resdetail_res_1 foreign key (res_reservation_id) references reserve (reservation_id) on delete restrict on update restrict;
create index ix_resdetail_res_1 on resdetail (res_reservation_id);
alter table reserve add constraint fk_reserve_myGuest_2 foreign key (my_guest_guest_id) references guest (guest_id) on delete restrict on update restrict;
create index ix_reserve_myGuest_2 on reserve (my_guest_guest_id);
alter table reserve add constraint fk_reserve_costs_3 foreign key (costs_costs_id) references res_costs (costs_id) on delete restrict on update restrict;
create index ix_reserve_costs_3 on reserve (costs_costs_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists guest;

drop table if exists res_costs;

drop table if exists resdetail;

drop table if exists reserve;

drop table if exists account;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists guest_seq;

drop sequence if exists res_costs_seq;

drop sequence if exists resdetail_seq;

drop sequence if exists reserve_seq;

drop sequence if exists account_seq;

