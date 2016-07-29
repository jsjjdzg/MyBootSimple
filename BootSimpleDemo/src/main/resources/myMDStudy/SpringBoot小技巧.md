##### 一、SpringBoot启动加载数据
> Spring Boot 为我们提供了一个方法，通过实现接口 CommandLineRunner 来实现

```java
@Component
@Order(value=2)
public class StarterOne implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>> StarterOne启动了<<<<");
    }

}

@Component
@Order(value=2)
public class StarterTwo implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>> StarterTwo启动了<<<<");
    }

}

其中 @Order 可写可不写 写了就是按照里面value的顺序执行
```
##### 二、SpringBoot中的小tips：
**如果properties配置文件中和mian方法类中配置了@Bean，会优先使用@Bean中的内容**