-- 1
select B.name 
from Bank B, Customer C, Account A 
where B.B# = A.B# and A.C# = C.C# and C.name = 'Last'; 

-- 2
select C.name 
from Customer C, Bank B, Account A 
where C.C#=A.C# and A.B#=B.B# and B.name='Royal'; 

-- 3
select distinct C.name 
from Customer C, Account A 
where C.C#=A.C# and A.Balance < 3000;

-- 4
select C.name as CName, B.name as BName 
from Customer C, Bank B, Account A 
where C.C#=A.C# and A.B#=B.B#; 

-- 5
select C.name 
from Customer C 
where not exists (
    select * 
    from Account A 
    where A.C#=C.C#
);

-- 6
select C.name 
from Customer C 
where not exists (
    select * 
    from Bank B 
    where not exists (
        select * 
        from Account A 
        where A.B#=B.B# and A.C#=C.C#
    )
); 

-- 7
select C.Name 
from Customer C 
where not exists (
    select * 
    from Bank B 
    where (
        B.Name!='France' or exists (
            select * 
            from Account A 
            where C.C#=A.C# and B.B#=A.B#
        )
    ) and (
        B.Name='France' or not exists (
            select * 
            from Account A 
            where C.C#=A.C# and B.B#=A.B#
        )
    )
);

-- 8
select C1.Name 
from Customer C1 
where C1.name != 'Clark' and not exists (
    select B.B# 
    from Bank B, Customer C, Account A 
    where C.name = 'Clark' and A.C#=C.C# and A.B#=B.B#
    MINUS
    select B.B# 
    from Bank B, Account A 
    where C1.C#=A.C# and A.B#=B.b#
); 


-- 9
select C1.name 
from Customer C1, Customer C
where C1.name != 'Clark' and C.name = 'Clark' and not exists (
    select * 
    from Bank B 
    where not exists (
        select * 
        from Account A, Account A1
        where C.C#=A.C# and A.B#=B.B# and C1.C#=A1.C# and A1.B#=B.B#
    ) and exists (
        select * 
        from Account A
        where (C.C#=A.C# and A.B#=B.B#) or (C1.C#=A1.C# and A1.B#=B.B#)
    )
);

-- 10
select C.Name 
from Customer C, Account A 
where A.C#=C.C# 
group by C.Name 
having COUNT(A.B#) > 2;