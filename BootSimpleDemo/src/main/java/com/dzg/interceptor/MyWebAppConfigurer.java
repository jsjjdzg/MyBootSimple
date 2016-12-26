package com.dzg.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * MyWebAppConfigurer测试类
 * 
 * @author DZG
 * @since V1.0 2016年7月29日
 */
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter{

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new MyInterceptorOne()).addPathPatterns("/**");
        registry.addInterceptor(new MyInterceptorTwo()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
