package kr.co.person.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.UUID;

import javax.servlet.http.Cookie;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import kr.co.person.BoardProjectApplication;
import kr.co.person.common.Message;
import kr.co.person.domain.User;
import kr.co.person.repository.UserRepository;
import kr.co.person.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class UserControllerTest {
	
	@Mock private UserService userService;
	@Autowired private Message message;
	@InjectMocks private UserController userController;
	@Autowired private UserRepository userRepository;
	@Autowired private WebApplicationContext wac;
	private MockMvc mock;
	private MockHttpSession mockSession;
	 
    @Before
    public void setUp() throws Exception {
    	MockitoAnnotations.initMocks(this);
    	mock = MockMvcBuilders.webAppContextSetup(wac).build();
    	mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
    	mockSession.setAttribute("loginYn", "Y");
    	User user = new User();
    	user.setIdx(1);
    	user.setId("sungjin");
    	user.setName("hong");
    	user.setImg("default.png");
    	user.setEmail("sungjin@naver.com");
    	user.setAccess("Y");
    	mockSession.setAttribute("user", user);
    }
	
    @Test
    public void testJoinView() throws Exception {
    	mock.perform(get("/join"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/join"));
    }
    
    @Test
    public void testJoinNoEmail() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", null, null, "bar".getBytes());
    	
    	mock.perform(
			fileUpload("/join")
				.file(file)
				.param("id", "test")
				.param("password", "123456"))
    		.andExpect(status().isOk())
    		.andExpect(model().attribute("message", message.USER_NO_EMAIL))
    		.andExpect(view().name("view/user/join"));
    }
    
    @Test
    public void testJoinNoEmailFormet() throws Exception {
    	MockMultipartFile file = new MockMultipartFile("file", null, null, "bar".getBytes());
    	
    	mock.perform(
    		fileUpload("/join")
    			.file(file)
    			.param("id", "test")
    			.param("password", "123456")
    			.param("email", "aaa"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("message", message.USER_NO_EMAIL_FORMAT))
			.andExpect(view().name("view/user/join"));
    }
    
    @Test
    public void testJoinSuccess() throws Exception {
    	MockMultipartFile file = new MockMultipartFile("file", null, null, "bar".getBytes());
    	
    	mock.perform(
    		fileUpload("/join")
    			.file(file)
    			.param("id", "test")
    			.param("name", "test")
    			.param("password", "123456")
    			.param("email", "tjdwlsdms100@naver.com"))
			.andExpect(status().isFound())
			.andExpect(flash().attribute("email", "tjdwlsdms100@naver.com"))
			.andExpect(redirectedUrl("/emailAccessAgo"));
    }
    
    @Test
    public void testIdCheckNoId() throws Exception {
    	mock.perform(
        	post("/join/idCheck")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("str", is(message.USER_NO_ID)))
    		.andExpect(jsonPath("bool", is("false")));
    }
    
    @Test
    public void testIdCheckAlreadyId() throws Exception {
    	mock.perform(
    		post("/join/idCheck")
    			.param("id", "sungjin")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
	    	.andExpect(status().isOk())
	    	.andExpect(jsonPath("str", is(message.USER_ALREADY_JOIN_ID)))
	    	.andExpect(jsonPath("bool", is("false")));
    }
    
    @Test
    public void testIdCheckSuccess() throws Exception {
    	mock.perform(
    		post("/join/idCheck")
    			.param("id", "test")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("str", is(message.USER_AVAILABLE_ID)))
    		.andExpect(jsonPath("bool", is("true")));
    }

    @Test
    public void testEmailCheckNoEmail() throws Exception {
    	mock.perform(
    		post("/join/emailCheck")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("str", is(message.USER_NO_EMAIL)))
    		.andExpect(jsonPath("bool", is("false")));
    }
    
    @Test
    public void testEmailCheckAleadyEmail() throws Exception {
    	mock.perform(
    		post("/join/emailCheck")
    			.param("email", "sungjin@naver.com")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
	    	.andExpect(status().isOk())
	    	.andExpect(jsonPath("str", is(message.USER_ALREADY_JOIN_EMAIL)))
	    	.andExpect(jsonPath("bool", is("false")));
    }
    
    @Test
    public void testEmailCheckSuccess() throws Exception {
    	mock.perform(
    		post("/join/emailCheck")
    			.param("email", "test@naver.com")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
	    	.andExpect(status().isOk())
	    	.andExpect(jsonPath("str", is(message.USER_AVAILABLE_EMAIL)))
	    	.andExpect(jsonPath("bool", is("true")));
    }
    
    @Test
    public void testLoginViewLoginYn() throws Exception {
    	mock.perform(
    		get("/")
    			.sessionAttr("loginYn", "Y"))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testLoginViewAutoLogin() throws Exception {
    	mock.perform(
    		get("/")
    			.sessionAttr("loginYn", "N")
    			.cookie(new Cookie("psvd", "Lh0ZL9k8lqxLZPl4okIVpw=="))
    			.cookie(new Cookie("psvlgnd", "asdasdasd")))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testLoginView() throws Exception {
    	mock.perform(
    		get("/")
    			.sessionAttr("loginYn", "N"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/login"));
    }
    
    @Test
    public void testLoginNoIdAndNoPassword() throws Exception{
    	mock.perform(post("/"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/login"))
    		.andExpect(model().attribute("message", message.USER_WRONG_ID_OR_WRONG_PASSWORD));
    }
    
    @Test
    public void testLoginNoPassword() throws Exception{
    	mock.perform(
    		post("/")
				.param("id", "sungjin"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/login"))
    		.andExpect(model().attribute("message", message.USER_WRONG_ID_OR_WRONG_PASSWORD));
    }
    
    @Test
    public void testLoginNoId() throws Exception{
    	mock.perform(
    		post("/")
				.param("password", "123123"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/login"))
    		.andExpect(model().attribute("message", message.USER_WRONG_ID_OR_WRONG_PASSWORD));
    }
    
    @Test
    public void testLoginWrongIdAndWrongPassword() throws Exception{
    	mock.perform(
    		post("/")
				.param("id", "test")
				.param("password", "123456"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/login"))
    		.andExpect(model().attribute("message", message.USER_WRONG_ID_OR_WRONG_PASSWORD));
    }
    
    @Test
    public void testLoginSuccess() throws Exception{
    	mock.perform(
    		post("/")
    			.param("id", "sungjin")
    			.param("password", "123123"))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testLoginAutoLoginSuccess() throws Exception{
    	mock.perform(
    		post("/")
    			.param("id", "sungjin")
    			.param("password", "123123")
    			.param("idSave", "check"))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testLogoutWrongSessionIdx() throws Exception {
    	mock.perform(
    		get("/logout")
	    		.sessionAttr("loginYn", "Y")
	    		.sessionAttr("idx", 5))
	    	.andExpect(status().isOk())
			.andExpect(view().name("view/user/login"))
			.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    }
    
    @Test
    public void testLogoutWrongCookie() throws Exception {
    	mock.perform(
    		get("/logout")
	    		.session(mockSession)
	    		.sessionAttr("idx", 1)
	    		.cookie(new Cookie("psvlgnd", "asdasdasdsss")))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/board"))
    		.andExpect(flash().attribute("message", message.USER_FAIL_LOGOUT));
    }
    
    @Test
    public void testLogoutSuccess() throws Exception {
    	mock.perform(
    		get("/logout")
    			.session(mockSession)
	    		.cookie(new Cookie("psvlgnd", "asdasdasd")))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/"));
    }
    
    @Test
    public void testTranslatePasswordNoEmail() throws Exception{
    	mock.perform(post("/translatePassword"))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.USER_NO_EMAIL))
    		.andExpect(redirectedUrl("/"));
    }
    
    @Test
    public void testTranslatePasswordNoEmailFormat() throws Exception{
    	mock.perform(
    		post("/translatePassword")
    			.param("email", "test"))
			.andExpect(status().isFound())
			.andExpect(flash().attribute("message", message.USER_NO_EMAIL_FORMAT))
			.andExpect(redirectedUrl("/"));
    }
    
    @Test
    public void testTranslatePasswordWrongEmail() throws Exception{
    	mock.perform(
    		post("/translatePassword")
    			.param("email", "test@naver.com"))
	    	.andExpect(status().isFound())
	    	.andExpect(flash().attribute("message", message.USER_WRONG_EMAIL))
	    	.andExpect(redirectedUrl("/"));
    }
    
    @Test
    public void testTranslatePasswordSuccess() throws Exception{
    	mock.perform(
    		post("/translatePassword")
    			.param("email", "sungjin@naver.com"))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/"));
    }
    
    @Test
    public void testMyPageViewSuccess() throws Exception{
    	mock.perform(
    		get("/mypage")
    			.session(mockSession))
    		.andExpect(status().isOk())
    		.andExpect(model().attribute("include", "/view/user/mypage.ftl"))
    		.andExpect(view().name("view/board/frame"));
    }
    
    @Test
    public void testMyPageViewNoSession() throws Exception{
    	mock.perform(
    		get("/mypage")
    			.sessionAttr("loginYn", "Y"))
	    	.andExpect(status().isOk())
			.andExpect(view().name("view/user/login"))
			.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    }
    
    @Test
    public void testChangePasswordNoPasswordAndNoUpdatePassword() throws Exception {
    	mock.perform(
    		post("/changePassword")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.USER_NO_PASSWORD))
    		.andExpect(redirectedUrl("/mypage"));
    }
    
    @Test
    public void testChangePasswordNoSession() throws Exception {
    	mock.perform(
    		post("/changePassword")
    			.sessionAttr("loginYn", "Y"))
	    	.andExpect(status().isOk())
			.andExpect(view().name("view/user/login"))
			.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    }
    
    @Test
    public void testChangePasswordNoUpdatePassword() throws Exception {
    	mock.perform(
    		post("/changePassword")
    			.session(mockSession)
    			.param("password", "123123"))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.USER_NO_UPDATE_PASSWORD))
    		.andExpect(redirectedUrl("/mypage"));
    }
    
    @Test
    public void testChangePasswordNoPassword() throws Exception {
    	mock.perform(
    		post("/changePassword")
    			.session(mockSession)
    			.param("changePassword", "123123"))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.USER_NO_PASSWORD))
    		.andExpect(redirectedUrl("/mypage"));
    }
    
    @Test
    public void testChangePasswordPasswordSameUpdatePassword() throws Exception {
    	mock.perform(
    		post("/changePassword")
    			.session(mockSession)
    			.param("password", "123123")
    			.param("changePassword", "123123"))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.USER_PASSWORD_SAME_UPDATE_PASSWORD))
    		.andExpect(redirectedUrl("/mypage"));
    }
    
    @Test
    public void testChangePasswordWrongSessionIdx() throws Exception {
    	User user = (User)mockSession.getAttribute("user");
    	user.setIdx(5);
    	mockSession.setAttribute("user", user);
    	
    	mock.perform(
    		post("/changePassword")
				.session(mockSession)
    			.param("password", "123123")
    			.param("changePassword", "123456"))
	    	.andExpect(status().isOk())
	    	.andExpect(view().name("view/user/login"))
	    	.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    }
    
    @Test
    public void testChangePasswordWrongSessionId() throws Exception {
    	User user = (User)mockSession.getAttribute("user");
    	user.setIdx(1);
    	user.setId("test");
    	mockSession.setAttribute("user", user);
    	
    	mock.perform(
    		post("/changePassword")
    			.session(mockSession)
    			.param("password", "123123")
    			.param("changePassword", "123456"))
	    	.andExpect(status().isOk())
	    	.andExpect(view().name("view/user/login"))
	    	.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    }
    
    @Test
    public void testChangePasswordSuccess() throws Exception {
    	User user = (User)mockSession.getAttribute("user");
    	user.setId("sungjin");
    	mockSession.setAttribute("user", user);
    	
    	mock.perform(
    		post("/changePassword")
    			.session(mockSession)
    			.param("password", "123123")
    			.param("changePassword", "123456"))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/mypage"))
	    	.andExpect(flash().attribute("message", message.USER_SUCCESS_TRANSlATE_PASSWORD));
    }
    
    @Test
    public void testLeaveNoPassword() throws Exception {
    	mock.perform(
    		get("/leave")
	    		.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/mypage"))
    		.andExpect(flash().attribute("message", message.USER_NO_PASSWORD));
    }
    
    @Test
    public void testLeaveNoSession() throws Exception {
    	mock.perform(
    		get("/leave")
    			.sessionAttr("loginYn", "Y")
    			.param("password", "123123"))
	    	.andExpect(status().isOk())
	    	.andExpect(view().name("view/user/login"))
	    	.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    }
    
    @Test
    public void testLeaveWrongCookie() throws Exception {
    	mock.perform(
    		get("/leave")
    			.session(mockSession)
    			.sessionAttr("id", "sungjin")
    			.param("password", "123123")
    			.cookie(new Cookie("psvlgnd", "adasdasd")))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/mypage"))
	    	.andExpect(flash().attribute("message", message.USER_FAIL_LEAVE));
    }
    
    @Test
    public void testLeaveSuccess() throws Exception {
    	mock.perform(
    		get("/leave")
    			.session(mockSession)
    			.sessionAttr("idx", 1)
    			.param("password", "123123"))
	    	.andExpect(status().isFound())
			.andExpect(redirectedUrl("/"))
	    	.andExpect(flash().attribute("message", message.USER_SUCCESS_LEAVE))
	    	.andExpect(request().sessionAttribute("loginYn", "N"));
    	
    	User user = userRepository.findOne(1);
    	String garbage = "b94c56f6f1cf92d48e021c573b77fa253eca91e579e308473c0536716c8e7bd6personProject";
    	Assert.assertThat(user.getEmail(), is(garbage));
		Assert.assertThat(user.getId(), is(garbage));
		Assert.assertThat(user.getPassword(), is(garbage));
		Assert.assertThat(user.getName(), is(garbage));
    }
    
    @Test
    public void testUpdateView() throws Exception{
    	mock.perform(
    		post("/updateView")
	    		.sessionAttr("loginYn", "Y")
				.sessionAttr("user", new User()))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/login"))
    		.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    }
    
    @Test
    public void testUpdateViewNoPassword() throws Exception{
    	mock.perform(
    		post("/updateView")
    			.session(mockSession))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/mypage"))
	    	.andExpect(flash().attribute("message", message.USER_NO_PASSWORD));
    }
    
    @Test
    public void testUpdateViewWrongPassword() throws Exception{
    	mock.perform(
    		post("/updateView")
    			.session(mockSession)
    			.param("password", "111111"))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/mypage"))
	    	.andExpect(flash().attribute("message", message.USER_NO_PASSWORD));
    }
    
    @Test
    public void testUpdateViewSuccess() throws Exception{
    	mock.perform(
    		post("/updateView")
	    		.session(mockSession)
	    		.param("password", "123123"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/board/frame"))
    		.andExpect(model().attribute("include", "/view/user/update.ftl"));
    }
    
    @Test
    public void testUpdateNoSession() throws Exception {
    	mock.perform(
    		fileUpload("/update")
    			.file(new MockMultipartFile("ufile", "b".getBytes()))
    			.sessionAttr("loginYn", "Y"))
	    	.andExpect(status().isOk())
			.andExpect(view().name("view/user/login"))
			.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    }
    
    @Test
    public void testUpdateNoEmail() throws Exception {
    	mock.perform(
			fileUpload("/update")
				.file(new MockMultipartFile("ufile", "b".getBytes()))
    			.session(mockSession)
    			.param("id", "test"))
	    	.andExpect(status().isFound())
			.andExpect(redirectedUrl("/mypage"))
			.andExpect(flash().attribute("message", message.USER_NO_EMAIL));
    }
    
    @Test
    public void testUpdateNoName() throws Exception {
    	mock.perform(
			fileUpload("/update")
				.file(new MockMultipartFile("ufile", "b".getBytes()))
    			.session(mockSession)
    			.param("id", "test")
    			.param("email", "test@naver.com"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/mypage"))
			.andExpect(flash().attribute("message", message.USER_NO_NAME));
    }
    
    @Test
    public void testUpdateSuccessParam3() throws Exception{
    	MvcResult result = mock.perform(
    		fileUpload("/update")
    			.file(new MockMultipartFile("ufile", "b".getBytes()))
    			.session(mockSession)
    			.param("id", "test")
    			.param("email", "test@naver.com")
    			.param("name", "test"))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/mypage"))
	    	.andExpect(flash().attribute("message", message.USER_SUCCESS_UPDATE))
			.andReturn();
        	
    	User user = (User)result.getRequest().getSession().getAttribute("user");
    	int idx = user.getIdx();
    	String id = user.getId();
    	String name = user.getName();
    	String email = user.getEmail();
    	String img = user.getImg();
    	Assert.assertThat(idx, is(1));
    	Assert.assertThat(id, is("sungjin"));
    	Assert.assertThat(name, is("test"));
    	Assert.assertThat(email, is("test@naver.com"));
    	Assert.assertThat("default.png", is(img.substring(9)));
    }
    
    @Test
    public void testUpdateSuccessParam4() throws Exception{
    	MockMultipartFile isFile = new MockMultipartFile("ufile", "none.png", null, "bar".getBytes());
        
    	MvcResult result = mock.perform(
    		fileUpload("/update")
    			.file(isFile)
    			.session(mockSession)
    			.param("id", "test")
    			.param("email", "test@naver.com")
    			.param("name", "test"))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/mypage"))
	    	.andExpect(flash().attribute("message", message.USER_SUCCESS_UPDATE))
			.andReturn();
    	
    	User user = (User)result.getRequest().getSession().getAttribute("user");
    	int idx = user.getIdx();
    	String id = user.getId();
    	String name = user.getName();
    	String email = user.getEmail();
    	String img = user.getImg();
    	Assert.assertThat(idx, is(1));
    	Assert.assertThat(id, is("sungjin"));
    	Assert.assertThat(name, is("test"));
    	Assert.assertThat(email, is("test@naver.com"));
    	Assert.assertThat("test", is(img.substring(9, 13)));
    }
    
    @Test
    public void testEmailAccessNoEmail() throws Exception {
    	mock.perform(get("/emailAccess"))
	    	.andExpect(status().isOk())
			.andExpect(view().name("view/user/login"))
			.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    }
    
    @Test
    public void testEmailAccessSuccess() throws Exception {
    	mock.perform(
    		get("/emailAccess")
    			.param("access", "sungjine@naver.com"))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.MAIL_THANK_YOU_FOR_AGREE))
			.andExpect(request().sessionAttribute("loginYn", "Y"))
			.andExpect(request().sessionAttribute("user", is(notNullValue())))
    		.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testEmailAccessAgoSuccess() throws Exception {
    	mock.perform(get("/emailAccessAgo"))
	    	.andExpect(status().isOk())
			.andExpect(view().name("view/user/emailAccessAgo"));
    }
    
    @Test
    public void testEmailAccessReNoEmail() throws Exception {
    	mock.perform(post("/emailAccessRe"))
			.andExpect(status().isOk())
			.andExpect(view().name("view/user/login"))
			.andExpect(model().attribute("message", message.USER_NO_EMAIL));
    }
    
    @Test
    public void testEmailAccessReSuccess() throws Exception {
    	mock.perform(
    		post("/emailAccessRe")
    			.param("email", "sungjine@naver.com"))
	    	.andExpect(status().isFound())
			.andExpect(redirectedUrl("/emailAccessAgo"));
    }
    
    @Test
    public void testInterceptorView() throws Exception {
    	mock.perform(get("/interceptorView"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("common/interceptorPage"));
    }
}
