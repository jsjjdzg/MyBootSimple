package com.dzg.rabblitMQ;

import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.dzg.entity.Student;
import com.dzg.entity.Teacher;

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
        int i = 2;
        while (i > 0){
            senderService.sendBar2Rabbitmq(new Teacher("这是Teacher:"+String.valueOf(random.nextInt())));
            senderService.sendFoo2Rabbitmq(new Student("这是Student:"+UUID.randomUUID().toString()));
            i--;
        }
    }
}
