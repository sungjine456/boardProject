package kr.co.person.repository;

import static org.hamcrest.CoreMatchers.*;

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
	
	@Test
	public void testSave() {
		User user = userRepository.findOne(1);
		Assert.assertThat(user.getIdx(), is(1));
		Assert.assertThat(user.getName(), is("홍길동"));
		Assert.assertThat(user.getEmail(), is("sungjin@naver.com"));
		AutoLogin autoLoginSave = autoLoginRepository.save(new AutoLogin("asdasdasd", new DateTime(), user));
		Assert.assertThat(autoLoginSave.getLoginId(), is("asdasdasd"));
	}
	
	@Test
	public void testDelete(){
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndLoginId(1, "asdasdasd");
		Assert.assertThat(autoLogin, is(notNullValue()));
		autoLoginRepository.delete(autoLogin);
		autoLogin = autoLoginRepository.findByUserIdxAndLoginId(1, "asdasdasd");
		Assert.assertThat(autoLogin, is(nullValue()));
	}
	
	@Test
	public void testFindByUserIdx(){
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndLoginId(1, "asdasdasd");
		Assert.assertThat(autoLogin.getLoginId(), is("asdasdasd"));
	}
}
