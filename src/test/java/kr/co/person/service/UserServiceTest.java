package kr.co.person.service;

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

	@Test
	public void testLoginCheck(){
		user = userService.loginCheck("sungjin", "123456");
		Assert.assertEquals("sungjin", user.getId());
	}

	@Test
	public void testEmailCheck() {
		String email = null;
		Assert.assertEquals("올바른 형식의 메일을 입력해주세요.", userService.emailCheck(email).getMessage());
		email = "";
		Assert.assertEquals("올바른 형식의 메일을 입력해주세요.", userService.emailCheck(email).getMessage());
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
	public void test(){
		OkCheck ok = userService.changePassword(1, "123456", "654321");
		Assert.assertTrue(ok.isBool());
		Assert.assertEquals("비밀번호 수정이 완료되었습니다.", ok.getMessage());
	}
}
