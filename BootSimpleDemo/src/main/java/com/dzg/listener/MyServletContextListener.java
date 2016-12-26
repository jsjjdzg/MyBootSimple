package com.dzg.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 
 * ServletContextListener监听器测试类
 * 使用@WebListener注解，实现ServletContextListener接口
 * 监听ServletContext的创建与销毁
 * 
 * @author DZG
 * @since V1.0 2016年7月29日
 */
@WebListener
public class MyServletContextListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("ServletContex销毁");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("ServletContex初始化");
        System.out.println(arg0.getServletContext().getServerInfo());
	}

}
