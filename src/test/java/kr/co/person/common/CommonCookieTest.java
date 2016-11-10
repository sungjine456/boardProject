package kr.co.person.common;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.person.BoardProjectApplication;
import kr.co.person.common.exception.EmptyStringException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
public class CommonCookieTest {
	
	@Autowired CommonCookie commonCookie;
	@Test
	public void testAes() throws EmptyStringException, Exception{
		String testStr = "sungjin";
		String en = commonCookie.aesEncode(testStr);
		Assert.assertThat(en, is(not(testStr)));
		String de = commonCookie.aesDecode(en);
		Assert.assertThat(de, is(testStr));
	}
}
