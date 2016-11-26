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
import kr.co.person.domain.AutoLogin;
import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;
import kr.co.person.pojo.OkObjectCheck;
import kr.co.person.repository.AutoLoginRepository;
import kr.co.person.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class UserServiceTest {

	@Autowired private UserService userService;
	@Autowired private UserRepository userRepository;
	@Autowired private AutoLoginRepository autoLoginRepository;
	@Autowired private Message message;
	private User user;
	// 비밀번호123123을 암호화한 형태
    private String password = "96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1epersonProject";

	@Test
	public void testConfirmUserPassword(){
		OkObjectCheck<User> userCheck = userService.confirmUserPassword("sungjin", password);
		Assert.assertThat(userCheck, is(notNullValue()));
		Assert.assertThat(userCheck.getMessage(), is(""));
		Assert.assertThat(userCheck.isBool(), is(true));
		Assert.assertThat(userCheck.getObject(), is(notNullValue()));
		Assert.assertThat(userCheck.getObject().getId(), is("sungjin"));
	}
	
	@Test
	public void testIdCheck() {
		Assert.assertThat(userService.idCheck("sungjin").getMessage(), is(message.USER_ALREADY_JOIN_ID));
		Assert.assertThat(userService.idCheck("sungjin123").getMessage(), is(message.USER_AVAILABLE_ID));
	}

	@Test
	public void testEmailCheck() {
		Assert.assertThat(userService.emailCheck("tjdwls@naver.com").getMessage(), is(message.USER_AVAILABLE_EMAIL));
		Assert.assertThat(userService.emailCheck("sungjin@naver.com").getMessage(), is(message.USER_ALREADY_JOIN_EMAIL));
		Assert.assertThat(userService.emailCheck("sungjin").getMessage(), is(message.USER_NO_EMAIL_FORMAT));
	}

	@Test
	public void testFindUserForId(){
		OkObjectCheck<User> ouc = userService.findUserForId("abcdabcd");
		Assert.assertThat(ouc.isBool(), is(false));
		ouc = userService.findUserForId("sungjin");
		Assert.assertThat(ouc.isBool(), is(true));
		Assert.assertThat(ouc.getObject().getName(), is("hong"));
		Assert.assertThat(ouc.getObject().getId(), is("sungjin"));
		Assert.assertThat(ouc.getObject().getEmail(), is("sungjin@naver.com"));
	}
	
	@Test
	public void testFindUserForEmail(){
		OkObjectCheck<User> ouc = userService.findUserForEmail("su@naver.com");
		Assert.assertThat(ouc.isBool(), is(false));
		Assert.assertThat(ouc.getMessage(), is(message.USER_WRONG_EMAIL));
		ouc = userService.findUserForEmail("sungjin@naver.com");
		Assert.assertThat(ouc.isBool(), is(true));
		Assert.assertThat(ouc.getObject().getName(), is("hong"));
		Assert.assertThat(ouc.getObject().getId(), is("sungjin"));
		Assert.assertThat(ouc.getObject().getEmail(), is("sungjin@naver.com"));
	}
	
	@Test
	public void testChangePassword(){
		user = userRepository.findOne(1);
		Assert.assertThat(user.getPassword(), is(password));
		OkCheck ok = userService.changePassword(1, password, "654321");
		Assert.assertThat(ok.isBool(), is(true));
		Assert.assertThat(ok.getMessage(), is(message.USER_SUCCESS_TRANSlATE_PASSWORD));
		user = userRepository.findOne(1);
		Assert.assertThat(user.getPassword(), not(password));
	}
	
	@Test
	public void testJoin(){
		user = userRepository.findById("sungjin1");
		Assert.assertThat(user, nullValue());
		user = new User();
		user.setId("sungjin1");
		user.setPassword(password);
		user.setName("hong");
		user.setEmail("sungjin1@naver.com");
		OkCheck ok = userService.join(user);
		Assert.assertThat(ok.getMessage(), is(message.USER_SUCCESS_JOIN));
		Assert.assertThat(ok.isBool(), is(true));
		user = userRepository.findById("sungjin1");
		Assert.assertThat(user, notNullValue());
	}
	
	@Test
	public void testAutoLoginCheck(){
		user = userRepository.findOne(1);
		user.setIdx(5);
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(false));
		user.setIdx(1);
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(true));
	}
	
	@Test
	public void testLeave(){
		String garbage = "b94c56f6f1cf92d48e021c573b77fa253eca91e579e308473c0536716c8e7bd6personProject";
		User user = userRepository.findOne(1);
		Assert.assertThat(user.getEmail(), is("sungjin@naver.com"));
		Assert.assertThat(user.getId(), is("sungjin"));
		Assert.assertThat(user.getPassword(), is(password));
		Assert.assertThat(user.getName(), is("hong"));
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(true));
		Assert.assertThat(userService.leave(1, "asdasdasd"), is(true));
		user = userRepository.findOne(1);
		Assert.assertThat(user.getEmail(), is(garbage));
		Assert.assertThat(user.getId(), is(garbage));
		Assert.assertThat(user.getPassword(), is(garbage));
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(false));
	}
	
	@Test
	public void testUpdate(){
		String newName = "hongs";
		String newEmail = "hong@naver.com";
		String newImg = "img/hong";
		user = userRepository.findOne(1);
		Assert.assertThat(user.getRegDate(), is(user.getUpdateDate()));
		Assert.assertThat(user.getName(), not(newName));
		Assert.assertThat(user.getEmail(), not(newEmail));
		Assert.assertThat(user.getImg(), not(newImg));
		user.setName(newName);
		user.setEmail(newEmail);
		user.setImg(newImg);
		OkObjectCheck<User> ok = userService.update(user);
		Assert.assertThat(ok.isBool(), is(true));
		Assert.assertThat(ok.getMessage(), is(message.USER_SUCCESS_UPDATE));
		user = ok.getObject();
		Assert.assertThat(user.getUpdateDate(), not(user.getRegDate()));
		Assert.assertThat(user.getName(), is(newName));
		Assert.assertThat(user.getEmail(), is(newEmail));
		Assert.assertThat(user.getImg(), is(newImg));
		user = userRepository.findOne(1);
		Assert.assertThat(user.getUpdateDate(), not(user.getRegDate()));
		Assert.assertThat(user.getName(), is(newName));
		Assert.assertThat(user.getEmail(), is(newEmail));
		Assert.assertThat(user.getImg(), is(newImg));
	}
	
	@Test
	public void testTranslatePassword(){
		user = userRepository.findOne(1);
		userService.translatePassword("sungjin@naver.com");
		User updateUser = userRepository.findOne(1);
		Assert.assertThat(updateUser.getPassword(), not(user.getPassword()));
	}
	
	@Test
	public void testAutoLogin(){
		String loginId = "asdasdasdasdfasdf";
		AutoLogin autoLogin = autoLoginRepository.findOne(3);
		Assert.assertThat(autoLogin, nullValue());
		Assert.assertThat(userService.autoLogin(userRepository.findOne(1), loginId), is(true));
		autoLogin = autoLoginRepository.findOne(3);
		Assert.assertThat(autoLogin, notNullValue());
		Assert.assertThat(autoLogin.getLoginId(), is(loginId));
	}
	
	@Test
	public void testAutoLogout(){
		user = userRepository.findOne(1);
		Assert.assertThat(userService.autoLogout(userRepository.findOne(3), "asdasdasd"), is(false));
		Assert.assertThat(userService.autoLogout(user, ""), is(false));
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(true));
		Assert.assertThat(userService.autoLogout(user, "asdasdasd"), is(true));
		Assert.assertThat(userService.autoLoginCheck(user, "asdasdasd"), is(false));
	}
	
	@Test
	public void testPasswordCheck(){
		Assert.assertThat(userService.passwordCheck(1, password), is(true));
		Assert.assertThat(userService.passwordCheck(100, "123123"), is(false));
		Assert.assertThat(userService.passwordCheck(1, "654321"), is(false));
		Assert.assertThat(userService.passwordCheck(100, "654321"), is(false));
	}
	
	@Test
	public void testAccessEmail(){
		User user = userRepository.findByEmail("sungjine@naver.com");
		Assert.assertThat(user.getAccess(), is("N"));
		userService.accessEmail("sungjine@naver.com");
		user = userRepository.findByEmail("sungjine@naver.com");
		Assert.assertThat(user.getAccess(), is("Y"));
	}
}
