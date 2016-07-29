package com.dzg.service;

import org.springframework.stereotype.Service;

@Service("helloService")
public class HelloService {

	public void home(){
		System.out.println("我运行到service内的方法了");
		System.out.println("这是真的");
	}
}
