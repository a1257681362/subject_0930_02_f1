# EVOPS 基础工作区

这是 springboot的骨架项目。

技术栈：Java 8、Spring Boot 2.7、MyBatis-Plus、H2 本地文件数据库、Shiro、Thymeleaf。

启动前准备：

1. 执行 `mvn -q -DskipTests compile` 验证骨架构建。
2. 执行 `mvn spring-boot:run` 启动应用；Spring Boot 会自动执行 `src/main/resources/schema.sql`。
3. H2 数据文件默认写入工作区 `data/evops`。如需修改路径，编辑 `application.yml` 中的 `jdbc:h2:file:` 连接串。

根目录的 `..\..\docs\schema\evops.sql` 是交付副本，应与工作区 `src/main/resources/schema.sql` 保持一致。

## 海水养殖台账 V1

流程：建池 → 投苗 → 日常记录 → 抽检 → 出池（成本核算留待后续版本）。

- `POST /api/aqua/ponds` 建池（水深、盐度、所属海区，池塘编号唯一）
- `POST /api/aqua/batches` 投苗（批次编号唯一，关联苗种来源、入池数量、计划出池日；`DELETE` 被显式拒绝——原始投苗单不可删除）
- `POST /api/aqua/daily-records` / `feed-records` / `medicine-records` 日常、饲料、用药记录，均按池按批校验留痕（批次须在养且挂在该池）
- `POST /api/aqua/batches/{id}/transfer` 换池，自动生成转移记录（`GET /api/aqua/transfers` 查询）
- `POST /api/aqua/inspections` 抽检
- `POST /api/aqua/batches/{id}/harvest` 出池（`GET /api/aqua/harvests` 查询），出池后批次封存、禁止再登记

台账接口 `/api/aqua/**` 当前为内网放行（见 `ShiroConfig`），后续版本接入登录鉴权。

验收：启动应用后执行 `bash scripts/acceptance-aqua.sh`，覆盖建池→投苗→落库追溯→唯一性→留痕→换池→抽检→出池→禁删共 13 步，任一步失败会打印"停点"并以非零码退出。


