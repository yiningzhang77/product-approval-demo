USE product_approval_demo;
SET NAMES utf8mb4;

INSERT INTO product_apply (
  merchant_id, merchant_name, category_id, category_name,
  product_name, price, image_url, intro, remark,
  status, approval_remark, is_warning, warning_reason,
  created_at, updated_at, approved_at
) VALUES
(1, '百姓超市', 1, '食品饮料',
 '有机纯牛奶', 68.00, NULL, '适合家庭日常饮用的有机纯牛奶。', '常规商品申请',
 'PENDING', NULL, 0, NULL,
 NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY, NULL),

(1, '百姓超市', 3, '数码家电',
 '智能空气炸锅', 1299.00, NULL, '多功能智能空气炸锅，支持预约和温控。', '高价商品，请重点审核',
 'PENDING', NULL, 1, '商品价格超过审批阈值，请重点关注',
 NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY, NULL),

(1, '百姓超市', 4, '日用百货',
 '保温水杯', 89.90, NULL, '316不锈钢保温水杯，适合办公和户外。', '补充日用品类目',
 'APPROVED', '审批通过',
 0, NULL,
 NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY),

(1, '百姓超市', 5, '美妆个护',
 '进口护肤礼盒', 1688.00, NULL, '节日促销护肤礼盒套装。', '价格较高，需确认授权',
 'REJECTED', '缺少品牌授权证明，请补充材料后重新提交',
 1, '商品价格超过审批阈值，请重点关注',
 NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY),

(2, '万隆商城', 2, '服装鞋帽',
 '春季休闲外套', 299.00, NULL, '春季新款休闲外套，多尺码可选。', '换季新品',
 'PENDING', NULL,
 0, NULL,
 NOW() - INTERVAL 3 HOUR, NOW() - INTERVAL 3 HOUR, NULL),

(2, '万隆商城', 3, '数码家电',
 '4K 智能电视', 3599.00, NULL, '55英寸4K智能电视，适合家庭客厅。', '大件高价商品',
 'APPROVED', '审批通过',
 1, '商品价格超过审批阈值，请重点关注',
 NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 7 DAY),

(2, '万隆商城', 6, '运动户外',
 '户外登山包', 468.00, NULL, '大容量户外登山包，适合短途旅行。', '户外类商品',
 'REJECTED', '商品简介不完整，请补充材质和容量说明',
 0, NULL,
 NOW() - INTERVAL 9 DAY, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 8 DAY),

(3, '测试商家', 7, '图书文具',
 '办公笔记本套装', 39.90, NULL, '办公会议常用笔记本和签字笔组合。', '低价常规商品',
 'PENDING', NULL,
 0, NULL,
 NOW() - INTERVAL 6 HOUR, NOW() - INTERVAL 6 HOUR, NULL),

(3, '测试商家', 8, '其他',
 '企业定制礼品盒', 1180.00, NULL, '用于企业活动的定制礼品盒。', '定制商品，价格略高',
 'PENDING', NULL,
 1, '商品价格超过审批阈值，请重点关注',
 NOW() - INTERVAL 10 HOUR, NOW() - INTERVAL 10 HOUR, NULL),

(3, '测试商家', 1, '食品饮料',
 '精品坚果礼盒', 199.00, NULL, '节日精品坚果礼盒，适合团购。', '食品礼盒',
 'APPROVED', '审批通过',
 0, NULL,
 NOW() - INTERVAL 12 DAY, NOW() - INTERVAL 11 DAY, NOW() - INTERVAL 11 DAY);
