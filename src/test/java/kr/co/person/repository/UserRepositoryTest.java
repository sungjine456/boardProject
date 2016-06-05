package kr.co.person.repository;

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
    // 비밀번호 123456을 암호화한 형태
    private String password = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92personProject";
 
    @Test
    public void testUserICheck() {
    	String id = userRepository.userIdCheck("sungjin");
    	Assert.assertEquals("sungjin", id);
    }
    
	@Test
	public void testFindOne() {
		user = userRepository.findOne(1);
		Assert.assertEquals("sungjin", user.getId());
	}
    
    @Test
    public void testUserEmialCheck() {
    	String email = userRepository.userEmialCheck("sungjin@naver.com");
    	Assert.assertEquals("sungjin@naver.com", email);
    	email = userRepository.userEmialCheck("ass@naver.com");
    	Assert.assertNull(email);
    }

    @Test
    public void testLoginCheck() {
    	user = userRepository.findByIdAndPassword("sungjin", password);
    	Assert.assertEquals("홍길동", user.getName());
    	Assert.assertEquals("sungjin@naver.com", user.getEmail());
    	Assert.assertEquals("sungjin", user.getId());
    	Assert.assertEquals(password, user.getPassword());
    }
    
    @Test
    public void testFindPassword(){
    	user = userRepository.findByEmail("sungjin@naver.com");
    	Assert.assertEquals("홍길동", user.getName());
    	Assert.assertEquals("sungjin@naver.com", user.getEmail());
    	Assert.assertEquals("sungjin", user.getId());
    	Assert.assertEquals(password, user.getPassword());
    }
    
    @Test
    public void testDelete(){
    	user = userRepository.findOne(1);
		Assert.assertEquals("sungjin", user.getId());
		userRepository.delete(1);
		user = userRepository.findOne(1);
		Assert.assertNull(user);
    }
}
