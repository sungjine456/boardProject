package kr.co.person.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.person.BoardProjectApplication;
import kr.co.person.common.Message;
import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;
import kr.co.person.pojo.OkUserCheck;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class UserServiceTest {

	@Autowired private UserService userService;
	@Autowired private Message message;
	private User user;
	// 비밀번호123123을 암호화한 형태
    private String password = "96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1epersonProject";

	@Test
	public void testJoinCheck(){
		OkUserCheck userCheck = userService.joinCheck("sungjin", "123123");
		Assert.assertThat(userCheck, is(notNullValue()));
		Assert.assertThat(userCheck.getMessage(), is(""));
		Assert.assertThat(userCheck.isBool(), is(true));
		Assert.assertThat(userCheck.getUser(), is(notNullValue()));
		Assert.assertThat(userCheck.getUser().getId(), is("sungjin"));
	}
	
	@Test
	public void testIdCheck() {
		Assert.assertThat(userService.idCheck(null).getMessage(), is(message.USER_NO_ID));
		Assert.assertThat(userService.idCheck("").getMessage(), is(message.USER_NO_ID));
		Assert.assertThat(userService.idCheck("sungjin").getMessage(), is(message.USER_ALREADY_JOIN_ID));
		Assert.assertThat(userService.idCheck("sungjin123").getMessage(), is(message.USER_AVAILABLE_ID));
	}

	@Test
	public void testEmailCheck() {
		Assert.assertThat(userService.emailCheck(null).getMessage(), is(message.USER_NO_EMAIL));
		Assert.assertThat(userService.emailCheck("").getMessage(), is(message.USER_NO_EMAIL));
		Assert.assertThat(userService.emailCheck("tjdwls@naver.com").getMessage(), is(message.USER_AVAILABLE_EMAIL));
		Assert.assertThat(userService.emailCheck("sungjin@naver.com").getMessage(), is(message.USER_ALREADY_JOIN_EMAIL));
		Assert.assertThat(userService.emailCheck("sungjin").getMessage(), is(message.USER_NO_EMAIL_FORMAT));
	}

	@Test
	public void testFindUserForIdx() {
		user = userService.findUserForIdx(0);
		Assert.assertThat(user, is(nullValue()));
		user = userService.findUserForIdx(1);
		Assert.assertThat(user.getName(), is("hong"));
		Assert.assertThat(user.getId(), is("sungjin"));
		Assert.assertThat(user.getEmail(), is("sungjin@naver.com"));
	}
	
	@Test
	public void testFindUserForId(){
		user = userService.findUserForId(null);
		Assert.assertThat(user, is(nullValue()));
		user = userService.findUserForId("");
		Assert.assertThat(user, is(nullValue()));
		user = userService.findUserForId("abcdabcd");
		Assert.assertThat(user, is(nullValue()));
		user = userService.findUserForId("sungjin");
		Assert.assertThat(user.getName(), is("hong"));
		Assert.assertThat(user.getId(), is("sungjin"));
		Assert.assertThat(user.getEmail(), is("sungjin@naver.com"));
	}
	
	@Test
	public void testChangePassword(){
		user = userService.findUserForIdx(1);
		Assert.assertThat(user.getPassword(), is(password));
		OkCheck ok = userService.changePassword(1, "123123", "654321");
		Assert.assertThat(ok.isBool(), is(true));
		Assert.assertThat(ok.getMessage(), is(message.USER_SUCCESS_TRANSlATE_PASSWORD));
		user = userService.findUserForIdx(1);
		Assert.assertThat(user.getPassword(), not(password));
	}
	
	@Test
	public void testJoin(){
		user = new User();
		OkCheck ok = userService.join(user);
		Assert.assertThat(ok.getMessage(), is(message.USER_FAIL_JOIN));
		user.setId("sungjin1");
		ok = userService.join(user);
		Assert.assertThat(ok.getMessage(), is(message.USER_WRONG_ID_OR_WRONG_PASSWORD));
		user.setPassword(password);
		ok = userService.join(user);
		Assert.assertThat(ok.getMessage(), is(message.USER_NO_NAME));
		user.setName("hong");
		ok = userService.join(user);
		Assert.assertThat(ok.getMessage(), is(message.USER_NO_EMAIL));
		user.setEmail("sungjin1");
		ok = userService.join(user);
		Assert.assertThat(ok.getMessage(), is(message.USER_NO_EMAIL_FORMAT));
		user.setEmail("sungjin1@naver.com");
		ok = userService.join(user);
		Assert.assertThat(ok.getMessage(), is(message.USER_SUCCESS_JOIN));
		Assert.assertThat(ok.isBool(), is(true));
		ok = userService.join(user);
		Assert.assertThat(ok.getMessage(), is(message.USER_ALREADY_JOIN_ID));
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
		Assert.assertThat(user.getName(), is("hong"));
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(true));
		Assert.assertThat(userService.leave(1, "asdasdasd"), is(true));
		user = userService.findUserForIdx(1);
		Assert.assertThat(user.getEmail(), is(garbage));
		Assert.assertThat(user.getId(), is(garbage));
		Assert.assertThat(user.getPassword(), is(garbage));
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(false));
	}
	
	@Test
	public void testUpdate(){
		user = userService.findUserForIdx(1);
		Assert.assertEquals(user.getRegDate(), user.getUpdateDate());
		Assert.assertThat(userService.update(1, "hyun", "", ""), is(false));
		Assert.assertThat(userService.update(1, "", "hyun@naver.com", ""), is(false));
		Assert.assertThat(userService.update(100, "hyun", "hyun@naver.com", ""), is(false));
		Assert.assertThat(userService.update(1, "hyun", "hyun@naver.com", ""), is(true));
		user = userService.findUserForIdx(1);
		Assert.assertThat(user.getUpdateDate(), not(user.getRegDate()));
	}
	
	@Test
	public void testTranslatePassword(){
		user = userService.findUserForIdx(1);
		userService.translatePassword("sungjin@naver.com");
		User updateUser = userService.findUserForIdx(1);
		Assert.assertThat(updateUser.getPassword(), not(user.getPassword()));
	}
	
	@Test
	public void testAutoLogin(){
		Assert.assertThat(userService.autoLogin(null, ""), is(false));
		Assert.assertThat(userService.autoLogin(null, "asdasdasd"), is(false));
		Assert.assertThat(userService.autoLogin(userService.findUserForIdx(2), ""), is(false));
		Assert.assertThat(userService.autoLogin(userService.findUserForIdx(100), "asdasdasd"), is(false));
		Assert.assertThat(userService.autoLogin(userService.findUserForIdx(1), ""), is(false));
		Assert.assertThat(userService.autoLogin(userService.findUserForIdx(1), "asdasdasd"), is(true));
	}
	
	@Test
	public void testAutoLogout(){
		user = userService.findUserForIdx(1);
		Assert.assertThat(userService.autoLogout(null, ""), is(false));
		Assert.assertThat(userService.autoLogout(null, "asdasdasd"), is(false));
		Assert.assertThat(userService.autoLogout(userService.findUserForIdx(3), "asdasdasd"), is(false));
		Assert.assertThat(userService.autoLogout(user, ""), is(false));
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(true));
		Assert.assertThat(userService.autoLogout(user, "asdasdasd"), is(true));
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(false));
	}
	
	@Test
	public void testPasswordCheck(){
		Assert.assertThat(userService.passwordCheck(1, "123123"), is(true));
		Assert.assertThat(userService.passwordCheck(100, "123123"), is(false));
		Assert.assertThat(userService.passwordCheck(1, "654321"), is(false));
		Assert.assertThat(userService.passwordCheck(100, "654321"), is(false));
	}
	
	@Test
	public void testAccessEmail(){
		user = userService.findUserForId("sungjine");
		Assert.assertThat(user.getAccess(), is("N"));
		userService.accessEmail("sungjine@naver.com");
		user = userService.findUserForId("sungjine");
		Assert.assertThat(user.getAccess(), is("Y"));
	}
}
