package test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.dzg.BootSimpleApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BootSimpleApplication.class)
@WebAppConfiguration // 使用@WebIntegrationTest注解需要将@WebAppConfiguration注释掉
//@WebIntegrationTest("server.port:8080")
public class HelloControllerTest {

	private RestTemplate rt = new RestTemplate();
	
	@Test
	public void test(){
		String url = "http://localhost:8080/hello";
		String result = rt.postForObject(url, null, String.class);
		System.out.println(result);
		assertNotNull(result);
        assertThat(result, Matchers.containsString("白色萌德"));
	}
}
