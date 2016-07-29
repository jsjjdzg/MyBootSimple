#### SpringBoot整合MyBatis:
##### 一、连接池的使用：
> SpringBoot默认使用 org.apache.tomcat.jdbc.pool.DataSource 
现在有个叫 HikariCP 的JDBC连接池组件，据说其性能比常用的 c3p0、tomcat、bone、vibur 这些要高很多。
github源码地址： https://github.com/brettwooldridge/HikariCP

```
只要在properties配置文件中指定：

spring.datasource.type=com.zaxxer.hikari.HikariDataSource

然后在pom中添加Hikari的依赖：

<!-- com.zaxxer.HikariCP 性能爆炸连接池组件 -->
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
</dependency>
```
##### 二、使用XML方式集成MyBatis
###### 1、添加POM一栏依赖

```
<!-- mybatis -->
<dependency>
	<groupId>org.mybatis.spring.boot</groupId>
	<artifactId>mybatis-spring-boot-starter</artifactId>
	<version>1.1.1</version>
</dependency>
```
###### 2、JDBC信息添加：
> 2.1 properties配置文件方式添加：

```
#JDBC连接信息
spring.datasource.url=jdbc:mysql://58.210.98.198:3306/hbwx_v1_test
spring.datasource.username=bnxd
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
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
> 2.2 Bean方式添加（如果配置文件中也有写，SpringBoot优先使用它）：

```java
@SpringBootApplication
@MapperScan("com.dzg.mapper")
public class BootSimpleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootSimpleApplication.class, args);
	}

	@Bean
	public HikariDataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://58.210.98.198:3306/hbwx_v1_test");
		config.setUsername("bnxd");
		config.setPassword("123456");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		return new HikariDataSource(config);
	}

	@Bean
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:myBatisXml/*Mapper.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}

！！！注意：
1.@MapperScan 后面写的是扫描的mapper接口的包路径
2.sqlSessionFactoryBean Bean内路径写的是放置 mapper.xml 配置文件的路径
3.dataSource 这个Bean我修改了连接池 默认是tomcat的dataSource

！！！！非常注意的是：
如果properties配置文件中写有相关的jdbc连接池内容和MyBatis信息，上述三个Bean可以省略不写
但是 @MapperScan 一定要写
```

###### 3、添加Mapper.java（接口）和 *Mapper.xml文件
> 3.1、创建mapper文件夹和mapper接口，前提实体类创建完毕：

```java
/**
 * 
 * Fan测试Mapper接口
 * 
 * @author DZG
 * @since V1.0 2016年7月29日
 */
public interface FanMapper {

    List<Fan> getFans();
    
    Fan getById(int id);
    
    String getNameById(int id);
}

注意：
1、类上没有任何的声明
2、方法的名称、输入参数书，与xml定义的一直
```

> 3.2、在resources下创建文件夹XXxml，其中创建 与3.1中对应的 *Mapper.xml文件

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.dzg.mapper.FanMapper">

    <!-- type为实体类Fan，包名已经配置，可以直接写类名 -->
    <resultMap id="fanMap" type="com.dzg.entity.Fan">
        <id property="id" column="id" />
        <result property="nickname" column="nickname" />
        <result property="city" column="city" />
        <result property="sex" column="sex" />
        <result property="province" column="province" />
    </resultMap>

    <select id="getById" resultMap="fanMap" resultType="com.dzg.entity.Fan">
        SELECT *
        FROM weixin_fan
        WHERE id = #{id}
    </select>

    <select id="getFans" resultMap="fanMap" parameterType="string" resultType="list">
        SELECT *
        FROM weixin_fan
        LIMIT 1,10
    </select>

    <select id="getNameById" resultType="string">
        SELECT nickname
        FROM weixin_fan
        WHERE id = #{id}
    </select>
</mapper>

注意：与普通的mybatis配置一样，只是小心：
1、mapper的命名空间（namespace），一定是所对应的接口的包名+类名
2、parameterType和resultMap，与实际匹配
```

###### 4、添加properties文件中一些重要的Mybatis配置信息：

```
#Mybatis指定mapper文件路径，一般是在resources文件夹下
mybatis.mapper-locations=classpath*:myBatisXml/*Mapper.xml
#Mybatis指定实体类文件路径
mybatis.type-aliases-package=com.dzg.entity
```
###### 5、进行测试,Ok 完毕

```java
@RestController
@EnableAutoConfiguration
public class HelloController {
    @Autowired
    FanMapper fanMapper;

    @RequestMapping("/hello") 
    public List<String> hellName(){
		List<Fan> list = fanMapper.getFans();
        List<String> list2 = new ArrayList<String>();
        for(Fan f : list){
        	list2.add(f.toString());
        }
        return list2;
    }
}
```


##### 三、使用注解方式使用MyBatis

###### 1、配置都和 二 一样，使用方法换一下：

> 在mapper接口 中的方法上直接添加ibatis注解：

```java
/**
 * 
 * Fan测试Mapper接口
 * 
 * @author DZG
 * @since V1.0 2016年7月29日
 */
public interface FanMapper {

    List<Fan> getFans();
    
    @Select("SELECT * FROM weixin_fan LIMIT 1,10")
    List<Fan> getFans2();
    
    @Select("SELECT * FROM weixin_fan WHERE id = #{id}")
    Fan getById(int id);
    
    String getNameById(int id);
}


直接在方法上添加注解并输入MyBatis格式的SQL语句即可
注意：如果该方法已经在xml文件中有对应处理，那么通过注解写sql会报错，两者取其一
```

##### 四、分页插件集成：
MyBatis提供了拦截器接口，我们可以实现自己的拦截器，将其作为一个plugin装入到SqlSessionFactory中。
> 找到一个很好用的SQL分页插件：

github地址：https://github.com/pagehelper/Mybatis-PageHelper

myBatis相关工具：http://www.mybatis.tk/

###### 1、添加POM依赖：

```
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>4.1.6</version>
</dependency>
```
###### 2、创建Config 类

```java
@Configuration
@AutoConfigureAfter(MyBatisConfig.class)
public class MyBatisConfig {
	
	@Bean
	public PageHelper pageHelper(){
    	System.out.println("开始配置PageHelper");
    	PageHelper helper = new PageHelper();
    	Properties p = new Properties();
    	p.setProperty("dialect", "mysql"); //数据库类型
        p.setProperty("offsetAsPageNum", "true"); //设置为true时，会将RowBounds第一个参数offset当成pageNum页码使用
        p.setProperty("rowBoundsWithCount", "true"); //设置为true时，使用RowBounds分页会进行count查询 
        p.setProperty("reasonable", "true");
        //启用合理化时，如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页 ,
        //禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据
        helper.setProperties(p);
        return helper;
	}
}

```
###### 3、测试

```java
@RequestMapping("/hello") 
public List<String> hellName(){
	//List<Fan> list = fanService.getFans();
	PageHelper.startPage(1, 5); //已5个分一页，查第一页
	List<Fan> list = fanMapper.getFans();
    List<String> list2 = new ArrayList<String>();
    for(Fan f :list){
    	list2.add(f.toString());
    }
    return list2;
}

目前来看 是有一点问题，分页没成功，应该是分页并没有注册进入，
Boot启动显示Bean已注册，所以比较奇怪，可以花些时间来完善。
```

