package kr.co.person.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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
import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class UserServiceTest {

	@Autowired
	private UserService userService;
	private User user;
	// 비밀번호123123을 암호화한 형태
    private String password = "96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1epersonProject";
    private DateTime date = new DateTime();

	@Test
	public void testLoginCheck(){
		user = userService.loginCheck("sungjin", "123123");
		Assert.assertEquals("sungjin", user.getId());
	}
	
	@Test
	public void testIdCheck() {
		Assert.assertThat(userService.idCheck(null).getMessage(), is("아이디를 입력해주세요."));
		Assert.assertThat(userService.idCheck("").getMessage(), is("아이디를 입력해주세요."));
		Assert.assertThat(userService.idCheck("sungjin").getMessage(), is("이미 가입되어 있는 아이디입니다."));
		Assert.assertThat(userService.idCheck("sungjin123").getMessage(), is("가입 가능한 아이디입니다."));
	}

	@Test
	public void testEmailCheck() {
		Assert.assertThat(userService.emailCheck(null).getMessage(), is("메일을 입력해주세요."));
		Assert.assertThat(userService.emailCheck("").getMessage(), is("메일을 입력해주세요."));
		Assert.assertThat(userService.emailCheck("tjdwls@naver.com").getMessage(), is("가입 가능한 이메일입니다."));
		Assert.assertThat(userService.emailCheck("sungjin@naver.com").getMessage(), is("이미 가입되어 있는 이메일입니다."));
		Assert.assertThat(userService.emailCheck("sungjin").getMessage(), is("올바른 형식의 메일을 입력해주세요."));
	}

	@Test
	public void testFindUserForIdx() {
		user = userService.findUserForIdx(1);
		Assert.assertThat(user.getName(), is("홍길동"));
		Assert.assertThat(user.getId(), is("sungjin"));
		Assert.assertThat(user.getEmail(), is("sungjin@naver.com"));
	}
	
	@Test
	public void testFindUserForId(){
		user = userService.findUserForId("");
		Assert.assertThat(user, is(nullValue()));
		user = userService.findUserForId("abcdabcd");
		Assert.assertThat(user, is(nullValue()));
		user = userService.findUserForId("sungjin");
		Assert.assertThat(user.getName(), is("홍길동"));
		Assert.assertThat(user.getId(), is("sungjin"));
		Assert.assertThat(user.getEmail(), is("sungjin@naver.com"));
	}
	
	@Test
	public void testChangePassword(){
		user = userService.findUserForIdx(1);
		Assert.assertThat(user.getPassword(), is(password));
		OkCheck ok = userService.changePassword(1, "123123", "654321");
		Assert.assertThat(ok.isBool(), is(true));
		Assert.assertThat(ok.getMessage(), is("비밀번호 수정이 완료되었습니다."));
		user = userService.findUserForIdx(1);
		Assert.assertThat(user.getPassword(), is("481f6cc0511143ccdd7e2d1b1b94faf0a700a8b49cd13922a70b5ae28acaa8c5personProject"));
	}
	
	@Test
	public void testJoin(){
		user = new User();
		user.setEmail("sungjin1@naver.com");
		user.setId("sungjin1");
		user.setName("홍길동");
		user.setPassword(password);
		user.setRegDate(date);
		user.setUpDate(date);
		OkCheck ok = userService.join(user);
		Assert.assertThat(ok.getMessage(), is("회원가입에 성공하셨습니다."));
		Assert.assertThat(ok.isBool(), is(true));
		user.setId("sungjin1");
		ok = userService.join(user);
		Assert.assertThat(ok.getMessage(), is("이미 가입되어있는 회원입니다."));
		Assert.assertThat(ok.isBool(), is(false));
		user.setEmail("sungjin@naver.com");
		ok = userService.join(user);
		Assert.assertThat(ok.getMessage(), is("이미 가입되어있는 회원입니다."));
		Assert.assertThat(ok.isBool(), is(false));
	}
	
	@Test
	public void testAutoLoginCheck(){
		User user = new User();
		Assert.assertThat(userService.autoLoginCheck(null, ""), is(false));
		Assert.assertThat(userService.autoLoginCheck(null, "abdsbas"), is(false));
		Assert.assertThat(userService.autoLoginCheck(user, ""), is(false));
		user = userService.findUserForIdx(1);
		user.setIdx(5);
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(false));
		user.setIdx(1);
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(true));
	}
	
	@Test
	public void testLeave(){
		String garbage = "b94c56f6f1cf92d48e021c573b77fa253eca91e579e308473c0536716c8e7bd6personProject";
		User user = userService.findUserForIdx(1);
		Assert.assertThat(user.getEmail(), is("sungjin@naver.com"));
		Assert.assertThat(user.getId(), is("sungjin"));
		Assert.assertThat(user.getPassword(), is(password));
		Assert.assertThat(user.getName(), is("홍길동"));
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(true));
		Assert.assertThat(userService.leave(1, "asdasdasd"), is(true));
		user = userService.findUserForIdx(1);
		Assert.assertThat(user.getEmail(), is(garbage));
		Assert.assertThat(user.getId(), is(garbage));
		Assert.assertThat(user.getPassword(), is(garbage));
		Assert.assertThat(user.getName(), is(garbage));
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(false));
	}
	
	@Test
	public void testUpdate(){
		user = userService.findUserForIdx(1);
		Assert.assertEquals(user.getRegDate(), user.getUpDate());
		Assert.assertThat(userService.update(1, "hyun", "", ""), is(false));
		Assert.assertThat(userService.update(1, "", "hyun@naver.com", ""), is(false));
		Assert.assertThat(userService.update(2, "hyun", "hyun@naver.com", ""), is(false));
		Assert.assertThat(userService.update(1, "hyun", "hyun@naver.com", ""), is(true));
		user = userService.findUserForIdx(1);
		Assert.assertThat(user.getUpDate(), is(not(user.getRegDate())));
	}
	
	@Test
	public void testTranslatePassword(){
		user = userService.findUserForIdx(1);
		userService.translatePassword("sungjin@naver.com");
		User updateUser = userService.findUserForIdx(1);
		Assert.assertThat(updateUser.getPassword(), is(not(user.getPassword())));
	}
	
	@Test
	public void testAutoLogin(){
		Assert.assertThat(userService.autoLogin(null, ""), is(false));
		Assert.assertThat(userService.autoLogin(null, "asdasdasd"), is(false));
		Assert.assertThat(userService.autoLogin(userService.findUserForIdx(2), ""), is(false));
		Assert.assertThat(userService.autoLogin(userService.findUserForIdx(2), "asdasdasd"), is(false));
		Assert.assertThat(userService.autoLogin(userService.findUserForIdx(1), ""), is(false));
		Assert.assertThat(userService.autoLogin(userService.findUserForIdx(1), "asdasdasd"), is(true));
	}
	
	@Test
	public void testAutoLogout(){
		user = userService.findUserForIdx(1);
		Assert.assertThat(userService.autoLogout(null, ""), is(false));
		Assert.assertThat(userService.autoLogout(null, "asdasdasd"), is(false));
		Assert.assertThat(userService.autoLogout(userService.findUserForIdx(2), "asdasdasd"), is(false));
		Assert.assertThat(userService.autoLogout(user, ""), is(false));
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(true));
		Assert.assertThat(userService.autoLogout(user, "asdasdasd"), is(true));
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(false));
	}
	
	@Test
	public void testPasswordCheck(){
		Assert.assertThat(userService.passwordCheck(1, "123123"), is(true));
		Assert.assertThat(userService.passwordCheck(2, "123123"), is(false));
		Assert.assertThat(userService.passwordCheck(1, "654321"), is(false));
		Assert.assertThat(userService.passwordCheck(2, "654321"), is(false));
	}
}
