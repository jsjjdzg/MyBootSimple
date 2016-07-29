#### 一、SpringBoot拦截器的使用：
> 实现自定义拦截器只需要3步： 

```
1、创建我们自己的拦截器类并实现 HandlerInterceptor 接口。
2、创建一个Java类继承WebMvcConfigurerAdapter，并重写 addInterceptors 方法。 
3、实例化我们自定义的拦截器，然后将对像手动添加到拦截器链中（在addInterceptors方法中添加）
```
###### 1、创建拦截器：

```java
/**
 * 
 * 拦截器测试类
 * 
 * @author DZG
 * @since V1.0 2016年7月29日
 */
public class MyInterceptorOne implements HandlerInterceptor{
	
	//在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("One的afterCompletion");
	}
	
	//请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("One的postHandle");
	}

	//在请求处理之前进行调用（Controller方法调用之前）
	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("One的preHandle");
		return true; // 只有返回true才会继续向下执行，返回false取消当前请求
	}

}

```

###### 2和3、创建一个Java类继承WebMvcConfigurerAdapter，并重写 addInterceptors 方法，实例化我们自定义的拦截器，然后将对像手动添加到拦截器链中（在addInterceptors方法中添加）

```java
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
```

###### 4、测试完成

#### 二、SpringBoot过滤器的使用
###### 1、创建Filter过滤器，实现Filter接口

```java
/**
 * 
 * MyFilter过滤器测试类
 * @WebFilter 将一个实现了javax.servlet.Filter接口的类定义为过滤器
 * 属性filterName声明过滤器的名称,可选
 * 属性urlPatterns指定要过滤 的URL模式,也可使用属性value来声明.(指定要过滤的URL模式是必选属性)
 * 
 * @author DZG
 * @since V1.0 2016年7月29日
 */
@WebFilter(filterName="myFilter",urlPatterns="/*")
public class MyFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("过滤器销毁");
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("过滤器开始过滤");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("过滤器初始化");
	}

}

注意 需要 @WebFilter 注解
```
#### 三、SpringBoot监听器的使用
###### 1、创建ServletContextListener，实现ServletContextListener接口

```java
/**
 * 
 * ServletContextListener监听器测试类
 * 使用@WebListener注解，实现ServletContextListener接口
 * 监听ServletContext的创建与销毁
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

注意 需要 @WebListener 注解
```
###### 2、创建HttpSessionListener，实现HttpSessionListener接口

```
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

注意 需要 @WebListener 注解
```

#### 四、测试
###### 1、拦截器可以直接工作
###### 2、过滤器和监听器需要 main 主类添加

```
@ServletComponentScan 该注解才可以工作
```
