package kr.co.person.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.FileInputStream;

import org.joda.time.DateTime;
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
import kr.co.person.domain.User;
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
        
    	DateTime date = new DateTime();
    	mock.perform(
			post("/join")
				.requestAttr("user", new User("test", "test@naver.com", "123456", "test", "", date, date))
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
    		.andExpect(status().isOk());
    }

    @Test
    public void testEmailCheck() throws Exception {
    	mock.perform(
    		post("/join/emailCheck")
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk());
    }
}
