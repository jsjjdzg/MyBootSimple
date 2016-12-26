package com.dzg.config;

import java.util.Properties;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.pagehelper.PageHelper;

@Configuration
@AutoConfigureAfter(MyBatisConfig.class)
public class MyBatisConfig {
	
	@Bean
	public PageHelper pageHelper(){
		System.out.println("开始配置PageHelper");
		PageHelper helper = new PageHelper();
		Properties p = new Properties();
		p.setProperty("dialect", "mysql"); //数据库类型
        p.setProperty("offsetAsPageNum", "true"); //设置为true时，会将RowBounds第一个参数offset当成pageNum页码使用
        p.setProperty("rowBoundsWithCount", "true"); //设置为true时，使用RowBounds分页会进行count查询 
        p.setProperty("reasonable", "true");
        //启用合理化时，如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页 ,
        //禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据
        helper.setProperties(p);
        return helper;
	}
}
