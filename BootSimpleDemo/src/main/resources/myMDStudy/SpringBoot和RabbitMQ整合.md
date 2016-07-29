#### SpringBoot和RabbitMQ整合:
##### 1、导包

```java
POM文件中添加：
<dependency>  
	<groupId>org.springframework.boot</groupId>  
	<artifactId>spring-boot-starter-amqp</artifactId>  
</dependency>
```
##### 2.配置文件更改：

```java
#rabbitMQ相关配置，如果已经配置@Bean该配置，这里可以不写
spring.rabbitmq.host=192.168.1.59
spring.rabbitmq.port=5673
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
spring.rabbitmq.virtual-host=/

logging.level.org.springframework.amqp=ERROR
logging.level.com.patterncat=INFO

#spring.rabbitmq.dynamic 是否创建AmqpAdmin bean. 默认为: true
#spring.rabbitmq.listener.acknowledge-mode 指定Acknowledge的模式.
#spring.rabbitmq.listener.auto-startup 是否在启动时就启动mq，默认: true
#spring.rabbitmq.listener.concurrency 指定最小的消费者数量.
#spring.rabbitmq.listener.max-concurrency 指定最大的消费者数量.
#spring.rabbitmq.listener.prefetch 指定一个请求能处理多少个消息，如果有事务的话，必须大于等于transaction数量.
#spring.rabbitmq.listener.transaction-size 指定一个事务处理的消息数量，最好是小于等于prefetch的数量.
#spring.rabbitmq.requested-heartbeat 指定心跳超时，0为不指定.
#spring.rabbitmq.ssl.enabled 是否开始SSL，默认: false
#spring.rabbitmq.ssl.key-store 指定持有SSL certificate的key store的路径
#spring.rabbitmq.ssl.key-store-password 指定访问key store的密码.
#spring.rabbitmq.ssl.trust-store 指定持有SSL certificates的Trust store.
#spring.rabbitmq.ssl.trust-store-password 指定访问trust store的密码
```

##### 3、java代码完成
###### 3.1、生产者

```java
/**
 * 
 * 生产者配置类
 * 
 * @author DZG
 * @since V1.0 2016年7月28日
 */
@Configuration
public class ProducerConfig {

	@Bean
	RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	// 声明创建队列dzgS
	@Bean
	Queue queueFoo(RabbitAdmin rabbitAdmin) {
		Queue queue = new Queue("queue.dzgS", true);
		rabbitAdmin.declareQueue(queue);// 声明操作
		return queue;
	}// 要创建新的队列这个要新建一个

	// 声明创建队列dzgT
	@Bean
	Queue queueBar(RabbitAdmin rabbitAdmin) {
		Queue queue = new Queue("queue.dzgT", true);
		rabbitAdmin.declareQueue(queue);// 声明操作
		return queue;
	}

	@Bean
	TopicExchange exchange(RabbitAdmin rabbitAdmin) {
		TopicExchange topicExchange = new TopicExchange("exchange");
		rabbitAdmin.declareExchange(topicExchange);
		return topicExchange;
	}

	@Bean
	Binding bindingExchangeFoo(Queue queueFoo, TopicExchange exchange, RabbitAdmin rabbitAdmin) {
		Binding binding = BindingBuilder.bind(queueFoo).to(exchange).with("queue.dzgS");
		rabbitAdmin.declareBinding(binding);
		return binding;
	}// 要创建新的队列这个要新建一个

	@Bean
	Binding bindingExchangeBar(Queue queueBar, TopicExchange exchange, RabbitAdmin rabbitAdmin) {
		Binding binding = BindingBuilder.bind(queueBar).to(exchange).with("queue.dzgT");
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	/**
	 * 生产者用
	 * 
	 * @return
	 */
	@Bean
	public RabbitMessagingTemplate rabbitMessagingTemplate(RabbitTemplate rabbitTemplate) {
		RabbitMessagingTemplate rabbitMessagingTemplate = new RabbitMessagingTemplate();
		rabbitMessagingTemplate.setMessageConverter(jackson2Converter());
		rabbitMessagingTemplate.setRabbitTemplate(rabbitTemplate);
		return rabbitMessagingTemplate;
	}

	@Bean
	public MappingJackson2MessageConverter jackson2Converter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		return converter;
	}

	// 创建链接的Bean，一般在配置文件中配置即可，这里可写可不写
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses("192.168.1.59:5673");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		connectionFactory.setVirtualHost("/");
		connectionFactory.setPublisherConfirms(true); // 必须要设置
		return connectionFactory;
	}
}

```

###### 3.2、消费者

```java
/**
 * 
 * 消费者配置
 * @author DZG
 * @since V1.0 2016年7月28日
 */
@Configuration
@EnableRabbit
public class ConsumerConfig implements RabbitListenerConfigurer {

    @Bean
    public DefaultMessageHandlerMethodFactory myHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(new MappingJackson2MessageConverter());
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // factory.setPrefetchCount(5);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(myHandlerMethodFactory());
    }

}
```

###### 3.3、消息发送

```java
/**
 * 
 * 消息收发-发送服务
 * @author DZG
 * @since V1.0 2016年7月28日
 */
@Component
public class SenderService {

    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;

    public void sendFoo2Rabbitmq(final Student s) {
        this.rabbitMessagingTemplate.convertAndSend("exchange", "queue.dzgS", s);
    }

    public void sendBar2Rabbitmq(final Teacher t){
        this.rabbitMessagingTemplate.convertAndSend("exchange", "queue.dzgT", t);
    }
}
```

###### 3.4、消息接收

```java
/**
 * 消息接收类
 * 类描述
 * @author DZG
 * @since V1.0 2016年7月28日
 */
@Component
public class ReceiverService {

    @RabbitListener(queues = "queue.dzgS")//这的队列名称不能写错成没有的
    public void receiveFooQueue(Student s) {
        System.out.println("Received Student<" + s.getName() + ">");
    }

    @RabbitListener(queues = "queue.dzgT")//这的队列名称不能写错成没有的
    public void receiveBarQueue(Teacher t) {
        System.out.println("Received Teacher<" + t.getName() + ">");
    }
}
```

###### 3.5、测试发送类，在SpringBoot启动时开始

```java
/**
 * 
 * Boot启动时调用测试
 * @author DZG
 * @since V1.0 2016年7月28日
 */
@Component
public class RabbitmqdemoApplication implements CommandLineRunner {

    @Autowired
    SenderService senderService;

    @Override
    public void run(String... strings) throws Exception {
        Random random = new Random();
        while (true){
            senderService.sendBar2Rabbitmq(new Teacher("这是Teacher:"+String.valueOf(random.nextInt())));
            senderService.sendFoo2Rabbitmq(new Student("这是Student:"+UUID.randomUUID().toString()));
            Thread.sleep(5000);
        }
    }
}
```

###### 3.6、启动主main类
> 可以从 RabbitMQ管理页面看到创建的队列和队列信息，接收类一直扫描接收接口一直会有信息出来





