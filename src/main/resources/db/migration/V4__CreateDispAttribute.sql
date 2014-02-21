CREATE TABLE DispAttribute(
	idDispAttribute INT PRIMARY KEY,
	idDispClass INT CONSTRAINT FK_DispClass REFERENCES DispClass(idDispClass),
	name VARCHAR(45),
	SQLType VARCHAR(45),
	formType VARCHAR(45)
);

INSERT INTO DispAttribute VALUES(1, 1, 'id', 'INT', 'text'),
(2, 1, 'name', 'VARCHAR(45)', 'text');