package kr.co.person.repository;

import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import kr.co.person.BoardProjectApplication;
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
public class UserRepositoryTest {

	@Autowired private UserRepository userRepository;
    private User user;
 
    @Before
    public void setUp(){
    	Date d = new Date();
        user = new User();
        user.setName("woniper");
        user.setEmail("sungjin@naver.com");
        user.setId("sungjin");
        user.setPassword("123456");
        user.setRegDate(d);
        user.setUpDate(d);
        userRepository.save(user);
    }
    
    @After
    public void tearDown() {
    	userRepository.delete(user);
    }
 
    @Test
    public void testUserICheck() {
    	String id = userRepository.userIdCheck("sungjin");
    	Assert.assertEquals("sungjin", id);
    }
    
    @Test
    public void testUserEmialCheck() {
    	String email = userRepository.userEmialCheck("sungjin@naver.com");
    	Assert.assertEquals("sungjin@naver.com", email);
    }

    @Test
    public void testLoginCheck() {
    	user = userRepository.loginCheck("sungjin", "123456");
    	Assert.assertEquals("sungjin", user.getId());
    }
}
