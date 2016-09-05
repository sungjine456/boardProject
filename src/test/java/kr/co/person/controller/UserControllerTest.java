package kr.co.person.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
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
import kr.co.person.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class UserControllerTest {
	
	@Mock private UserService userService;
	@Autowired private Message message;
	@InjectMocks private UserController userController;
	@Autowired private WebApplicationContext wac;
	private MockMvc mock;
	private MockHttpSession mockSession;
	 
    @Before
    public void setUp() throws Exception {
    	MockitoAnnotations.initMocks(this);
    	mock = MockMvcBuilders.webAppContextSetup(wac).build();
    	mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
    	mockSession.setAttribute("loginYn", "Y");
    	mockSession.setAttribute("idx", 1);
    	mockSession.setAttribute("id", "sungjin");
    	mockSession.setAttribute("img", "defaul.png");
    	mockSession.setAttribute("name", "hong");
    	mockSession.setAttribute("email", "sungjin@naver.com");
    }
	
    @Test
    public void testJoinView() throws Exception {
    	mock.perform(get("/join"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/join"));
    }
    
    @Test
    public void testJoin() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", null, null, "bar".getBytes());
        String se = File.separator;
        
    	mock.perform(
			fileUpload("/join")
				.param("id", "test")
				.param("password", "123456")
				.contentType(MediaType.MULTIPART_FORM_DATA))
    		.andExpect(status().isOk())
    		.andExpect(model().attribute("message", message.FILE_FAIL_UPLOAD))
    		.andExpect(view().name("view/user/join"));
    	
    	mock.perform(
			fileUpload("/join")
				.file(file)
				.param("id", "test")
				.param("password", "123456")
				.contentType(MediaType.MULTIPART_FORM_DATA))
    		.andExpect(status().isOk())
    		.andExpect(model().attribute("message", message.USER_NO_EMAIL))
    		.andExpect(view().name("view/user/join"));
    	
    	mock.perform(
    		fileUpload("/join")
    			.file(file)
    			.param("id", "test")
    			.param("password", "123456")
    			.param("email", "aaa")
    			.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isOk())
			.andExpect(model().attribute("message", message.USER_NO_EMAIL_FORMAT))
			.andExpect(view().name("view/user/join"));
    	
    	mock.perform(
    		fileUpload("/join")
    			.file(file)
    			.param("id", "test")
    			.param("name", "test")
    			.param("password", "123456")
    			.param("email", "aaaads@naver.com")
    			.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isFound())
			.andExpect(flash().attribute("message", message.USER_SUCCESS_JOIN))
			.andExpect(request().sessionAttribute("loginYn", "Y"))
			.andExpect(request().sessionAttribute("id", "test"))
			.andExpect(request().sessionAttribute("name", "test"))
			.andExpect(request().sessionAttribute("email", "aaaads@naver.com"))
			.andExpect(request().sessionAttribute("img", "img"+se+"user"+se+"default.png"))
			.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testIdCheck() throws Exception {
    	mock.perform(
        	post("/join/idCheck")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("str", is(message.USER_NO_ID)))
    		.andExpect(jsonPath("bool", is("false")));
    	
    	mock.perform(
    		post("/join/idCheck")
    			.param("id", "sungjin")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
	    	.andExpect(status().isOk())
	    	.andExpect(jsonPath("str", is(message.USER_ALREADY_JOIN_ID)))
	    	.andExpect(jsonPath("bool", is("false")));
    	
    	mock.perform(
    		post("/join/idCheck")
    			.param("id", "test")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("str", is(message.USER_AVAILABLE_ID)))
    		.andExpect(jsonPath("bool", is("true")));
    }

    @Test
    public void testEmailCheck() throws Exception {
    	mock.perform(
    		post("/join/emailCheck")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("str", is(message.USER_NO_EMAIL)))
    		.andExpect(jsonPath("bool", is("false")));

    	mock.perform(
    		post("/join/emailCheck")
    			.param("email", "sungjin@naver.com")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
	    	.andExpect(status().isOk())
	    	.andExpect(jsonPath("str", is(message.USER_ALREADY_JOIN_EMAIL)))
	    	.andExpect(jsonPath("bool", is("false")));

    	mock.perform(
    		post("/join/emailCheck")
    			.param("email", "test@naver.com")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
	    	.andExpect(status().isOk())
	    	.andExpect(jsonPath("str", is(message.USER_AVAILABLE_EMAIL)))
	    	.andExpect(jsonPath("bool", is("true")));
    }
    
    @Test
    public void testLoginView() throws Exception {
    	mock.perform(
    		get("/")
    			.sessionAttr("loginYn", "Y"))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/board"));

    	mock.perform(
    		get("/")
    			.sessionAttr("loginYn", "N")
    			.cookie(new Cookie("psvd", "Lh0ZL9k8lqxLZPl4okIVpw=="))
    			.cookie(new Cookie("psvlgnd", "asdasdasd")))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/board"));
    	
    	mock.perform(
    		get("/")
    			.sessionAttr("loginYn", "N"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/login"));
    }
    
    @Test
    public void testLogin() throws Exception{
    	mock.perform(post("/"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/login"))
    		.andExpect(model().attribute("message", message.USER_WRONG_ID_OR_WRONG_PASSWORD));
    	
    	mock.perform(
    		post("/")
				.param("id", "sungjin"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/login"))
    		.andExpect(model().attribute("message", message.USER_WRONG_ID_OR_WRONG_PASSWORD));
    	
    	mock.perform(
    		post("/")
				.param("password", "123123"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/login"))
    		.andExpect(model().attribute("message", message.USER_WRONG_ID_OR_WRONG_PASSWORD));
    	
    	mock.perform(
    		post("/")
				.param("id", "test")
				.param("password", "123456"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/login"))
    		.andExpect(model().attribute("message", message.USER_WRONG_ID_OR_WRONG_PASSWORD));

    	mock.perform(
    		post("/")
    			.param("id", "test")
    			.param("password", "123456")
    			.param("idSave", "check"))
	    	.andExpect(status().isOk())
	    	.andExpect(view().name("view/user/login"))
	    	.andExpect(model().attribute("message", message.USER_WRONG_ID_OR_WRONG_PASSWORD));

    	mock.perform(
    		post("/")
    			.param("id", "sungjin")
    			.param("password", "123123"))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/board"));
    	
    	mock.perform(
    		post("/")
    			.param("id", "sungjin")
    			.param("password", "123123")
    			.param("idSave", "check"))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testLogout() throws Exception {
    	mock.perform(
    		get("/logout")
	    		.sessionAttr("loginYn", "Y")
	    		.sessionAttr("idx", 5))
	    	.andExpect(status().isOk())
			.andExpect(view().name("view/user/login"))
			.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    	
    	mock.perform(
    		get("/logout")
	    		.session(mockSession)
	    		.sessionAttr("idx", 1)
	    		.cookie(new Cookie("psvlgnd", "asdasdasdsss")))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/board"))
    		.andExpect(flash().attribute("message", message.USER_FAIL_LOGOUT));
    	
    	mock.perform(
    		get("/logout")
    			.session(mockSession)
	    		.cookie(new Cookie("psvlgnd", "asdasdasd")))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/"));
    }
    
    @Test
    public void testTranslatePassword() throws Exception{
    	mock.perform(post("/translatePassword"))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.USER_NO_EMAIL))
    		.andExpect(redirectedUrl("/"));
    	
    	mock.perform(
    		post("/translatePassword")
    			.param("email", "test"))
			.andExpect(status().isFound())
			.andExpect(flash().attribute("message", message.USER_NO_EMAIL_FORMAT))
			.andExpect(redirectedUrl("/"));

    	mock.perform(
    		post("/translatePassword")
    			.param("email", "test@naver.com"))
	    	.andExpect(status().isFound())
	    	.andExpect(flash().attribute("message", message.USER_WRONG_EMAIL))
	    	.andExpect(redirectedUrl("/"));

    	mock.perform(
    		post("/translatePassword")
    			.param("email", "sungjin@naver.com"))
	    	.andExpect(status().isFound())
	    	.andExpect(flash().attributeExists("message"))
	    	.andExpect(flash().attributeCount(1))
	    	.andExpect(redirectedUrl("/"));
    }
    
    @Test
    public void testMyPageView() throws Exception{
    	mock.perform(
    		get("/mypage")
    			.session(mockSession))
    		.andExpect(status().isOk())
    		.andExpect(model().attribute("include", "/view/user/mypage.ftl"))
    		.andExpect(view().name("view/board/frame"));

    	mock.perform(
    		get("/mypage")
    			.sessionAttr("loginYn", "Y"))
	    	.andExpect(status().isOk())
			.andExpect(view().name("view/user/login"))
			.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    }
    
    @Test
    public void testChangePassword() throws Exception {
    	mock.perform(
    		post("/changePassword")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.USER_NO_PASSWORD))
    		.andExpect(redirectedUrl("/update"));
    	
    	mock.perform(
    		post("/changePassword")
    			.sessionAttr("loginYn", "Y"))
	    	.andExpect(status().isOk())
			.andExpect(view().name("view/user/login"))
			.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    	
    	mock.perform(
    		post("/changePassword")
    			.session(mockSession)
    			.param("password", "123123"))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.USER_NO_UPDATE_PASSWORD))
    		.andExpect(redirectedUrl("/update"));
    	
    	mock.perform(
    		post("/changePassword")
    			.session(mockSession)
    			.param("changePassword", "123123"))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.USER_NO_PASSWORD))
    		.andExpect(redirectedUrl("/update"));
    	
    	mock.perform(
    		post("/changePassword")
    			.session(mockSession)
    			.param("password", "123123")
    			.param("changePassword", "123123"))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.USER_PASSWORD_SAME_UPDATE_PASSWORD))
    		.andExpect(redirectedUrl("/update"));
    	
    	mock.perform(
    		post("/changePassword")
	    		.sessionAttr("idx", 5)
				.session(mockSession)
    			.param("password", "123123")
    			.param("changePassword", "123456"))
	    	.andExpect(status().isOk())
	    	.andExpect(view().name("view/user/login"))
	    	.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    	
    	mock.perform(
    		post("/changePassword")
    			.sessionAttr("loginYn", "Y")
    			.sessionAttr("idx", 1)
    			.sessionAttr("id", "test")
    			.sessionAttr("img", "defaul.png")
    			.sessionAttr("name", "test")
    			.sessionAttr("email", "test@naver.com")
    			.param("password", "123123")
    			.param("changePassword", "123456"))
	    	.andExpect(status().isOk())
	    	.andExpect(view().name("view/user/login"))
	    	.andExpect(model().attribute("message", message.USER_NO_LOGIN));

    	mock.perform(
    		post("/changePassword")
    			.sessionAttr("idx", 1)
    			.session(mockSession)
    			.param("password", "123123")
    			.param("changePassword", "123456"))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/mypage"))
	    	.andExpect(flash().attribute("message", message.USER_SUCCESS_TRANSlATE_PASSWORD));
    }
    
    @Test
    public void testLeave() throws Exception {
    	mock.perform(
    		get("/leave")
	    		.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/mypage"))
    		.andExpect(flash().attribute("message", message.USER_NO_PASSWORD));
    	
    	mock.perform(
    		get("/leave")
    			.sessionAttr("loginYn", "Y")
    			.param("password", "123123"))
	    	.andExpect(status().isOk())
	    	.andExpect(view().name("view/user/login"))
	    	.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    	
    	mock.perform(
   			get("/leave")
    			.sessionAttr("loginYn", "Y")
    			.sessionAttr("id", "sungjin")
    			.param("password", "123123"))
	    	.andExpect(status().isOk())
	    	.andExpect(view().name("view/user/login"))
	    	.andExpect(model().attribute("message", message.USER_NO_LOGIN));

    	mock.perform(
   			get("/leave")
    			.sessionAttr("loginYn", "Y")
    			.sessionAttr("idx", 1)
    			.param("password", "123123"))
	    	.andExpect(status().isOk())
	    	.andExpect(view().name("view/user/login"))
	    	.andExpect(model().attribute("message", message.USER_NO_LOGIN));

    	mock.perform(
    		get("/leave")
    			.session(mockSession)
    			.sessionAttr("id", "test")
    			.param("password", "123123"))
	    	.andExpect(status().isOk())
	    	.andExpect(view().name("view/user/login"))
	    	.andExpect(model().attribute("message", message.USER_NO_LOGIN));

    	mock.perform(
    		get("/leave")
    			.session(mockSession)
    			.sessionAttr("id", "sungjin")
    			.param("password", "123123")
    			.cookie(new Cookie("psvlgnd", "adasdasd")))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/mypage"))
	    	.andExpect(flash().attribute("message", message.USER_FAIL_LEAVE));

    	mock.perform(
    		get("/leave")
    			.session(mockSession)
    			.sessionAttr("idx", 1)
    			.param("password", "123123"))
	    	.andExpect(status().isFound())
			.andExpect(redirectedUrl("/"))
	    	.andExpect(flash().attribute("message", message.USER_SUCCESS_LEAVE))
	    	.andExpect(request().sessionAttribute("loginYn", "N"));
    }
    
    @Test
    public void testUpdateView() throws Exception{
    	mock.perform(
    		post("/updateView")
	    		.sessionAttr("loginYn", "Y")
				.sessionAttr("id", "test")
				.sessionAttr("img", "defaul.png")
				.sessionAttr("name", "test")
				.sessionAttr("email", "test@naver.com"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/login"))
    		.andExpect(model().attribute("message", message.USER_NO_LOGIN));

    	mock.perform(
    		post("/updateView")
    			.session(mockSession))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/mypage"))
	    	.andExpect(flash().attribute("message", message.USER_NO_PASSWORD));
    	
    	mock.perform(
    		post("/updateView")
    			.session(mockSession)
    			.param("password", "111111"))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/mypage"))
	    	.andExpect(flash().attribute("message", message.USER_NO_PASSWORD));
    	
    	mock.perform(
    		post("/updateView")
	    		.session(mockSession)
	    		.param("password", "123123"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/board/frame"))
    		.andExpect(model().attribute("include", "/view/user/update.ftl"));
    }
    
    @Test
    public void testUpdate() throws Exception {
    	MockMultipartFile isNotFile = new MockMultipartFile("ufile", null, null, "bar".getBytes());
    	MockMultipartFile isFile = new MockMultipartFile("ufile", "none.png", null, "bar".getBytes());
        String se = File.separator;
        
    	mock.perform(
    		fileUpload("/update")
    			.sessionAttr("loginYn", "Y"))
	    	.andExpect(status().isOk())
			.andExpect(view().name("view/user/login"))
			.andExpect(model().attribute("message", message.USER_NO_LOGIN));
    	
    	mock.perform(
    		fileUpload("/update")
    			.session(mockSession)
    			.param("id", "test"))
	    	.andExpect(status().isOk())
			.andExpect(view().name("view/user/join"))
			.andExpect(model().attribute("message", message.FILE_FAIL_UPLOAD));
    	
    	mock.perform(
    		fileUpload("/update")
    			.file(isNotFile)
    			.session(mockSession)
    			.param("id", "test"))
	    	.andExpect(status().isOk())
			.andExpect(view().name("view/board/frame"))
			.andExpect(model().attribute("message", message.USER_NO_EMAIL));

    	mock.perform(
			fileUpload("/update")
    			.file(isNotFile)
    			.session(mockSession)
    			.param("id", "test")
    			.param("email", "test@naver.com"))
			.andExpect(status().isOk())
			.andExpect(view().name("view/board/frame"))
			.andExpect(model().attribute("message", message.USER_NO_NAME));
    	
    	mock.perform(
			fileUpload("/update")
    			.file(isNotFile)
    			.session(mockSession)
    			.param("id", "test")
    			.param("email", "test@naver.com")
    			.param("name", "test"))
			.andExpect(status().isOk())
			.andExpect(view().name("view/board/frame"))
			.andExpect(model().attribute("include", "/view/user/mypage.ftl"))
			.andExpect(model().attribute("message", message.USER_SUCCESS_UPDATE))
			.andExpect(request().sessionAttribute("name", "test"))
			.andExpect(request().sessionAttribute("email", "test@naver.com"));
    	
    	MvcResult result = mock.perform(
    		fileUpload("/update")
    			.file(isFile)
    			.session(mockSession)
    			.param("id", "test")
    			.param("email", "test@naver.com")
    			.param("name", "test"))
	    	.andExpect(status().isOk())
	    	.andExpect(view().name("view/board/frame"))
	    	.andExpect(model().attribute("include", "/view/user/mypage.ftl"))
	    	.andExpect(model().attribute("message", message.USER_SUCCESS_UPDATE))
	    	.andExpect(request().sessionAttribute("name", "test"))
			.andExpect(request().sessionAttribute("email", "test@naver.com"))
			.andReturn();
    	
    	String img = (String)result.getRequest().getSession().getAttribute("img");
    	Assert.assertThat("img"+se+"user"+se, is(img.substring(0, 9)));
    }
    
    @Test
    public void testInterceptorView() throws Exception {
    	mock.perform(get("/interceptorView"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("common/interceptorPage"));
    }
}
