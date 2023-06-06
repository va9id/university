create type Student_names as varray(5) of varchar(15);
/

create type Name_v as varray(5) of ref Person_t;
/

create type Section_t as object (
    	Name varchar(15),
    	Instructor ref Person_t,
    	Students  Name_v
);
/

create type Section_v as varray(5) of Section_t;
/

create table Course (
    	Name varchar(10),
    	Sections Section_v
);


insert into Course values (
	'PL',
	Section_v(Section_t('A',
	(select ref(P) from Person P where P.Name='Jack'),
	cast(multiset(
			select ref(P) from Person P
			where P.Name='Alan' or P.Name='Barb'
	) as Name_v ))));

insert into Course values (
	'OS',
	Section_v(Section_t('A',
	(select ref(P) from Person P where P.Name='Lisa'),
	cast(multiset(
			select ref(P) from Person P
			where P.Name='Alan' or P.Name='Last'
	) as Name_v ))));

insert into Course values (
	'DB',
	Section_v(Section_t('A',
	(select ref(P) from Person P where P.Name='Jack'),
	cast(multiset(
			select ref(P) from Person P
			where P.Name='Alan' or P.Name='Barb'
	) as Name_v )),
	Section_t('B',
	(select ref(P) from Person P where P.Name='Lisa'),
	cast(multiset(
			select ref(P) from Person P
			where P.Name='Dora' or P.Name='Last'
	) as Name_v))));

