declare
    cust Customer%rowtype;
    name Customer.name%type;
    cursor C (n Customer.name%type) is
   	select * from Customer where Customer.name=n;
begin
    name := '&name';
    open C (name);
    loop
   	    fetch C into cust;
        exit when C%NOTFOUND;
   	    dbms_output.put_line(cust.C#||' _ '||cust.Name||' _ '||cust.Age||' _ '||cust.City);
   	    for B in (select B.* from Bank B, Account A where cust.C#=A.C# and B.B#=A.B#) loop
   		    dbms_output.put_line(B.B#||' _ '||B.Name||' _ '||B.City);
   	    end loop;
    end loop;
    close C;
end;
/
