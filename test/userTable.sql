DROP TABLE IF EXISTS `user`;

-- Table `user`
CREATE TABLE `user` (
	`id` VARCHAR(32) NOT NULL,
	`password` VARCHAR(32) NULL,
	`email` VARCHAR(64) NULL,
	`nickname` VARCHAR(64) NULL,
	`gender` CHAR(1) NULL,
	`timestamp` DATETIME NOT NULL,
	PRIMARY KEY(`id`)
) ENGINE = InnoDB;
