### **Spring Boot的几个知识点：** 

##### 1. Spring Boot中几个常用注解介绍
##### 2. 一个Maven+Spring Boot项目基本的包结构形式 
##### 3. 一个简单的在Spring Boot项目集成安全控制 
##### 4. 如何在Spring Boot中记录log日志 

---
#### 一、SpringBoot常用注解：

```
1.@RestController和@Controller 指定一个类，作为控制器的注解 
其中，@RestController等同于 @Controller + @ResponseBody

2.@RequestMapping 方法级别的映射注解， 

3.@EnableAutoConfiguration和@SpringBootApplication是类级别的注解，根据maven依赖的jar来自动猜测完成正确的spring的对应配置，
只要引入了spring-boot-starter-web的依赖，默认会自动配置Spring MVC和tomcat容器，类似自动扫描
其中，@SpringBootApplication 等同于 @EnableAutoConfiguration + @Configuration + @ComponentScan 具体看4,5条

4.@Configuration类级别的注解，
我们用来标识main方法所在的类,完成元数据bean的初始化。 

5.@ComponentScan类级别的注解，
自动扫描加载所有的Spring组件包括Bean注入，一般用在main方法所在的类上 

6.@ImportResource类级别注解，
当我们必须使用一个xml的配置时，使用@ImportResource和@Configuration来标识这个文件资源的类。 

7.@Autowired 和 @Resource 注解，同Spring中的功能
一般结合@ComponentScan注解，来自动注入一个Service或Dao级别的Bean 

8.@Component类级别注解，
用来标识一个组件，比如我自定了一个filter，则需要此注解标识之后，Spring Boot才会正确识别。
其他 @Service @Repository 等同Spring功能

9.@Configuration和@EnableAutoConfiguration区别：
被@Configuration注解标识的类，通常作为一个配置类，类似于一个xml文件，表示在该类中将配置Bean元数据，
其作用类似于Spring里面applicationcontext.xml文件，而@Bean标签，则类似于该xml文件中，声明的一个bean实例。
@EnableAutoConfiguration 标签，表示激活了自动配置，如果maven引入第三方库，比如说一个hsqldb内存库，
那么只要它的jar在classpath之下，这个标签就可以自动配置一个内存库实例，而无须我们手动配置它相关的bean依赖！

所以：

1.一般main方法：

@SpringBootApplication
public class BootSimpleApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootSimpleApplication.class, args);
    }
}

2.一般Controller：

@RestController
@EnableAutoConfiguration
public class HelloController {
    @Resource   
    HelloService helloService;
    @RequestMapping("/")
    String home() {
		helloService.home();
        return "Hello World!";
    }
    @RequestMapping("/hello/{name}") 
    public String hellName(@PathVariable String name){  
        return "Hello "+name;  
    }
}

3.一般Service：

@Service("helloService")
public class HelloService {
    public void execute(){
    	...
    }
}
```



---
#### 二、包结构：

```
官网结构：

com  
 +- example  
     +- myproject  
         +- Application.java  
         |  
         +- entity  
         |   +- Customer.java  
         |   +- CustomerRepository.java  
         |  
         +- service  
         |   +- CustomerService.java  
         | 
         +- controller  
             +- CustomerController.java
             
值得注意的是：
1. 在src/main/java包下的第一层结构中，是必须放一个含有main方法的主启动的类，而且只能有一个main方法；
2. Application类中：@SpringBootApplication注解等于@Configuration配置控制，  
@EnableAutoConfiguration启用自动配置 ，@ComponentScan组件扫描 的总和。
3. 在src/main/resource目录下面，是放置一些配置文件，或模板支持的文件，如JSP，Velocity,Freemaker等，
这里面比较常用或重要的一个文件，就是Spring Boot的集中式配置文件application.properties这个文件了，
这里面给其他大部分的组件，提供了一个可集中管理和配置的中心，包括安全控制，redis，solr，mangodb的连接信息，
以及数据库连接池，容器端口号，jmx，javamail，动态模板等。此外这个目录下默认是可以访问静态资源的，
比如我们的css，js，或者第三方的一些引用文件等。
```
---

#### 三、简单的安全机制：
1. POM.xml中添加：

```
<dependency>
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-security</artifactId>  
</dependency>
```
2. application.properties中添加：

```
security.user.name = XX
security.user.password = XX 即可
```
> 然后访问页面，会出现登录拦截，更复杂的配置，可以分不用角色，在控制范围上，能够拦截到方法级别的权限控制

---

#### 四、日志管理

> 基本使用步骤:
> 在resource目录下的根目录下创建logback.xml，配置相关的log生成规则，log级别，以及日志路径，log的字符编码集，这个非常重要，因为它不支持中文log，需要配置相关的log编码才可以正确记录对应的信息，

> 1.通用配置：

```
<!-- Logback configuration. See http://logback.qos.ch/manual/index.html -->  
<configuration scan="true" scanPeriod="10 seconds">  
    
  <!-- Simple file output -->  
  <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">  
    <!-- encoder defaults to ch.qos.logback.classic.encoder.PatternLayoutEncoder -->  
    <encoder>  
        <pattern>  
            [ %-5level] [%date{yyyy-MM-dd HH:mm:ss}] %logger{96} [%line] - %msg%n  
        </pattern>  
        <charset>UTF-8</charset> <!-- 此处设置字符集 -->  
    </encoder>  
  
    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">  
      <!-- rollover daily 配置日志所生成的目录以及生成文件名的规则 -->  
      <fileNamePattern>logs/mylog-%d{yyyy-MM-dd}.%i.log</fileNamePattern>  
      <timeBasedFileNamingAndTriggeringPolicy  
          class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">  
        <!-- or whenever the file size reaches 64 MB -->  
        <maxFileSize>64 MB</maxFileSize>  
      </timeBasedFileNamingAndTriggeringPolicy>  
    </rollingPolicy>  
  
  
    <filter class="ch.qos.logback.classic.filter.ThresholdFilter">  
      <level>DEBUG</level>  
    </filter>  
    <!-- Safely log to the same file from multiple JVMs. Degrades performance! -->  
    <prudent>true</prudent>  
  </appender>  
  
  
  <!-- Console output -->  
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">  
    <!-- encoder defaults to ch.qos.logback.classic.encoder.PatternLayoutEncoder -->  
      <encoder>  
          <pattern>  
              [ %-5level] [%date{yyyy-MM-dd HH:mm:ss}] %logger{96} [%line] - %msg%n  
          </pattern>  
          <charset>GBK</charset> <!-- 此处设置字符集 -->  
      </encoder>  
    <!-- Only log level WARN and above -->  
    <filter class="ch.qos.logback.classic.filter.ThresholdFilter">  
      <level>WARN</level>  
    </filter>  
  </appender>  
  
  
  <!-- Enable FILE and STDOUT appenders for all log messages.  
       By default, only log at level INFO and above. -->  
  <root level="INFO">  
    <appender-ref ref="FILE" />  
    <appender-ref ref="STDOUT" />  
  </root>  
  
  <!-- For loggers in the these namespaces, log at all levels. -->  
  <logger name="pedestal" level="ALL" />  
  <logger name="hammock-cafe" level="ALL" />  
  <logger name="user" level="ALL" />  
</configuration>
```
> 2.在application.properties中，指定log文件的加载路径，已经配置通用的log日志级别:

```
#指定log的配置文件，以及记录Spring Boot的log级别

logging.config=logback.xml
logging.level.org.springframework.web: INFO
```