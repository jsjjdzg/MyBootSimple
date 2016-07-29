package com.dzg.rabblitMQ;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dzg.entity.Student;
import com.dzg.entity.Teacher;

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
        this.rabbitMessagingTemplate.convertAndSend("exchange", "queue.dzgSS", s);
    }

    public void sendBar2Rabbitmq(final Teacher t){
        this.rabbitMessagingTemplate.convertAndSend("exchange", "queue.dzgTT", t);
    }
}
