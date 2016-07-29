Spring Boot提供了一个强大的一键式Spring的集成开发环境，能够单独进行一个Spring应用的开发，其中：

1.集中式配置（application.properties）+注解，大大简化了开发流程

2.内嵌的Tomcat和Jetty容器，可直接打成jar包启动，无需提供Java war包以及繁琐的Web配置 

3.提供了Spring各个插件的基于Maven的pom模板配置，开箱即用，便利无比。 

4.可以在任何你想自动化配置的地方，实现可能 

5.提供更多的企业级开发特性，如何系统监控，健康诊断，权限控制 

6.无冗余代码生成和XML强制配置 

7.提供支持强大的Restfult风格的编码，非常简洁 




#### 基本环境搭建流程：
1. 使用IDE新建一个基本maven项目
2. 修改POM文件核心区域：
```
<parent><!-- 父依赖 -->
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-parent</artifactId>
	<version>1.3.6.RELEASE</version>
	<relativePath /> <!-- lookup parent from repository -->
</parent>

<properties><!-- 基本设置 包括jdk版本 -->
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
	<java.version>1.8</java.version>
</properties>

<dependencies><!-- 需要导入的其他boot包 -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
	</dependency>

	<dependency><!-- 导入tomcat包 -->
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-tomcat</artifactId>
		<scope>provided</scope>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-test</artifactId>
		<scope>test</scope>
	</dependency>
</dependencies>

<build><!-- 导入maven构建包 -->
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
	</plugins>
</build>
```
3. 等待maven构建，使用maven的 Update Project 更新项目
4. Ok，现在可以写代码，一般代码结构：

```
eg：
src/main/java
  |- com.XXX.XX（包）
      |-  controller（包）
      |-  service（包）
      |-  BootSimpleApplication（启动类）

1. BootSimpleApplication类:

@SpringBootApplication
public class BootSimpleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootSimpleApplication.class, args);
	}
}

2. controller中HelloWorld类:

@Controller
@EnableAutoConfiguration
public class HelloController {
	
    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }
	
    @RequestMapping("/hello/{name}") 
    @ResponseBody
    public String hellName(@PathVariable String name){  
        return "Hello "+name;  
    }
}
```
> 直接启动SpringBootApplication类中的main方法
> 就可以在浏览器中输入http://localhost:8080/ 就会有 Hello World返回
> 基本环境搭建完成


---
> 遇到的问题：

```
1. IDE发生很多莫名其妙的错误：
    作为开发，第一件事关防火墙关更新关关闭显示器和硬盘，
    把用户的安全级别降低到最低，并注意 管理员权限

2. 尽量按照官方文档来进行环境搭建：
    毕竟网上“大神”很多
```

