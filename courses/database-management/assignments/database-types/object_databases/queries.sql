-- 1 
select C.Name, cast(multiset( 
    select value(ST).Name 
    from table(C.Sections) S, table(S.Students) ST) as Student_names)
as Students
from Course C;

-- 2
select deref(S.Instructor).Name as Instructor, cast(multiset(
    select distinct value(ST).Name
    from Course C1, table(C1.Sections) S1, table(S1.Students) ST
    where deref(S.Instructor).Name=deref(S1.Instructor).Name)
    as Student_names)
as Students
from (
    select distinct S.Instructor 
    from Course C, table(C.Sections) S
) S;

-- 3 
select deref(S.Instructor).Name as Instructor, cast(multiset(
    select distinct value(ST).Name
    from Course C1, table(C1.Sections) S1, table(S1.Students) ST, Person P, Person P1
    where deref(S.Instructor).Name=deref(S1.Instructor).Name and
    value(P).Name=value(ST).Name and
    value(P1).Name=deref(S.Instructor).Name and
    value(P).BirthDate.Month=value(P1).BirthDate.Month) as Student_names)
as Students
from (
    select distinct S.Instructor 
    from Course C, table(C.Sections) S
) S;

-- 4
select distinct value(ST).Name as Student
from Course C, table(C.Sections) S, table(S.Students) ST
where not exists (
    select * 
    from Course C1 
    where value(ST).Name not in (
        select value(ST1).Name 
        from table(C1.Sections) S1, table(S1.Students) ST1
    )
);
