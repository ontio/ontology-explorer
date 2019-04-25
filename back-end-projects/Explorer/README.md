# explorer

Provide Restful API about block,transaction,ontid information for the Explorer front-end and DApp.



## mybatis-generator

使用mvn命令：mvn mybatis-generator:generate



## package
使用mvn命令：mvn clean package



## 使用flyway做数据迁移

使用flyway可以使每个程序员对数据库的改动都能自动的合并更新。

> 使用flyway需要修改`flyway.conf`连接到本地数据库。

### 第一次使用flyway
1. 再配置文件中取消flyway，我们使用mvn插件跑数据库迁移命令

`spring.flyway.enabled=false`

2. 如果本地已经有数据库，清除数据库（注意生产环境不能删除数据，可以使用baseline方法）

`mvn -Dflyway.configFiles=./config/flyway.conf flyway:clean`

3. 迁移

`mvn -Dflyway.configFiles=./config/flyway.conf flyway:migrate`

更多命令参考[flyway 文档](https://flywaydb.org/documentation/maven/)

### 非第一次使用flyway

直接使用`mvn -Dflyway.configFiles=./config/flyway.conf flyway:migrate`

### 生成环境已经有大量数据，但是没有用过flyway

1. 使用baseline

`mvn -Dflyway.configFiles=./config/flyway.conf flyway:baseline`

2. 如果发现错误，可以用repair尝试修复

` mvn -Dflyway.configFiles=./config/flyway.conf flyway:repaire`

3. 再使用migrate

4. 后面就和非第一次使用flyway的情况一样了

### migration脚本

1. 脚本在`/src/main/resources/db/migration`
2. 配置文件支持的格式`https://flywaydb.org/documentation/configfiles`
3. 命名规则`https://flywaydb.org/documentation/migrations`
4. 一般V1用来新建表，V2用来插入数据，再有修改可以继续往后加
