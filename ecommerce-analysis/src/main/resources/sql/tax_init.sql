-- ============================================
-- 税务数据初始化脚本
-- 包含税率表和汇率表
-- ============================================

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
-- 插入巴西税率数据
-- ============================================
INSERT INTO `tax_rates` (`country`, `country_name`, `category`, `import_tax_rate`, `vat_rate`, `consumption_tax_rate`, `effective_tax_rate`, `hs_code`, `description`, `policy_changes`, `tax_tips`) VALUES
('brazil', '巴西', '服装配饰', 10.00, 17.00, 0.00, 28.87, '61-62章', '服装类商品', '2024年巴西政府宣布降低部分消费品进口关税', '建议通过正规清关渠道进口，确保HS编码准确'),
('brazil', '巴西', '电子产品', 10.00, 17.00, 0.00, 28.87, '85章', '电子设备类商品', '电子产品进口关税从15%降至10%', '巴西清关周期较长，建议预留足够时间'),
('brazil', '巴西', '家居用品', 10.00, 17.00, 0.00, 28.87, '94章', '家具及家居用品', '家居类产品保持稳定税率', '选择可靠的物流合作伙伴'),
('brazil', '巴西', '美妆护肤', 10.00, 17.00, 15.00, 43.87, '33章', '化妆品及护肤品', '美妆产品需额外注意ANVISA认证', '需提前申请ANVISA认证，认证周期约3-6个月'),
('brazil', '巴西', '母婴用品', 10.00, 17.00, 0.00, 28.87, '95章', '玩具及婴幼儿用品', '儿童产品有特殊监管要求', '确保产品符合巴西安全标准'),
('brazil', '巴西', '运动户外', 10.00, 17.00, 0.00, 28.87, '95章', '体育用品', '体育用品税率保持稳定', '运动品类市场需求持续增长'),
('brazil', '巴西', '食品饮料', 0.00, 17.00, 0.00, 17.00, '21章', '食品及饮料', '食品类进口税率较低', '需办理巴西食品认证'),
('brazil', '巴西', '宠物用品', 10.00, 17.00, 0.00, 28.87, '95章', '宠物用品', '宠物市场快速增长', '选择有经验的物流服务商'),
('brazil', '巴西', '汽车配件', 10.00, 17.00, 0.00, 28.87, '87章', '汽车零部件', '汽配类产品税率稳定', '注意原产地标识要求'),
('brazil', '巴西', '数码配件', 10.00, 17.00, 0.00, 28.87, '85章', '电子配件', '配件类产品需求旺盛', '综合税率较高，需做好成本核算')
ON DUPLICATE KEY UPDATE
    `import_tax_rate` = VALUES(`import_tax_rate`),
    `vat_rate` = VALUES(`vat_rate`),
    `consumption_tax_rate` = VALUES(`consumption_tax_rate`),
    `effective_tax_rate` = VALUES(`effective_tax_rate`);

-- ============================================
-- 插入中国税率数据
-- ============================================
INSERT INTO `tax_rates` (`country`, `country_name`, `category`, `import_tax_rate`, `vat_rate`, `consumption_tax_rate`, `effective_tax_rate`, `hs_code`, `description`, `policy_changes`, `tax_tips`) VALUES
('china', '中国', '服装配饰', 0.00, 13.00, 0.00, 13.00, '61-62章', '服装类商品', '跨境电商综试区政策持续优化', '可申请出口退税，合理利用政策优惠'),
('china', '中国', '电子产品', 0.00, 13.00, 0.00, 13.00, '85章', '电子设备类商品', '电子产品出口退税率稳定', '选择市场采购贸易等便利化方式'),
('china', '中国', '家居用品', 0.00, 13.00, 0.00, 13.00, '94章', '家具及家居用品', '家具出口保持增长态势', '利用跨境电商综合试验区政策'),
('china', '中国', '美妆护肤', 0.00, 13.00, 0.00, 13.00, '33章', '化妆品及护肤品', '美妆产品出口退税率较高', '注意目的地国家的化妆品法规'),
('china', '中国', '母婴用品', 0.00, 13.00, 0.00, 13.00, '95章', '玩具及婴幼儿用品', '儿童产品出口增长迅速', '确保符合目的地安全标准'),
('china', '中国', '运动户外', 0.00, 13.00, 0.00, 13.00, '95章', '体育用品', '户外运动用品需求增长', '选择可靠的物流渠道'),
('china', '中国', '食品饮料', 0.00, 9.00, 0.00, 9.00, '21章', '食品及饮料', '食品出口需办理SC认证', '注意目的地国家的食品进口要求'),
('china', '中国', '宠物用品', 0.00, 13.00, 0.00, 13.00, '95章', '宠物用品', '宠物市场快速增长', '宠物食品需特殊认证'),
('china', '中国', '汽车配件', 0.00, 13.00, 0.00, 13.00, '87章', '汽车零部件', '汽配出口保持稳定', '注意目的地认证要求'),
('china', '中国', '数码配件', 0.00, 13.00, 0.00, 13.00, '85章', '电子配件', '配件类产品出口增长', '利用跨境电商便利化政策')
ON DUPLICATE KEY UPDATE
    `import_tax_rate` = VALUES(`import_tax_rate`),
    `vat_rate` = VALUES(`vat_rate`),
    `consumption_tax_rate` = VALUES(`consumption_tax_rate`),
    `effective_tax_rate` = VALUES(`effective_tax_rate`);

-- ============================================
-- 插入汇率数据
-- ============================================
INSERT INTO `exchange_rates` (`from_currency`, `to_currency`, `rate`) VALUES
('CNY', 'BRL', 5.5000),
('BRL', 'CNY', 0.1818),
('CNY', 'USD', 0.1400),
('USD', 'CNY', 7.2400),
('BRL', 'USD', 0.2000),
('USD', 'BRL', 5.0000),
('CNY', 'EUR', 0.1300),
('EUR', 'CNY', 7.8000)
ON DUPLICATE KEY UPDATE `rate` = VALUES(`rate`);
