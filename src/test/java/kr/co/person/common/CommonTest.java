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
import kr.co.person.pojo.OkCheck;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
public class CommonTest {
	
	@Autowired private Common common;
	@Autowired private Message message;
    
	@Test
	public void testIsEmail() {
		OkCheck check = common.isEmail("sungjin@naver.com");
		Assert.assertThat(check.isBool(), is(true));
		Assert.assertThat(check.getMessage(), is(""));
		check = common.isEmail("sungjin@naver");
		Assert.assertThat(check.isBool(), is(false));
		Assert.assertThat(check.getMessage(), is(message.USER_NO_EMAIL_FORMAT));
		check = common.isEmail("sungjin");
		Assert.assertThat(check.isBool(), is(false));
		Assert.assertThat(check.getMessage(), is(message.USER_NO_EMAIL_FORMAT));
		check = common.isEmail("");
		Assert.assertThat(check.isBool(), is(false));
		Assert.assertThat(check.getMessage(), is(message.USER_NO_EMAIL));
	}
	
	@Test
	public void testAes(){
		String en = common.cookieAesEncode("sungjin");
		Assert.assertThat(en, is(not("sungjin")));
		String de = common.cookieAesDecode(en);
		Assert.assertThat(de, is("sungjin"));
	}
}
