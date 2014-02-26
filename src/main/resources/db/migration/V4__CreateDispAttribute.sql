CREATE TABLE DispAttribute(
	idDispAttribute INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	idDispClass INT CONSTRAINT FK_DispClass REFERENCES DispClass(idDispClass),
	name VARCHAR(45),
	SQLType VARCHAR(45),
	formType VARCHAR(45),
	CONSTRAINT DispAttribute_primary PRIMARY KEY (idDispAttribute)
);
