-- 台账表字段重构（原字段废弃，需重建）
-- 如有旧数据请先备份！
DROP TABLE IF EXISTS `biz_ledger`;
CREATE TABLE `biz_ledger` (
  `id` bigint NOT NULL,
  `date` varchar(50) DEFAULT '' COMMENT '日期',
  `inspection_date` varchar(50) DEFAULT '' COMMENT '检查日期',
  `inspected_unit` varchar(200) DEFAULT '' COMMENT '被检查单位',
  `legal_person` varchar(100) DEFAULT '' COMMENT '法定代表人',
  `contact_phone` varchar(50) DEFAULT '' COMMENT '联系方式',
  `address` varchar(300) DEFAULT '' COMMENT '地址',
  `inspection_content` text COMMENT '检查内容',
  `rectification_measure` text COMMENT '整改措施',
  `rectification_progress` varchar(10) DEFAULT '0' COMMENT '整改进度(0未开始/1进行中/2已完成)',
  `inspector_category` varchar(100) DEFAULT '' COMMENT '检查人员类别',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  `unified_social_credit_code` varchar(18) DEFAULT '' COMMENT '统一社会信用代码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='台账表';

-- 问题清单表：添加严重程度字段
ALTER TABLE `biz_issue` ADD COLUMN `severity` varchar(10) DEFAULT '0' COMMENT '严重程度(0严重/1一般/2轻微)' AFTER `auto_increment_code`;

-- 签名档案表：添加部门字段
ALTER TABLE `biz_signature` ADD COLUMN `department` varchar(100) DEFAULT '' COMMENT '部门' AFTER `name`;

-- 检察人员库表
DROP TABLE IF EXISTS `biz_inspector`;
CREATE TABLE `biz_inspector` (
  `id` bigint NOT NULL,
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `department` varchar(100) DEFAULT '' COMMENT '部门',
  `status` varchar(10) DEFAULT '0' COMMENT '状态(0在职/1离职)',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检察人员库';

-- 检察人员初始数据
INSERT INTO `biz_inspector` (`id`, `name`, `department`, `status`, `create_time`) VALUES
(1, '王建军', '', '0', NOW()),
(2, '武立民', '', '0', NOW()),
(3, '王立民', '', '0', NOW()),
(4, '李玉根', '', '0', NOW()),
(5, '任惠', '', '0', NOW()),
(6, '任耀芳', '', '0', NOW()),
(7, '宋海波', '', '0', NOW());
