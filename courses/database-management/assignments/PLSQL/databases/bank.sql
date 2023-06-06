begin
    execute immediate
        'CREATE TABLE Bank (
            B# VARCHAR(5) PRIMARY KEY,
            Name VARCHAR(10),
            City VARCHAR(15))';
    execute immediate
        'INSERT INTO Bank (B#, Name, City) VALUES (''B1'', ''England'', ''London'')';
    execute immediate
   	    'INSERT INTO Bank (B#, Name, City) VALUES (''B2'', ''America'', ''Chicago'')';
    execute immediate
   	    'INSERT INTO Bank (B#, Name, City) VALUES (''B3'', ''Royal'', ''Toronto'')';
    execute immediate
   	    'INSERT INTO Bank (B#, Name, City) VALUES (''B4'', ''France'', ''Paris'')';
end;
/