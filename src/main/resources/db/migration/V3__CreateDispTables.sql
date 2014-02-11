-- -----------------------------------------------------
-- Table `disptest`.`DispClass`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `disptest`.`DispClass` (
  `idDispClass` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idDispClass`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `disptest`.`DispAttribute`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `disptest`.`DispAttribute` (
  `idDispAttribute` INT NOT NULL AUTO_INCREMENT,
  `idDispClass` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `SQLtype` VARCHAR(45) NOT NULL,
  `formType` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idDispAttribute`, `idDispClass`),
  INDEX `fk_DispAttribute_DispClass_idx` (`idDispClass` ASC),
  CONSTRAINT `fk_DispAttribute_DispClass`
    FOREIGN KEY (`idDispClass`)
    REFERENCES `disptest`.`DispClass` (`idDispClass`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
