begin
    for C in (select * from Customer C) loop
   	    dbms_output.put_line('C#_Name_____Age_City');
   	    dbms_output.put_line(C.C#||' '||C.Name||' '||C.Age||' '||C.City);
   	    dbms_output.put_line('B#_Name____City');
        for B in (select B.* from Bank B, Account A where A.B#=B.B# and C.C#=A.C#) loop
            dbms_output.put_line(B.B#||' '||B.Name||' '||B.City);
   	    end loop;
        dbms_output.put_line(chr(1));
    end loop;
end;
/
