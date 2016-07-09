package kr.co.person.common;

import static org.hamcrest.CoreMatchers.*;

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
	public void testIsEmail() {
		Assert.assertThat(common.isEmail("sungjin@naver.com"), is(true));
		Assert.assertThat(common.isEmail("sungjin@naver"), is(false));
		Assert.assertThat(common.isEmail("sungjin"), is(false));
	}
	
	@Test
	public void testAes(){
		String key = "personProjectByJin";
		String en = common.cookieAesEncode(key, "sungjin");
		Assert.assertThat(en, is(not("sungjin")));
		String de = common.cookieAesDecode(key, en);
		Assert.assertThat(de, is("sungjin"));
	}
}
