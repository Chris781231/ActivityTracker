CREATE TABLE `activities` (
	`id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`startTime` DATETIME NOT NULL,
	`activity_desc` VARCHAR(500) NOT NULL COLLATE 'utf8_hungarian_ci',
	`activity_type` ENUM('BIKING','HIKING','RUNNING','BASKETBALL') NOT NULL COLLATE 'utf8_hungarian_ci',
	PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8_hungarian_ci'
ENGINE=InnoDB
;

CREATE TABLE `track_point` (
	`id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`activity_id` INT(10) UNSIGNED NOT NULL,
	`time` DATETIME NOT NULL,
	`lat` DOUBLE(22,5) NOT NULL DEFAULT '0',
	`lon` DOUBLE(22,5) NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`) USING BTREE,
	INDEX `activity_id` (`activity_id`) USING BTREE,
	CONSTRAINT `track_point_ibfk_1` FOREIGN KEY (`activity_id`) REFERENCES `activitytracker`.`activities` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
COLLATE='utf8_hungarian_ci'
ENGINE=InnoDB
;
