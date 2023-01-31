-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema dailymoon
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema dailymoon
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `dailymoon` DEFAULT CHARACTER SET utf8 ;
USE `dailymoon` ;

-- -----------------------------------------------------
-- Table `dailymoon`.`file`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dailymoon`.`file` (
  `file_no` BIGINT NOT NULL,
  `file_name` VARCHAR(255) NOT NULL,
  `file_path` VARCHAR(255) NOT NULL,
  `original_file_name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`file_no`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `dailymoon`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dailymoon`.`member` (
  `user` BIGINT NOT NULL,
  `nickname` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `gender` CHAR(1) NULL DEFAULT NULL,
  `birth` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`user`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `dailymoon`.`diary`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dailymoon`.`diary` (
  `diary_no` BIGINT NOT NULL AUTO_INCREMENT,
  `user` BIGINT NOT NULL,
  `file_no` BIGINT NULL DEFAULT NULL,
  `feeling` ENUM('5', '4', '3', '2', '1') NOT NULL,
  `date` DATE NOT NULL,
  `detail` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`diary_no`),
  INDEX `fk_dialy_file1_idx` (`file_no` ASC) VISIBLE,
  INDEX `fk_dialy_member1_idx` (`user` ASC) VISIBLE,
  CONSTRAINT `fk_dialy_file`
    FOREIGN KEY (`file_no`)
    REFERENCES `dailymoon`.`file` (`file_no`),
  CONSTRAINT `fk_dialy_member`
    FOREIGN KEY (`user`)
    REFERENCES `dailymoon`.`member` (`user`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `dailymoon`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dailymoon`.`tag` (
  `tag_name` VARCHAR(10) NOT NULL,
  `category` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`tag_name`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `dailymoon`.`diary_has_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dailymoon`.`diary_has_tag` (
  `mapping_no` BIGINT NOT NULL AUTO_INCREMENT,
  `diary_no` BIGINT NOT NULL,
  `tag_name` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`mapping_no`),
  INDEX `fk_dialy_has_tag_tag1_idx` (`tag_name` ASC) VISIBLE,
  INDEX `fk_dialy_has_tag_dialy1_idx` (`diary_no` ASC) VISIBLE,
  CONSTRAINT `fk_dialy_has_tag_dialy`
    FOREIGN KEY (`diary_no`)
    REFERENCES `dailymoon`.`diary` (`diary_no`),
  CONSTRAINT `fk_dialy_has_tag_tag`
    FOREIGN KEY (`tag_name`)
    REFERENCES `dailymoon`.`tag` (`tag_name`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
