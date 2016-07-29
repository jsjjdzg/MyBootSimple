package com.dzg.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 
 * HttpSessionListener监听器测试类
 * 使用@WebListener注解，实现HttpSessionListener接口
 * 监听Session的创建与销毁
 * 
 * @author DZG
 * @since V1.0 2016年7月29日
 */
@WebListener
public class MyHttpSessionListener implements HttpSessionListener{

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Session创建");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Session销毁");
	}

}
