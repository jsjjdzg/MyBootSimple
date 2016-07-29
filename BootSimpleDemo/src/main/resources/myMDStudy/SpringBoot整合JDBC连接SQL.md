#### 整合流程：
###### 1、POM.xml添加需要的包：

```
<!-- mysql连接 -->
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<scope>runtime</scope>
</dependency>
<!-- Spring Boot JDBC -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```
###### 2、application.properties配置文件添加：

```
#JDBC连接信息
spring.datasource.url=jdbc:mysql://58.210.98.198:3306/hbwx_v1_test
spring.datasource.username=bnxd
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```

###### 3、写Service类测试：
> 需要先写实体类

```java
/**
 * 
 * FanService测试JDBC类
 * 
 * @author DZG
 * @since V1.0 2016年7月29日
 */
@Service("fanService")
public class FanService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Fan> getFans(){
		String sql = "SELECT id,nickname,city,sex,province FROM weixin_fan limit 1,10";
		return jdbcTemplate.query(sql, new RowMapper<Fan>(){

			@Override
			public Fan mapRow(ResultSet rs, int rowNum) throws SQLException {
				Fan fan = new Fan();
				fan.setId(rs.getString("id"));
				fan.setNickname(rs.getString("nickname"));
				fan.setSex(rs.getString("sex"));
				fan.setCity(rs.getString("city"));
				fan.setProvince(rs.getString("province"));
				return fan;
			}
			
		});
	}
}
```
**需要注意：JdbcTemplate JDBC操作都是根据此模板类进行操作，配置文件中配置了jdbc的内容，会自动生成DataSource的 Bean**

###### 4、配置连接池：
> 配置文件中添加：

```
#连接池
spring.datasource.max-idle=10
spring.datasource.max-wait=10000
spring.datasource.min-idle=5
spring.datasource.initial-size=5
spring.datasource.validation-query=SELECT 1
spring.datasource.test-on-borrow=false
spring.datasource.test-while-idle=true
spring.datasource.time-between-eviction-runs-millis=18800
#延迟时间
spring.datasource.jdbc-interceptors=ConnectionState;SlowQueryReport(threshold=20)
```



