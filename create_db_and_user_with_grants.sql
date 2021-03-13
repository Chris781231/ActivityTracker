CREATE SCHEMA if NOT EXISTS `activitytracker` DEFAULT CHARACTER SET UTF8 COLLATE UTF8_HUNGARIAN_CI;

CREATE USER 'activitytracker'@'localhost' IDENTIFIED BY 'activitytracker';
GRANT ALL ON `activitytracker`.* TO `activitytracker`@`localhost`;