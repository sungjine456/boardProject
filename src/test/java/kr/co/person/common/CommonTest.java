package kr.co.person.common;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.person.BoardProjectApplication;
import kr.co.person.common.exception.EmptyStringException;
import kr.co.person.pojo.OkCheck;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
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
	
	@Test(expected=EmptyStringException.class)
	public void testPasswordEncryptionEmptyException() throws EmptyStringException, NoSuchAlgorithmException {
		common.passwordEncryption("");
	}
	
	@Test
	public void testPasswordEncryption() throws EmptyStringException, NoSuchAlgorithmException {
		String password = "123123";
		String passwordEncryption = common.passwordEncryption(password);
		Assert.assertThat(password, not(passwordEncryption));
	}
}
