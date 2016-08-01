package com.dzg.rabblitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

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
		Queue queue = new Queue("queue.dzgSS", true);
		rabbitAdmin.declareQueue(queue);// 声明操作
		return queue;
	}// 要创建新的队列这个要新建一个

	// 声明创建队列dzgT
	@Bean
	Queue queueBar(RabbitAdmin rabbitAdmin) {
		Queue queue = new Queue("queue.dzgTT", true);
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
		Binding binding = BindingBuilder.bind(queueFoo).to(exchange).with("queue.dzgSS");
		rabbitAdmin.declareBinding(binding);
		return binding;
	}// 要创建新的队列这个要新建一个

	@Bean
	Binding bindingExchangeBar(Queue queueBar, TopicExchange exchange, RabbitAdmin rabbitAdmin) {
		Binding binding = BindingBuilder.bind(queueBar).to(exchange).with("queue.dzgTT");
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
	// 1.创建连接工厂ConnectionFactory
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses("192.168.1.59:5673");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		connectionFactory.setVirtualHost("/");
		connectionFactory.setPublisherConfirms(true); 
		// 必须要设置(如果配置文件中配置了相关信息，但是这里不设置自动创建的connectionFactory无法完成回调)
		return connectionFactory;
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // 必须是prototype类型
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		return template;
	}
}
