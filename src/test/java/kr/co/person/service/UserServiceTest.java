package kr.co.person.service;

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
public class UserServiceTest {
	
	@Autowired private UserService userService;
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
        userService.join(user);
    }
    
    @After
    public void tearDown() {
    	userService.leave(user);
    }

    @Test
    public void testLoginCheck(){
    	user = userService.loginCheck("sungjin", "123456");
    	Assert.assertEquals("sungjin", user.getId());
    }
    
    @Test
    public void testEmailCheck(){
    	String email = null;
    	Assert.assertEquals("올바른 형식의 메일을 입력해주세요.", userService.emailCheck(email).getMessage());
    	email = "";
    	Assert.assertEquals("올바른 형식의 메일을 입력해주세요.", userService.emailCheck(email).getMessage());
    	email = "tjdwls@naver.com";
    	Assert.assertEquals("가입 가능한 이메일입니다.", userService.emailCheck(email).getMessage());
    	email = user.getEmail();
    	Assert.assertEquals("이미 가입되어 있는 이메일입니다.", userService.emailCheck(email).getMessage());
    	email = "sungjin";
    	Assert.assertEquals("올바른 형식의 메일을 입력해주세요.", userService.emailCheck(email).getMessage());
    }
}
