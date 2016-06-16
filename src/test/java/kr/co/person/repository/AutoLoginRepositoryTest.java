package kr.co.person.repository;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import kr.co.person.BoardProjectApplication;
import kr.co.person.domain.AutoLogin;
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class AutoLoginRepositoryTest {

	@Autowired private AutoLoginRepository autoLoginRepository;
	
	@Test
	public void testSave() {
		Date date = new Date();
		User user = new User();
		user.setIdx(1);
		user.setEmail("sungjin@naver.com");
		user.setId("sungjin");
		user.setName("홍길동");
		user.setPassword("123456");
		user.setRegDate(date);
		user.setUpDate(date);
		AutoLogin autoLogin = new AutoLogin(user, "O", "192.168.0.1", date);
		AutoLogin autoLoginSave = autoLoginRepository.save(autoLogin);
		Assert.assertEquals("O", autoLoginSave.getLoginCheck());
		Assert.assertEquals("192.168.0.1", autoLoginSave.getRegIp());
	}
}
