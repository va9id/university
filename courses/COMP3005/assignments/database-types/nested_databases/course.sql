create type Name_v as varray(5) of varchar(10);
/

create type Section_t as object (
        Name varchar(15),
        Instructor varchar(15),
        Students Name_v
);
/

create type Section_v as varray(5) of Section_t;
/

create table Course(
        Name varchar(10),
        Sections Section_v
);

insert into Course values ('PL', Section_v(Section_t('A', 'Jack', Name_v('Alan', 'Barb'))));
insert into Course values ('OS', Section_v(Section_t('A', 'Lisa', Name_v('Alan', 'Last'))));
insert into Course values ('DB', Section_v(Section_t('A', 'Jack', Name_v('Alan', 'Barb')), Section_t('B', 'Lisa', Name_v('Dora', 'Last'))));
