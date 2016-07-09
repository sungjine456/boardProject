package kr.co.person.repository;

import static org.hamcrest.CoreMatchers.*;

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

	@Test
	public void testSave() {
		user = userRepository.findOne(1);
		AutoLogin autoLogin = new AutoLogin(user, "O", "192.168.0.1", new Date());
		AutoLogin autoLoginSave = autoLoginRepository.save(autoLogin);
		Assert.assertThat(autoLoginSave.getLoginCheck(), is("O"));
		Assert.assertThat(autoLoginSave.getRegIp(), is("192.168.0.1"));
	}
	
	@Test
	public void testFindByUserIdxAndRegIp(){
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndRegIp(1, "192.168.0.1");
		Assert.assertThat(autoLogin.getLoginCheck(), is("O"));
		Assert.assertThat(autoLogin.getRegIp(), is("192.168.0.1"));
	}
}
