package kr.co.person.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import kr.co.person.BoardProjectApplication;
import kr.co.person.common.Message;
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class AdminControllerTest {
	
	@Autowired private Message message;
	@InjectMocks private AdminController AdminController;
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
    	user.setIdx(3);
    	user.setId("admin");
    	user.setName("admin");
    	user.setImg("defaul.png");
    	user.setEmail("admin@naver.com");
    	user.setAdmin("Y");
    	mockSession.setAttribute("user", user);
    }
    
    @Test
    public void testAdminViewNoAdmin() throws Exception {
    	User user = (User)mockSession.getAttribute("user");
    	user.setAdmin("N");
    	mockSession.setAttribute("user", user);
    	
    	mock.perform(
			get("/admin/users")
    			.session(mockSession))
	    	.andExpect(status().isFound())
			.andExpect(redirectedUrl("/"))
			.andExpect(flash().attribute("message", message.USER_NO_LOGIN));
    }

	@Test
	@SuppressWarnings("unchecked")
    public void testAdminViewSuccess() throws Exception {
    	MvcResult result = mock.perform(
			get("/admin/users")
    			.session(mockSession))
	    	.andExpect(status().isOk())
	    	.andExpect(model().attributeExists("users"))
	    	.andExpect(model().attribute("include", "/view/admin/adminUsers.ftl"))
	    	.andExpect(view().name("view/frame"))
	    	.andReturn();
    	
    	Page<User> pages = (Page<User>)result.getRequest().getAttribute("users");
    	List<User> userList = pages.getContent();
    	Assert.assertThat(userList.size(), is(3));
    }
	
	@Test
    public void testAdminTranslatePasswordNoEmail() throws Exception{
    	mock.perform(
    		post("/admin/translatePassword")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.USER_NO_EMAIL))
    		.andExpect(redirectedUrl("/admin/users"));
    }
    
    @Test
    public void testAdminTranslatePasswordNoEmailFormat() throws Exception{
    	mock.perform(
    		post("/admin/translatePassword")
    			.param("email", "test")
    			.session(mockSession))
			.andExpect(status().isFound())
			.andExpect(flash().attribute("message", message.USER_NO_EMAIL_FORMAT))
			.andExpect(redirectedUrl("/admin/users"));
    }
    
    @Test
    public void testAdminTranslatePasswordWrongEmail() throws Exception{
    	mock.perform(
    		post("/admin/translatePassword")
    			.param("email", "test@naver.com")
    			.session(mockSession))
	    	.andExpect(status().isFound())
	    	.andExpect(flash().attribute("message", message.USER_WRONG_EMAIL))
	    	.andExpect(redirectedUrl("/admin/users"));
    }
    
    @Test
    public void testAdminTranslatePasswordSuccess() throws Exception{
    	mock.perform(
    		post("/admin/translatePassword")
    			.param("email", "sungjin@naver.com")
    			.session(mockSession))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/admin/users"));
    }
    
    @Test
    public void testEmailAccessReNoEmail() throws Exception {
    	mock.perform(
    		post("/admin/emailAccessRe")
    			.session(mockSession))
			.andExpect(status().isFound())
			.andExpect(flash().attribute("message", message.USER_NO_EMAIL))
			.andExpect(redirectedUrl("/admin/users"));
    }
    
    @Test
    public void testEmailAccessReSuccess() throws Exception {
    	mock.perform(
    		post("/admin/emailAccessRe")
    			.param("email", "sungjine@naver.com")
    			.session(mockSession))
	    	.andExpect(status().isFound())
			.andExpect(redirectedUrl("/admin/users"));
    }
}
