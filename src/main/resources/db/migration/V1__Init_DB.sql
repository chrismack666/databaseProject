drop table if exists hibernate_sequence;
drop table if exists reminder;
create table hibernate_sequence (
    next_val bigint) engine=MyISAM;
insert into hibernate_sequence
    values ( 6 );
create table reminder (
                          id integer not null,
                          date datetime,
                          text varchar(255),
                          primary key (id)) engine=MyISAM;
