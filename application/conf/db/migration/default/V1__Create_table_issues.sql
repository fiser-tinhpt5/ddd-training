DROP TABLE IF EXISTS `issues`;
CREATE TABLE `issues` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(100) NOT NULL,
  `action` varchar(100) NOT NULL,
  `status` varchar(10) NOT NULL,
  `assignee` varchar(50) DEFAULT NULL,
  `deadline` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `issues`(`content`, `action`, `status`, `assignee`, `deadline`) VALUES ('ddd is difficult', 'learn more', 'PENDING', 'tinh_pt', '2016-11-24');
INSERT INTO `issues`(`content`, `action`, `status`, `assignee`, `deadline`) VALUES ('ddd is abstraction', 'do more', 'PENDING', 'tinh_pt', '2016-11-24');