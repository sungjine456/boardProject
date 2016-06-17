package kr.co.person.service;

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
import kr.co.person.domain.OkCheck;
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class UserServiceTest {

	@Autowired
	private UserService userService;
	private User user;
	// 비밀번호 123456을 암호화한 형태
    private String password = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92personProject";

	@Test
	public void testLoginCheck(){
		user = userService.loginCheck("sungjin", "123456");
		Assert.assertEquals("sungjin", user.getId());
	}
	
	@Test
	public void testIdCheck() {
		String id = null;
		Assert.assertEquals("아이디를 입력해주세요.", userService.idCheck(id).getMessage());
		id = "";
		Assert.assertEquals("아이디를 입력해주세요.", userService.idCheck(id).getMessage());
		id = "sungjin";
		Assert.assertEquals("이미 가입되어 있는 아이디입니다.", userService.idCheck(id).getMessage());
		id = "sungjin123";
		Assert.assertEquals("가입 가능한 아이디입니다.", userService.idCheck(id).getMessage());
	}

	@Test
	public void testEmailCheck() {
		String email = null;
		Assert.assertEquals("메일을 입력해주세요.", userService.emailCheck(email).getMessage());
		email = "";
		Assert.assertEquals("메일을 입력해주세요.", userService.emailCheck(email).getMessage());
		email = "tjdwls@naver.com";
		Assert.assertEquals("가입 가능한 이메일입니다.", userService.emailCheck(email).getMessage());
		email = "sungjin@naver.com";
		Assert.assertEquals("이미 가입되어 있는 이메일입니다.", userService.emailCheck(email).getMessage());
		email = "sungjin";
		Assert.assertEquals("올바른 형식의 메일을 입력해주세요.", userService.emailCheck(email).getMessage());
	}

	@Test
	public void testFindUserForIdx() {
		user = userService.findUserForIdx(1);
		Assert.assertEquals("sungjin", user.getId());
	}
	
	@Test
	public void testChangePassword(){
		user = userService.findUserForIdx(1);
		Assert.assertEquals(password, user.getPassword());
		OkCheck ok = userService.changePassword(1, "123456", "654321");
		Assert.assertTrue(ok.isBool());
		Assert.assertEquals("비밀번호 수정이 완료되었습니다.", ok.getMessage());
		user = userService.findUserForIdx(1);
		Assert.assertEquals("481f6cc0511143ccdd7e2d1b1b94faf0a700a8b49cd13922a70b5ae28acaa8c5personProject", user.getPassword());
	}
	
	@Test
	public void testJoin(){
		user = new User();
		Date date = new Date();
		user.setEmail("sungjin1@naver.com");
		user.setId("sungjin1");
		user.setName("홍길동");
		user.setPassword("123456");
		user.setRegDate(date);
		user.setUpDate(date);
		OkCheck ok = userService.join(user);
		Assert.assertEquals("회원가입에 성공하셨습니다.", ok.getMessage());
		Assert.assertTrue(ok.isBool());
		user.setEmail("sungjin@naver.com");
		user.setId("sungjin1");
		user.setName("홍길동");
		user.setPassword("123456");
		user.setRegDate(date);
		user.setUpDate(date);
		ok = userService.join(user);
		Assert.assertEquals("이미 가입되어있는 회원입니다.", ok.getMessage());
		Assert.assertFalse(ok.isBool());
	}
	
	@Test
	public void testAutoLoginCheck(){
		Date date = new Date();
		User user = new User();
		Assert.assertFalse(userService.autoLoginCheck(null, "192.168.0.1"));
		Assert.assertFalse(userService.autoLoginCheck(user, ""));
		user.setIdx(5);
		user.setEmail("sungjin@naver.com");
		user.setId("sungjin");
		user.setName("홍길동");
		user.setPassword("123456");
		user.setRegDate(date);
		user.setUpDate(date);
		Assert.assertFalse(userService.autoLoginCheck(user, "192.168.0.1"));
		user.setIdx(1);
		Assert.assertTrue(userService.autoLoginCheck(user, "192.168.0.1"));
	}
}
