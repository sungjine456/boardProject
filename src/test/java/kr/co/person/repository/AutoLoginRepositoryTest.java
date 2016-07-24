package kr.co.person.repository;

import static org.hamcrest.CoreMatchers.is;

import org.joda.time.DateTime;
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
		AutoLogin autoLogin = new AutoLogin("asdasd", new DateTime(), "O", user);
		AutoLogin autoLoginSave = autoLoginRepository.save(autoLogin);
		Assert.assertThat(autoLoginSave.getLoginCheck(), is("O"));
	}
	
	@Test
	public void testFindByUserIdx(){
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndLoginId(1, "asdasdasd");
		Assert.assertThat(autoLogin.getLoginCheck(), is("O"));
	}
}
