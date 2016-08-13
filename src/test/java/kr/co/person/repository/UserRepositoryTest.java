package kr.co.person.repository;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import kr.co.person.BoardProjectApplication;
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class UserRepositoryTest {

	@Autowired private UserRepository userRepository;
    private User user;
    // 비밀번호 123123을 암호화한 형태
    private String password = "96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1epersonProject";
 
    @Test
    public void testFindById() {
    	String id = userRepository.findById("sungjin").getId();
    	Assert.assertThat(id, is("sungjin"));
    }
    
	@Test
	public void testFindOne() {
		user = userRepository.findOne(1);
		Assert.assertThat(user.getId(), is("sungjin"));
	}
    
    @Test
    public void testFindByIdAndPassword() {
    	user = userRepository.findByIdAndPassword("sungjin", password);
    	Assert.assertThat(user.getName(), is("hong"));
    	Assert.assertThat(user.getEmail(), is("sungjin@naver.com"));
    	Assert.assertThat(user.getId(), is("sungjin"));
    	Assert.assertThat(user.getPassword(), is(password));
    }
    
    @Test
    public void testFindByEmail(){
    	user = userRepository.findByEmail("sungjin@naver.com");
    	Assert.assertThat(user.getName(), is("hong"));
    	Assert.assertThat(user.getEmail(), is("sungjin@naver.com"));
    	Assert.assertThat(user.getId(), is("sungjin"));
    	Assert.assertThat(user.getPassword(), is(password));
    	
    	User user = userRepository.findByEmail("ass@naver.com");
    	Assert.assertThat(user, is(nullValue()));
    }
}
