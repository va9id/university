create type BirthDate_t as object (
    	Year int,
    	Month int
);
/
create type Person_t as object (
    	Name varchar(15),
    	BirthDate BirthDate_t
) not final;
/

create table Person of Person_t;

insert into Person values ('Jack', BirthDate_t(1972, 1));
insert into Person values ('Lisa', BirthDate_t(1980, 5));
insert into Person values ('Alan', BirthDate_t(2000, 1));
insert into Person values ('Barb', BirthDate_t(2000, 1));
insert into Person values ('Last', BirthDate_t(2000, 5));
insert into Person values ('Dora', BirthDate_t(2000, 5));
insert into Person values ('Evan', BirthDate_t(1999, 3));
insert into Person values ('Kate', BirthDate_t(1985, 4));