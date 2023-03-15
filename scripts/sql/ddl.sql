-- 交易表
DROP TABLE IF EXISTS trade_record;
CREATE TABLE `trade_record` (
                              `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增长id',
                              `date` VARCHAR ( 32 ) NOT NULL COMMENT '日期 格式yyyy-MM-dd',
                              `stock_code` VARCHAR ( 64 ) NOT NULL COMMENT '股票代码',
                              `direction` INT NOT NULL COMMENT '交易方向 1买入 2卖出',
                              `amount` INT NOT NULL COMMENT '交易数量，单位 股',
                              `price` NUMERIC ( 13, 2 ) NOT NULL COMMENT '交易价格，单位 元',
                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `create_person` VARCHAR ( 32 ) NOT NULL DEFAULT 'system' COMMENT '创建人',
                              `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                              `update_person` VARCHAR ( 32 ) NOT NULL DEFAULT 'system' COMMENT '更新人',
                              PRIMARY KEY ( `id` ),
                              UNIQUE KEY `idx_trade_record` ( `date`, `stock_code`, `direction` )
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT = '交易表';

-- 资金表
DROP TABLE IF	EXISTS funds;
CREATE TABLE `funds` (
                       `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增长id',
                       `date` VARCHAR ( 32 ) NOT NULL COMMENT '日期 格式yyyy-MM-dd',
                       `funds_amount` NUMERIC ( 13, 2 ) NOT NULL DEFAULT 0 COMMENT '资金 单位 元',
                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       `create_person` VARCHAR ( 32 ) NOT NULL DEFAULT 'system' COMMENT '创建人',
                       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                       `update_person` VARCHAR ( 32 ) NOT NULL DEFAULT 'system' COMMENT '更新人',
                       PRIMARY KEY ( `id` ),
                       UNIQUE KEY `uniq_idx_funds` ( `date` )
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT = '资金表';

-- 持仓表
DROP TABLE IF EXISTS stocks;
CREATE TABLE `stocks` (
                        `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增长id',
                        `date` VARCHAR ( 32 ) NOT NULL COMMENT '日期 格式yyyy-MM-dd',
                        `stock_code` VARCHAR ( 32 ) NOT NULL COMMENT '股票代码',
                        `stock_amount` INT NOT NULL DEFAULT 0 COMMENT '持仓数量 单位 股',
                        `cost_price` NUMERIC ( 13, 2 ) NOT NULL COMMENT '持仓成本',
                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `create_person` VARCHAR ( 32 ) NOT NULL DEFAULT 'system' COMMENT '创建人',
                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                        `update_person` VARCHAR ( 32 ) NOT NULL DEFAULT 'system' COMMENT '更新人',
                        PRIMARY KEY ( `id` ),
                        UNIQUE KEY `uniq_idx_stock_code` ( `date`, `stock_code` )
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT = '持仓表';