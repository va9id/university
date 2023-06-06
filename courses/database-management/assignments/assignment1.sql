CREATE TABLE test (
	object VARCHAR(10) PRIMARY KEY,
	dependent VARCHAR(10), 
    FOREIGN KEY (dependent) REFERENCES test(object)
		ON DELETE CASCADE
);

INSERT INTO test(object) VALUES ('o1');
INSERT INTO test(object) VALUES ('o2');
INSERT INTO test(object) VALUES ('o3');

UPDATE test SET dependent='o2' WHERE object='o1'; 
UPDATE test SET dependent='o3' WHERE object='o2'; 
UPDATE test SET dependent='o1' WHERE object='o3'; 