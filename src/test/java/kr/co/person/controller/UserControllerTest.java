package kr.co.person.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.FileInputStream;

import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import kr.co.person.BoardProjectApplication;
import kr.co.person.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
public class UserControllerTest {
	
	@Mock private UserService userService;
	@InjectMocks private UserController userController;
	@Autowired private WebApplicationContext wac;
	private MockMvc mock;
	 
    @Before
    public void setUp() throws Exception {
    	MockitoAnnotations.initMocks(this);
    	this.mock = MockMvcBuilders.webAppContextSetup(wac).build();
    }
	
    @Test
    public void testJoinView() throws Exception {
    	mock.perform(get("/join"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/join"));
    }
    
    @Test
    public void testJoin() throws Exception {
    	FileInputStream fis = new FileInputStream("C:/boardProject/img/user/none.png");
        MockMultipartFile file = new MockMultipartFile("file", "none.png", "multipart/form-data", fis);
        
    	mock.perform(
			post("/join")
				.param("id", "test")
				.param("password", "123456")
				.requestAttr("file", file)
				.contentType(MediaType.MULTIPART_FORM_DATA))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/join"));
    }
    
    @Test
    public void testIdCheck() throws Exception {
    	mock.perform(
        	post("/join/idCheck")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("str", is("아이디를 입력해주세요.")))
    		.andExpect(jsonPath("bool", is("false")));
    	
    	mock.perform(
    		post("/join/idCheck")
    			.param("id", "sungjin")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
	    	.andExpect(status().isOk())
	    	.andExpect(jsonPath("str", is("이미 가입되어 있는 아이디입니다.")))
	    	.andExpect(jsonPath("bool", is("false")));
    	
    	mock.perform(
    		post("/join/idCheck")
    			.param("id", "test")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("str", is("가입 가능한 아이디입니다.")))
    		.andExpect(jsonPath("bool", is("true")));
    }

    @Test
    public void testEmailCheck() throws Exception {
    	mock.perform(
    		post("/join/emailCheck")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("str", is("이메일을 입력해주세요.")))
    		.andExpect(jsonPath("bool", is("false")));

    	mock.perform(
    		post("/join/emailCheck")
    			.param("email", "sungjin@naver.com")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
	    	.andExpect(status().isOk())
	    	.andExpect(jsonPath("str", is("이미 가입되어 있는 이메일입니다.")))
	    	.andExpect(jsonPath("bool", is("false")));

    	mock.perform(
    		post("/join/emailCheck")
    			.param("email", "test@naver.com")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
	    	.andExpect(status().isOk())
	    	.andExpect(jsonPath("str", is("가입 가능한 이메일입니다.")))
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
    			.sessionAttr("loginYn", "Y")
    			.cookie(new Cookie("psvd", "sungjin"))
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
    	mock.perform(
    		post("/")
				.param("id", "test")
				.param("password", "123456"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/user/login"))
    		.andExpect(model().attribute("message", "아이디 혹은 비밀번호가 틀렸습니다."));

    	mock.perform(
    		post("/")
    			.param("id", "test")
    			.param("password", "123456")
    			.param("idSave", "check"))
	    	.andExpect(status().isOk())
	    	.andExpect(view().name("view/user/login"))
	    	.andExpect(model().attribute("message", "아이디 혹은 비밀번호가 틀렸습니다."));

    	mock.perform(
    		post("/")
    			.param("id", "sungjin")
    			.param("password", "123123"))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testLogout() throws Exception {
    	mock.perform(
    		get("/logout")
	    		.sessionAttr("loginYn", "Y")
	    		.sessionAttr("idx", 5))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/board"))
    		.andExpect(flash().attribute("message", "로그아웃에 실패하셨습니다."));
    	
    	mock.perform(
    		get("/logout")
	    		.sessionAttr("loginYn", "Y")
	    		.sessionAttr("idx", 1)
	    		.cookie(new Cookie("psvlgnd", "asdasdasdsss")))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/board"))
    		.andExpect(flash().attribute("message", "로그아웃에 실패하셨습니다."));
    	
    	mock.perform(
    		get("/logout")
	    		.sessionAttr("loginYn", "Y")
	    		.sessionAttr("idx", 1)
	    		.cookie(new Cookie("psvlgnd", "asdasdasd")))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/"));
    }
}
