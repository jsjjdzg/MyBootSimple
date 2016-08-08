package com.dzg.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dzg.mapper.FanMapper;
import com.dzg.entity.Fan;
import com.dzg.java18study.Fan18Service;
import com.dzg.java18study.FanCityAndSexPredicate;
import com.dzg.rabblitMQ.ReceiverService;
import com.dzg.service.FanService;
import com.dzg.service.HelloService;
import com.github.pagehelper.PageHelper;

@RestController
@EnableAutoConfiguration
public class HelloController {

	@Resource
	HelloService helloService;

	@Autowired
	ReceiverService receiverService;

	@Resource
	FanService fanService;

	@Autowired
	FanMapper fanMapper;

	@Resource
	Fan18Service fan18Service;

	@RequestMapping("/")
	String home() {
		helloService.home();
		return "Hello World!";
	}

	@RequestMapping("/hello")
	public List<String> hellName() {
		// List<Fan> list = fanService.getFans();
		PageHelper.startPage(1, 5); // 已5个分一页，查第一页
		List<Fan> list = fanMapper.getFans();
		List<String> list2 = new ArrayList<String>();
		for (Fan f : list) {
			list2.add(f.toString());
		}
		return list2;
	}

	@RequestMapping("/getFans")
	public List<String> getFans() {
		List<Fan> list = fanService.getFans();
		List<Fan> listNew = fan18Service.filterFans(list, new FanCityAndSexPredicate());
		List<String> list2 = new ArrayList<String>();
		if (listNew.size() > 0 || listNew != null) {
			for (Fan f : listNew) {
				list2.add(f.toString());
			}
		}
		System.out.println(list.size());
		System.out.println(listNew.size());
		return list2;
	}
}
