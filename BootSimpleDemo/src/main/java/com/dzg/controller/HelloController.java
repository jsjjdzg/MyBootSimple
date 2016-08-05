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
		List<Fan> list = helloService.getFans();
		List<String> list2 = new ArrayList<String>();
		if (list.size() > 0 || list != null) {
			for (Fan f : list) {
				list2.add(f.toString());
			}
		}
		return list2;
	}
}
