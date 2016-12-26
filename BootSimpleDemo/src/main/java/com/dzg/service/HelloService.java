package com.dzg.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import com.dzg.dao.FanDao;
import com.dzg.entity.Fan;

@Service("helloService")
public class HelloService {
	
	private final Logger logger = Logger.getLogger(HelloService.class);
	
	@Resource
	FanDao fanDao;
	
	public void home(){
		System.out.println("我运行到service内的方法了");
		System.out.println("这是真的");
	}
	
	public List<Fan> getFans(){
		List<Fan> list = new ArrayList<Fan>();
		try {
			list = fanDao.getFans();
		} catch (Throwable e) {
			logger.error("shit! this has a error");
		}
		return list;
	}
}
