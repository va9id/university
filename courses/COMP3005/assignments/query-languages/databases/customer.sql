CREATE TABLE Customer (
	C# VARCHAR(5) PRIMARY KEY, 
	Name VARCHAR(10) UNIQUE CHECK(Name NOT LIKE '%[^0-9]%'), 
	Age INTEGER CHECK(Age <= 120), 
	City VARCHAR(15) CHECK (City != lower(City))
);

INSERT INTO Customer (C#, Name, Age, City) VALUES ('C1', 'Adams', 20, 'London');
INSERT INTO Customer (C#, Name, Age, City) VALUES ('C2', 'Blake', 30, 'Paris');
INSERT INTO Customer (C#, Name, Age, City) VALUES ('C3', 'Clark', 25, 'Chicago');
INSERT INTO Customer (C#, Name, Age, City) VALUES ('C4', 'Last', 21, 'Ottawa');
INSERT INTO Customer (C#, Name, Age, City) VALUES ('C5', 'Smith', 30, 'Toronto');