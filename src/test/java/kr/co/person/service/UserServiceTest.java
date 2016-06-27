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
	public void testFindUserForId(){
		user = userService.findUserForId("");
		Assert.assertNull(user);
		user = userService.findUserForId("abcdabcd");
		Assert.assertNull(user);
		user = userService.findUserForId("sungjin");
		Assert.assertEquals("홍길동", user.getName());
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
		user.setPassword(password);
		user.setRegDate(date);
		user.setUpDate(date);
		OkCheck ok = userService.join(user);
		Assert.assertEquals("회원가입에 성공하셨습니다.", ok.getMessage());
		Assert.assertTrue(ok.isBool());
		user.setId("sungjin1");
		ok = userService.join(user);
		Assert.assertEquals("이미 가입되어있는 회원입니다.", ok.getMessage());
		Assert.assertFalse(ok.isBool());
		user.setEmail("sungjin@naver.com");
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
		user.setPassword(password);
		user.setRegDate(date);
		user.setUpDate(date);
		Assert.assertFalse(userService.autoLoginCheck(user, "192.168.0.1"));
		user.setIdx(1);
		/** 
		 * 자동 로그인의 유효기간이 하루 이기 때문에 false
		 * true로 하려면 testData를 수정한후 테스트
		 */
		Assert.assertFalse(userService.autoLoginCheck(user, "192.168.0.1"));
	}
	
	@Test
	public void testLeave(){
		String garbage = "b94c56f6f1cf92d48e021c573b77fa253eca91e579e308473c0536716c8e7bd6personProject";
		User user = userService.findUserForIdx(1);
		Assert.assertEquals("sungjin@naver.com", user.getEmail());
		Assert.assertEquals("sungjin", user.getId());
		Assert.assertEquals(password, user.getPassword());
		Assert.assertEquals("홍길동", user.getName());
		boolean bool = userService.leave(1, "192.168.0.1");
		user = userService.findUserForIdx(1);
		Assert.assertTrue(bool);
		Assert.assertEquals(garbage, user.getEmail());
		Assert.assertEquals(garbage, user.getId());
		Assert.assertEquals(garbage, user.getPassword());
		Assert.assertEquals(garbage, user.getName());
	}
	
	@Test
	public void testUpdate(){
		user = userService.findUserForIdx(1);
		Assert.assertEquals(user.getRegDate(), user.getUpDate());
		Assert.assertFalse(userService.update(1, "hyun", ""));
		Assert.assertFalse(userService.update(1, "", "hyun@naver.com"));
		Assert.assertFalse(userService.update(2, "hyun", "hyun@naver.com"));
		Assert.assertTrue(userService.update(1, "hyun", "hyun@naver.com"));
		user = userService.findUserForIdx(1);
		Assert.assertNotEquals(user.getRegDate(), user.getUpDate());
	}
	
	@Test
	public void testTranslatePassword(){
		userService.translatePassword("sungjin@naver.com");
		user = userService.findUserForIdx(1);
		Assert.assertNotEquals(password, user.getPassword());
	}
	
	@Test
	public void testAutoLogin(){
		Boolean bool = userService.autoLogin(user, "192.168.0.1");
		Assert.assertFalse(bool);
		bool = userService.autoLogin(userService.findUserForIdx(1), "");
		Assert.assertFalse(bool);
		bool = userService.autoLogin(userService.findUserForIdx(2), "192.168.0.1");
		Assert.assertFalse(bool);
		bool = userService.autoLogin(userService.findUserForIdx(1), "192.168.0.1");
		Assert.assertTrue(bool);
	}
	
	@Test
	public void testAutoLogout(){
		Boolean bool = userService.autoLogout(user, "192.168.0.1");
		Assert.assertFalse(bool);
		bool = userService.autoLogout(userService.findUserForIdx(1), "");
		Assert.assertFalse(bool);
		bool = userService.autoLogout(userService.findUserForIdx(2), "192.168.0.1");
		Assert.assertFalse(bool);
		bool = userService.autoLogout(userService.findUserForIdx(1), "192.168.0.1");
		Assert.assertTrue(bool);
	}
}
