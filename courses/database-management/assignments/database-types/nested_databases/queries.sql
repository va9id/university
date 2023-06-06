-- 1
select C.Name, cast(multiset(
    select S.column_value
    from table(C.Sections) SC, table(SC.Students) S)
    as Name_v) as course_students    
from Course C;

-- 2
select S.Instructor, cast(multiset(
    select distinct ST.column_value
    from Course C, table(C.Sections) S1, table(S1.Students) ST
    where S.Instructor=S1.Instructor) as  Name_v) as Students
from (
    select distinct S.Instructor from Course C1, table(C1.Sections) S
) S;

-- 3
select S.Instructor, cast(multiset(
    select distinct ST.column_value    
    from Course C, table(C.Sections) S1, table(S1.Students) ST, 
    Person P, Person P1
    where S.Instructor=S1.Instructor and
    P.Name=ST.column_value and
    P1.Name=S.Instructor and
    P.BirthDate.Month=P1.BirthDate.Month) as Name_v) as Students
from (
    select distinct S.Instructor
    from Course C1, table(C1.Sections) S
) S;

-- 4
select distinct A1.column_value as Student
from Course C1, table(C1.Sections) S1, table(S1.Students) A1
where not exists (
    select * 
    from Course C 
    where A1.column_value not in (
        select SS.column_value 
        from table(C.Sections) S, table(S.Students) SS
    )
);

