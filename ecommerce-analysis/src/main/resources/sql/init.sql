-- ============================================
-- 表结构初始化脚本
-- 运行顺序：先执行此文件，再执行 data.sql
-- ============================================

-- 创建巴西订单表
CREATE TABLE IF NOT EXISTS `olist_orders` (
    `order_id` VARCHAR(50) PRIMARY KEY COMMENT '订单ID',
    `customer_id` VARCHAR(50) NOT NULL COMMENT '客户ID',
    `order_status` VARCHAR(20) DEFAULT '' COMMENT '订单状态',
    `product_id` VARCHAR(50) NOT NULL COMMENT '商品ID',
    `product_category` VARCHAR(100) DEFAULT '' COMMENT '商品类别',
    `price` DECIMAL(10,2) DEFAULT 0.00 COMMENT '商品价格',
    `freight_value` DECIMAL(10,2) DEFAULT 0.00 COMMENT '运费',
    `order_date` DATE COMMENT '订单日期',
    `order_delivered_customer_date` DATE COMMENT '交付日期',
    `shipping_limit_date` DATE COMMENT '发货期限',
    `customer_city` VARCHAR(100) DEFAULT '' COMMENT '客户城市',
    `customer_state` VARCHAR(50) DEFAULT '' COMMENT '客户州',
    INDEX `idx_category` (`product_category`),
    INDEX `idx_order_date` (`order_date`),
    INDEX `idx_state` (`customer_state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巴西订单表';

-- 创建国内订单表
CREATE TABLE IF NOT EXISTS `domestic_orders` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_id` VARCHAR(50) NOT NULL COMMENT '订单号',
    `category` VARCHAR(100) NOT NULL COMMENT '商品类别',
    `product_name` VARCHAR(200) DEFAULT '' COMMENT '商品名称',
    `quantity` INT DEFAULT 0 COMMENT '销售数量',
    `unit_price` DECIMAL(10,2) DEFAULT 0.00 COMMENT '单价',
    `total_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '总金额',
    `cost_price` DECIMAL(10,2) DEFAULT 0.00 COMMENT '成本价',
    `order_date` DATE COMMENT '订单日期',
    `region` VARCHAR(100) DEFAULT '' COMMENT '销售区域',
    `sales_channel` VARCHAR(50) DEFAULT '' COMMENT '销售渠道',
    INDEX `idx_category` (`category`),
    INDEX `idx_order_date` (`order_date`),
    INDEX `idx_region` (`region`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='国内订单表';

-- 创建选品评分表
CREATE TABLE IF NOT EXISTS `selection_score` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `category` VARCHAR(100) NOT NULL COMMENT '品类名称',
    `market_score` INT DEFAULT 0 COMMENT '市场评分',
    `supply_score` INT DEFAULT 0 COMMENT '供应链评分',
    `competition_score` INT DEFAULT 0 COMMENT '竞争度评分',
    `profit_score` INT DEFAULT 0 COMMENT '利润评分',
    `growth_score` INT DEFAULT 0 COMMENT '增长评分',
    `final_score` INT DEFAULT 0 COMMENT '综合评分',
    `grade` VARCHAR(10) DEFAULT '' COMMENT '等级',
    `radar_data` VARCHAR(200) DEFAULT '' COMMENT '雷达图数据',
    `monthly_trend` VARCHAR(200) DEFAULT '' COMMENT '月度趋势',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选品评分表';

-- 创建选品结果表
CREATE TABLE IF NOT EXISTS `selection_result` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `category` VARCHAR(100) NOT NULL COMMENT '品类名称',
    `cn_category` VARCHAR(100) DEFAULT '' COMMENT '国内品类',
    `score` INT DEFAULT 0 COMMENT '评分',
    `grade` VARCHAR(10) DEFAULT '' COMMENT '等级',
    `status` VARCHAR(20) DEFAULT '' COMMENT '状态：红海/蓝海/黄海',
    `brazil_sales` INT DEFAULT 0 COMMENT '巴西销量',
    `china_sales` INT DEFAULT 0 COMMENT '国内销量',
    `profit_margin` DECIMAL(10,2) DEFAULT 0.00 COMMENT '毛利率',
    `growth_rate` DECIMAL(10,2) DEFAULT 0.00 COMMENT '增长率',
    `recommendation` TEXT COMMENT '推荐理由',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_category` (`category`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选品结果表';

-- 创建税率表
CREATE TABLE IF NOT EXISTS `tax_rates` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `country` VARCHAR(50) NOT NULL COMMENT '国家代码：brazil, china',
    `country_name` VARCHAR(100) NOT NULL COMMENT '国家名称',
    `category` VARCHAR(100) NOT NULL COMMENT '商品类别',
    `import_tax_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '进口关税率(%)',
    `vat_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '增值税率(%)',
    `consumption_tax_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '消费税率(%)',
    `effective_tax_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '综合税率(%)',
    `hs_code` VARCHAR(50) DEFAULT '' COMMENT 'HS编码建议',
    `description` VARCHAR(500) DEFAULT '' COMMENT '类别描述',
    `policy_changes` VARCHAR(1000) DEFAULT '' COMMENT '最新政策变化',
    `tax_tips` VARCHAR(1000) DEFAULT '' COMMENT '税务筹划建议',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_country_category` (`country`, `category`),
    INDEX `idx_country` (`country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='税率配置表';

-- 创建汇率表
CREATE TABLE IF NOT EXISTS `exchange_rates` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `from_currency` VARCHAR(10) NOT NULL COMMENT '源货币代码',
    `to_currency` VARCHAR(10) NOT NULL COMMENT '目标货币代码',
    `rate` DECIMAL(10,4) NOT NULL COMMENT '汇率',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_currency_pair` (`from_currency`, `to_currency`),
    INDEX `idx_from_currency` (`from_currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='汇率表';

-- ============================================
-- 初始化表结构完成
-- 下一步：执行 data.sql 导入测试数据
-- ============================================
