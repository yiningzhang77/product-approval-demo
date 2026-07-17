CREATE DATABASE IF NOT EXISTS product_approval_demo
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

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

CREATE TABLE IF NOT EXISTS product_apply (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  merchant_id BIGINT,
  merchant_name VARCHAR(100) NOT NULL,
  category_id BIGINT,
  category_name VARCHAR(100) NOT NULL,
  product_name VARCHAR(100) NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  image_url VARCHAR(255),
  intro TEXT,
  remark VARCHAR(500),
  status VARCHAR(20) NOT NULL,
  approval_remark VARCHAR(500),
  is_warning TINYINT(1) NOT NULL DEFAULT 0,
  warning_threshold DECIMAL(10, 2) NOT NULL,
  warning_reason VARCHAR(255),
  created_at DATETIME,
  updated_at DATETIME,
  approved_at DATETIME
);

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
