drop table if exists actor;
drop table if exists prefecture;

create table if not exists actor (
  id int not null auto_increment,
  name varchar(30) not null,
  height smallint,
  blood varchar(2),
  birthday date,
  birthplace_id smallint,
  update_at timestamp(6) not null default current_timestamp(6) on update current_timestamp(6),
  primary key (id)
) engine = INNODB;


create table if not exists prefecture (
  id smallint not null,
  name varchar(6) not null,
  primary key (id)
) engine = INNODB;
