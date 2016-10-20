package kr.co.person.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import kr.co.person.BoardProjectApplication;
import kr.co.person.common.Message;
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
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
			get("/adminView")
    			.session(mockSession))
	    	.andExpect(status().isFound())
			.andExpect(redirectedUrl("/"))
			.andExpect(flash().attribute("message", message.USER_NO_LOGIN));
    }

    @Test
    public void testAdminView() throws Exception {
    	mock.perform(
			get("/adminView")
    			.session(mockSession))
	    	.andExpect(status().isOk())
	    	.andExpect(view().name("view/admin/adminView"));
    }
}
