USE product_approval_demo;
SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS merchant (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  enabled TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME,
  updated_at DATETIME
);

CREATE TABLE IF NOT EXISTS product_category (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  sort_order INT NOT NULL DEFAULT 100,
  enabled TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME,
  updated_at DATETIME
);

SET @sql = (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE product_apply ADD COLUMN merchant_id BIGINT',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'product_apply'
    AND COLUMN_NAME = 'merchant_id'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE product_apply ADD COLUMN category_id BIGINT',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'product_apply'
    AND COLUMN_NAME = 'category_id'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE product_apply ADD COLUMN category_name VARCHAR(100)',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'product_apply'
    AND COLUMN_NAME = 'category_name'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

INSERT INTO merchant (id, name, enabled, created_at, updated_at)
VALUES
(1, '百姓超市', 1, NOW(), NOW()),
(2, '万隆商城', 1, NOW(), NOW()),
(3, '测试商家', 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name), enabled = VALUES(enabled), updated_at = NOW();

INSERT INTO product_category (id, name, sort_order, enabled, created_at, updated_at)
VALUES
(1, '食品饮料', 10, 1, NOW(), NOW()),
(2, '服装鞋帽', 20, 1, NOW(), NOW()),
(3, '数码家电', 30, 1, NOW(), NOW()),
(4, '日用百货', 40, 1, NOW(), NOW()),
(5, '美妆个护', 50, 1, NOW(), NOW()),
(6, '运动户外', 60, 1, NOW(), NOW()),
(7, '图书文具', 70, 1, NOW(), NOW()),
(8, '其他', 100, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name), sort_order = VALUES(sort_order), enabled = VALUES(enabled), updated_at = NOW();

UPDATE product_apply
SET merchant_id = 1
WHERE merchant_id IS NULL;

UPDATE product_apply
SET category_id = 8
WHERE category_id IS NULL;

UPDATE product_apply
SET category_name = COALESCE(NULLIF(category_name, ''), category, '其他')
WHERE category_name IS NULL OR category_name = '';
