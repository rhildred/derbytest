CREATE TABLE DispClass (
	idDispClass INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	name VARCHAR(45),
	CONSTRAINT DispClass_primary PRIMARY KEY (idDispClass)
);