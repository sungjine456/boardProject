package kr.co.person.common;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import kr.co.person.BoardProjectApplication;
import kr.co.person.common.exception.EmptyStringException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
public class CommonCookieTest {
	
	@Autowired CommonCookie commonCookie;
	@Test
	public void testAes() throws EmptyStringException, Exception{
		String en = commonCookie.aesEncode("sungjin");
		Assert.assertThat(en, is(not("sungjin")));
		String de = commonCookie.aesDecode(en);
		Assert.assertThat(de, is("sungjin"));
	}
}
