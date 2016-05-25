package kr.co.person.repository;

import java.util.Date;

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
import kr.co.person.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
public class UserRepositoryTest {

	@Autowired private UserRepository userRepository;
    private User user;
 
    @Before
    public void setUp() throws Exception {
    	Date d = new Date();
        user = new User();
        user.setName("woniper");
        user.setEmail("sungjin@naver.com");
        user.setId("sungjin");
        user.setPassword("123456");
        user.setRegDate(d);
        user.setUpDate(d);
    }
 
    @Test
    public void testCreateUser() throws Exception {
        User createUser = userRepository.save(user);
        Assert.assertEquals(createUser.getName(), user.getName());
    }
}
