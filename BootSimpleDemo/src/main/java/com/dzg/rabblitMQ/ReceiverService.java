package com.dzg.rabblitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dzg.entity.Student;
import com.dzg.entity.Teacher;

/**
 * 消息接收类
 * 类描述
 * @author DZG
 * @since V1.0 2016年7月28日
 */
@Component
public class ReceiverService {

    @RabbitListener(queues = "queue.dzgSS")//这的队列名称不能写错成没有的
    public void receiveFooQueue(Student s) {
        System.out.println("Received Student<" + s.getName() + ">");
    }

    @RabbitListener(queues = "queue.dzgTT")//这的队列名称不能写错成没有的
    public void receiveBarQueue(Teacher t) {
        System.out.println("Received Teacher<" + t.getName() + ">");
    }
}
