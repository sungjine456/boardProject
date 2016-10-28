package kr.co.person.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

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
	
	@Test
	public void testSave() {
		DateTime date = new DateTime();
		User user = new User("test", "test@naver.cmo", "123123", "test", "test", date, date);
		AutoLogin autoLoginSave = autoLoginRepository.save(new AutoLogin("asdasdasd", new DateTime(), user));
		Assert.assertThat(autoLoginSave.getLoginId(), is("asdasdasd"));
		Assert.assertThat(autoLoginSave.getUser().getIdx(), is(user.getIdx()));
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
		Assert.assertThat(autoLogin.getLoginIdx(), is(1));
		Assert.assertThat(autoLogin.getLoginId(), is("asdasdasd"));
	}
}
