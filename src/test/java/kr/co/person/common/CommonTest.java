package kr.co.person.common;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import kr.co.person.BoardProjectApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
public class CommonTest {
	
	@Autowired private Common common;
    
	@Test
	public void isEmail() {
		Assert.assertTrue(common.isEmail("sungjin@naver.com"));
		Assert.assertFalse(common.isEmail("sungjin@naver"));
		Assert.assertFalse(common.isEmail("sungjin"));
	}
}
