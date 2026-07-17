ALTER TABLE product_apply
ADD COLUMN warning_threshold DECIMAL(10, 2) NULL AFTER is_warning;

SELECT COUNT(*) AS total,
       SUM(warning_threshold IS NULL) AS missing_threshold
FROM product_apply;

UPDATE product_apply
SET warning_threshold = 1000.00
WHERE warning_threshold IS NULL;

SELECT DISTINCT warning_threshold
FROM product_apply;

ALTER TABLE product_apply
MODIFY COLUMN warning_threshold DECIMAL(10, 2) NOT NULL;
