begin    
    execute immediate
        'CREATE TABLE Customer (
            C# VARCHAR(5) PRIMARY KEY,
            Name VARCHAR(10),
            Age INTEGER,
            City VARCHAR(15))';
    execute immediate
   	    'INSERT INTO Customer (C#, Name, Age, City) VALUES (''C1'', ''Adams'', 20, ''London'')';
    execute immediate
   	    'INSERT INTO Customer (C#, Name, Age, City) VALUES (''C2'', ''Blake'', 30, ''Paris'')';
    execute immediate
   	    'INSERT INTO Customer (C#, Name, Age, City) VALUES (''C3'', ''Clark'', 25, ''Chicago'')';
    execute immediate
   	    'INSERT INTO Customer (C#, Name, Age, City) VALUES (''C4'', ''Last'', 20, ''Ottawa'')';
    execute immediate
   	    'INSERT INTO Customer (C#, Name, Age, City) VALUES (''C5'', ''Smith'', 30, ''Toronto'')';
end;
/