# 题型
CREATE TABLE IF NOT EXISTS `question_type` (
    `id` bigint unsigned not null auto_increment COMMENT 'ID',
    `name` varchar(30) not null comment '题型名称',
    `sort_num` int unsigned not null default 0 comment '排序号',
    `enabled` tinyint(1) unsigned not null default 1 comment '是否启用 【1：启用，0：废弃】',
    `create_by` bigint unsigned default null comment '创建者ID',
    `create_time` datetime not null default now() comment '创建日期',
    `update_by` bigint unsigned default null comment '最后更新者ID',
    `update_time` datetime default null comment '最后更新日期',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='题型';

insert into question_type(id, name, sort_num, enabled, create_by, create_time) values (1000, '单选题', 10, 1, 0, now());
insert into question_type(id, name, sort_num, enabled, create_by, create_time) values (1010, '多选题', 20, 1, 0, now());
insert into question_type(id, name, sort_num, enabled, create_by, create_time) values (1020, '判断题', 30, 1, 0, now());
insert into question_type(id, name, sort_num, enabled, create_by, create_time) values (1030, '填空题', 40, 1, 0, now());
insert into question_type(id, name, sort_num, enabled, create_by, create_time) values (1040, '解答题', 50, 1, 0, now());
insert into question_type(id, name, sort_num, enabled, create_by, create_time) values (1050, '实操题', 60, 1, 0, now());


# 难度
CREATE TABLE IF NOT EXISTS `difficulty` (
    `id` bigint unsigned not null auto_increment COMMENT 'ID',
    `name` varchar(30) not null comment '难度名称',
    `sort_num` int unsigned not null default 0 comment '排序号',
    `enabled` tinyint(1) unsigned not null default 1 comment '是否启用 【1：启用，0：废弃】',
    `create_by` bigint unsigned default null comment '创建者ID',
    `create_time` datetime not null default now() comment '创建日期',
    `update_by` bigint unsigned default null comment '最后更新者ID',
    `update_time` datetime default null comment '最后更新日期',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='难度';

insert into difficulty(id, name, sort_num, enabled, create_by, create_time) values (1, '简单', 10, 1, 0, now());
insert into difficulty(id, name, sort_num, enabled, create_by, create_time) values (2, '普通', 20, 1, 0, now());
insert into difficulty(id, name, sort_num, enabled, create_by, create_time) values (3, '困难', 10, 1, 0, now());


# 分类
CREATE TABLE IF NOT EXISTS `category` (
    `id` bigint unsigned not null auto_increment comment 'ID',
    `name` varchar(30) not null comment '分类名称',
    `parent_id` bigint unsigned default 0 comment '父节点ID',
    `parent_code` varchar(30) default '0' comment '父节点ID串',
    `level` smallint(2) unsigned default 1 comment '层级',
    `sort_num` int unsigned not null default 0 comment '排序号',
    `group_id` int unsigned not null default 0 comment '分组ID【1：试题，2：试卷】',
    `enabled` tinyint(1) unsigned not null default 1 comment '是否启用【1：启用，0：废弃】',
    `org_id` bigint(20) unsigned not null comment '机构ID',
    `create_by` bigint unsigned default null comment '创建者ID',
    `create_time` datetime not null default now() comment '创建日期',
    `update_by` bigint unsigned default null comment '最后更新者ID',
    `update_time` datetime default null comment '最后更新日期',
    primary key (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='分类';

insert into `category`(`id`, `name`, `sort_num`, `create_by`, `create_time`) values ('1', '安全技术', 10, 0, now());
insert into `category`(`id`, `name`, `sort_num`, `create_by`, `create_time`) values ('2', '安全认证', 10, 0, now());
INSERT INTO `category`(`id`, `name`, `sort_num`, `create_by`, `create_time`) VALUES ('3', 'Java', '10', '0', now());
INSERT INTO `category`(`id`, `name`, `sort_num`, `create_by`, `create_time`) VALUES ('4', '数据库', '10', '0', now());
INSERT INTO `category`(`id`, `name`, `sort_num`, `create_by`, `create_time`) VALUES ('5', 'MySQL', '10', '0', now());
INSERT INTO `category`(`id`, `name`, `sort_num`, `create_by`, `create_time`) VALUES ('6', 'Spring Boot', '10', '0', now());
INSERT INTO `category`(`id`, `name`, `sort_num`, `create_by`, `create_time`) VALUES ('7', 'Spring Cloud', '10', '0', now());
INSERT INTO `category`(`id`, `name`, `sort_num`, `create_by`, `create_time`) VALUES ('8', 'Nacos', '10', '0', now());


# 试题
CREATE TABLE IF NOT EXISTS `question` (
    `id` bigint unsigned not null auto_increment comment '试题ID',
    `question_type_id` smallint(5) unsigned not null comment '题型ID',
    `difficulty_id` int unsigned not null COMMENT '难度ID',
    `score` float(4,1) unsigned not null COMMENT '分数',
    `parent_flag` tinyint(1) unsigned not null default 1 COMMENT '父题标记, 1: 父题 0:小题',
    `org_id` bigint(20) unsigned not null comment '机构ID',
    `use_count` int unsigned DEFAULT 0 COMMENT '使用次数',
    `create_by` bigint unsigned default null comment '创建者ID',
    `create_time` datetime not null default now() comment '创建日期',
    `update_by` bigint unsigned default null comment '最后更新者ID',
    `update_time` datetime default null comment '最后更新日期',
    `enabled` tinyint(1) unsigned not null default 1 comment '是否启用 【1：启用，0：废弃】',
    PRIMARY KEY (`id`),
    KEY `id_idx` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='试题';


CREATE TABLE IF NOT EXISTS `question_body` (
    `id` bigint unsigned not null comment 'id 不自增, 与 question 表保持一致',
    `content` longtext comment '题干',
    `answer` longtext comment '答案',
    `answer_desc` longtext comment '答案解析',
    `org_id` bigint(20) unsigned not null comment '机构ID',
    PRIMARY KEY (`id`),
    KEY `id_idx` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试题主体';


CREATE TABLE IF NOT EXISTS `question_option` (
    `id` bigint unsigned not null auto_increment comment 'ID',
    `question_id` bigint unsigned not null comment '试题ID',
    `option` char(1) not null comment '选项[A-J]',
    `content` text not null comment '选项内容',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `org_id` bigint(20) unsigned not null comment '机构ID',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='试题选项';

CREATE TABLE IF NOT EXISTS `question_sub` (
    `parent_id` bigint(20) unsigned not null comment '父题ID',
    `sub_id` bigint(20) unsigned not null comment '子题ID',
    `sort_num` int unsigned not null default 0 comment '排序号',
    `org_id` bigint(20) unsigned not null comment '机构ID',
    PRIMARY KEY (`parent_id`, `sub_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='父子题关联';


# 试题分类关联表
CREATE TABLE IF NOT EXISTS `question_category` (
    `id` bigint unsigned not null auto_increment comment 'ID',
    `question_id` bigint unsigned comment '试题ID',
    `category_id` bigint unsigned comment '分类ID',
    `create_by` bigint unsigned default null comment '创建者ID',
    `create_time` datetime not null default now() comment '创建日期',
    `org_id` bigint(20) unsigned not null comment '机构ID',
    primary key (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='试题分类关联';


# 试卷
CREATE TABLE IF NOT EXISTS `paper` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT comment 'ID',
    `name` varchar(255) NOT NULL comment '试卷名称',
#     `paper_type_id` int(11) DEFAULT NULL comment '试卷类型ID',
#     `difficulty_id` int COMMENT '难度ID',
    `question_count` int(11) DEFAULT NULL COMMENT '试卷题目总数',
    `total_score` float(8,1) DEFAULT NULL COMMENT '试卷总分',
#     `duration` smallint(3) DEFAULT NULL COMMENT '考试时长(分钟)',
    `enabled` tinyint(1) unsigned not null default 1 comment '是否启用 【1：启用，0：废弃】',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `org_id` bigint(20) unsigned not null comment '机构ID',
#     `shelf_status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '上下架状态{0: 未上架, 1: 已上架, -1: 已下架}',
#     `up_shelf_user_id` int(11) DEFAULT NULL COMMENT '上架用户id',
#     `up_shelf_time` datetime DEFAULT NULL COMMENT '上架日期',
#     `down_shelf_user_id` int(11) DEFAULT NULL COMMENT '下架用户id',
#     `down_shelf_time` datetime DEFAULT NULL COMMENT '下架日期',
#     `browses` int(11) unsigned NOT NULL DEFAULT 0 COMMENT '浏览次数',
#     `use_count` smallint(11) unsigned DEFAULT '0' COMMENT '使用次数',
    `create_by` bigint unsigned default null comment '创建者ID',
    `create_time` datetime not null default now() comment '创建日期',
    `update_by` bigint unsigned default null comment '最后更新者ID',
    `update_time` datetime default null comment '最后更新日期',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='试卷';


# 试卷试题关联表
CREATE TABLE IF NOT EXISTS `paper_question` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT comment 'ID',
    `paper_id` bigint NOT NULL comment '试卷ID',
    `question_id` bigint NOT NULL comment '试题ID',
    `score` float(4,1) unsigned NOT NULL COMMENT '该题在该试卷中的分值',
    `parent_id` bigint DEFAULT NULL COMMENT '父题ID',
    `global_order` smallint(3) DEFAULT NULL COMMENT '题目序号(试卷内不分题型)',
    `local_order` smallint(3) unsigned NOT NULL DEFAULT 0 COMMENT '排序号(同一题型内)',
    `display_height` smallint(4) unsigned DEFAULT NULL COMMENT '显示高度',
    `create_by` bigint unsigned default null comment '创建者ID',
    `create_time` datetime not null default now() comment '创建日期',
    `update_by` bigint unsigned default null comment '最后更新者ID',
    `update_time` datetime comment '最后更新日期',
    `org_id` bigint(20) unsigned not null comment '机构ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `com_pid_qid` (`paper_id`,`question_id`) USING BTREE,
    KEY `idx_question` (`question_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='试卷试题关联';


# 试卷题型顺序表
CREATE TABLE IF NOT EXISTS `paper_question_type` (
    `id` bigint unsigned not null auto_increment comment 'ID',
    `paper_id` bigint unsigned comment '试卷ID',
    `question_type_id` bigint unsigned comment '题型ID',
    `sort_num` int unsigned not null default 0 comment '排序号',
    `org_id` bigint(20) unsigned not null comment '机构ID',
    primary key (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='试卷题型';


# 试卷分类关联表
CREATE TABLE IF NOT EXISTS `paper_category` (
    `id` bigint unsigned not null auto_increment comment 'ID',
    `paper_id` bigint unsigned comment '试卷ID',
    `category_id` bigint unsigned comment '分类ID',
    `create_by` bigint unsigned default null comment '创建者ID',
    `create_time` datetime not null default now() comment '创建日期',
    `org_id` bigint(20) unsigned not null comment '机构ID',
    primary key (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='试卷分类关联';

