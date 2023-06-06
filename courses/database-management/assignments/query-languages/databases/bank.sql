CREATE TABLE Bank (
    B# VARCHAR(5) PRIMARY KEY, 
    Name VARCHAR(10) NOT NULL,
    City VARCHAR(15) CHECK (City != upper(City))
);

INSERT INTO Bank (B#, Name, City) VALUES ('B1', 'England', 'London');
INSERT INTO Bank (B#, Name, City) VALUES ('B2', 'America', 'Chicago');
INSERT INTO Bank (B#, Name, City) VALUES ('B3', 'Royal', 'Toronto');
INSERT INTO Bank (B#, Name, City) VALUES ('B4', 'France', 'Paris');