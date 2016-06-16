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
	@Autowired private UserRepository userRepository;
	
	private User user;
	private Date date = new Date();

	@Test
	public void testSave() {
		user = userRepository.findOne(1);
		AutoLogin autoLogin = new AutoLogin(user, "O", "192.168.0.1", date);
		AutoLogin autoLoginSave = autoLoginRepository.save(autoLogin);
		Assert.assertEquals("O", autoLoginSave.getLoginCheck());
		Assert.assertEquals("192.168.0.1", autoLoginSave.getRegIp());
	}
	
	@Test
	public void testFind(){
		user = userRepository.findOne(1);
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndRegIpAndRegDate(1, "192.168.0.1", user.getRegDate());
		Assert.assertEquals("192.168.0.1", autoLogin.getRegIp());
		Assert.assertEquals("O", autoLogin.getLoginCheck());
	}
}
