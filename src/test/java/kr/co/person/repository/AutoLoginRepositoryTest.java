package kr.co.person.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.person.BoardProjectApplication;
import kr.co.person.domain.AutoLogin;
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class AutoLoginRepositoryTest {

	@Autowired private AutoLoginRepository autoLoginRepository;
	@Autowired private UserRepository userRepository;
	
	@Test
	public void testSave() {
		User user = userRepository.findOne(1);
		AutoLogin autoLoginSave = autoLoginRepository.save(new AutoLogin("asdasdasdaa", new DateTime(), user));
		Assert.assertThat(autoLoginSave.getLoginId(), is("asdasdasdaa"));
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
	public void testFindByUserIdxAndLoginId(){
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndLoginId(1, "asdasdasd");
		Assert.assertThat(autoLogin.getLoginIdx(), is(1));
		Assert.assertThat(autoLogin.getLoginId(), is("asdasdasd"));
	}
}
