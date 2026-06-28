# product-approval-demo

Java Web 考核项目：商家商品申请审批模块。

## 技术栈

- Java 17
- Spring Boot 3.5.3
- Maven
- Spring MVC + Thymeleaf
- Spring Data JPA
- MySQL
- 简单 CSS

## 当前功能

商家端：

- 商家工作台：当前商家的申请统计和最近申请
- 提交商品申请：分类来自 `product_category` 表
- 我的申请：按商品名、分类、状态、是否告警筛选
- 导出当前筛选结果 CSV

管理端：

- 审批工作台：全局统计、最近待审批、最近价格告警
- 商品申请管理：单页多条件筛选
- 价格告警：复用申请列表，默认 `warning=true`
- 商品分类维护：新增、修改、启用、停用
- 导出当前筛选结果 CSV

## 数据库

新建数据库：

```sql
CREATE DATABASE IF NOT EXISTS product_approval_demo
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

如果是全新数据库，执行：

```powershell
mysql --default-character-set=utf8mb4 -u root -p product_approval_demo < src/main/resources/schema.sql
```

如果你已经运行过基础版并已有旧的 `product_apply` 表，执行：

```powershell
mysql --default-character-set=utf8mb4 -u root -p product_approval_demo < src/main/resources/upgrade-from-basic.sql
```

Windows 的 `cmd` 或 PowerShell 导入中文 SQL 时，建议带上 `--default-character-set=utf8mb4`，否则初始化商家和分类中文数据时可能乱码或报 `Incorrect string value`。

说明：

- `merchant` 表只维护商家名称和启用状态，不做账号密码。
- `product_category` 表维护商品分类。
- `product_apply` 保存 `merchant_id`、`category_id`，同时保留商家名和分类名快照，方便历史展示和 CSV 导出。

## 配置 MySQL

修改：

```text
src/main/resources/application.properties
```

示例：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/product_approval_demo?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=你的MySQL密码
```

## 启动

```bash
mvn spring-boot:run
```

## 访问地址

管理端：

```text
http://localhost:8080/admin/dashboard
http://localhost:8080/admin/applications
http://localhost:8080/admin/applications/warnings
http://localhost:8080/admin/categories
```

商家端通过 URL 参数模拟当前商家：

```text
http://localhost:8080/merchant/dashboard?merchantId=1
http://localhost:8080/merchant/apply?merchantId=1
http://localhost:8080/merchant/applications?merchantId=1
```

如果缺少 `merchantId`，商家端会提示“缺少商家ID”。

## 演示流程

1. 打开 `http://localhost:8080/merchant/apply?merchantId=1`。
2. 选择商品分类，填写商品名称和价格。
3. 价格填 `1200` 可演示价格告警。
4. 提交后打开 `http://localhost:8080/merchant/applications?merchantId=1` 查看我的申请。
5. 打开 `http://localhost:8080/admin/applications` 查看全部申请。
6. 在管理端点击通过，备注可空，默认“审批通过”。
7. 再提交一条申请，点击驳回时必须填写驳回原因。
8. 打开 `http://localhost:8080/admin/applications/warnings` 查看价格告警。
9. 打开 `http://localhost:8080/admin/categories` 新增、修改、启用或停用分类。
10. 在列表底部点击“导出当前筛选结果 CSV”，Excel 打开中文不乱码。

## 验证

```bash
mvn test
```

本项目测试环境使用 H2 内存数据库和 `src/test/resources/import.sql` 初始化商家与分类数据。
