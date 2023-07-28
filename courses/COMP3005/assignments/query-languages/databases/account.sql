CREATE TABLE Account (
    C# VARCHAR(5), 
    B# VARCHAR(5), 
	Balance INTEGER DEFAULT 1000 CHECK(Balance >= 0),
	PRIMARY KEY(C#, B#),
    FOREIGN KEY (C#) REFERENCES Customer(C#) 
        ON DELETE CASCADE,
    FOREIGN KEY (B#) REFERENCES Bank(B#) 
    ON DELETE CASCADE
);

INSERT INTO Account (C#, B#, Balance) VALUES ('C1', 'B1', 1000);
INSERT INTO Account (C#, B#, Balance) VALUES ('C1', 'B2', 2000);
INSERT INTO Account (C#, B#, Balance) VALUES ('C1', 'B3', 3000);
INSERT INTO Account (C#, B#, Balance) VALUES ('C1', 'B4', 4000);
INSERT INTO Account (C#, B#, Balance) VALUES ('C2', 'B1', 2000);
INSERT INTO Account (C#, B#, Balance) VALUES ('C2', 'B2', 3000);
INSERT INTO Account (C#, B#, Balance) VALUES ('C2', 'B3', 4000);
INSERT INTO Account (C#, B#, Balance) VALUES ('C3', 'B1', 3000);
INSERT INTO Account (C#, B#, Balance) VALUES ('C3', 'B2', 4000);
INSERT INTO Account (C#, B#, Balance) VALUES ('C4', 'B1', 4000);
INSERT INTO Account (C#, B#, Balance) VALUES ('C4', 'B2', 5000);