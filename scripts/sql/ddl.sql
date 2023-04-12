CREATE TABLE `secret_verification` (
                                     `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增长id',
                                     `secret_key` varchar(64) NOT NULL COMMENT '密钥',
                                     `secret_days` int(11) NOT NULL COMMENT '密钥有效期天数',
                                     `produce_date` varchar(32) NOT NULL COMMENT '秘钥生成日期 格式yyyy-MM-dd',
                                     `verify_begin_datetime` varchar(32) DEFAULT NULL COMMENT '核销开始时间 格式yyyy-MM-dd hh:mm:ss',
                                     `verify_end_datetime` varchar(32) DEFAULT NULL COMMENT '核销结束时间 格式yyyy-MM-dd hh:mm:ss',
                                     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `create_person` varchar(32) NOT NULL DEFAULT 'system' COMMENT '创建人',
                                     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                     `update_person` varchar(32) NOT NULL DEFAULT 'system' COMMENT '更新人',
                                     PRIMARY KEY (`id`),
                                     KEY `uniq_idx_secret_verification` (`secret_key`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='密钥核销表';