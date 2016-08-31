package kr.co.person.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
	
	@InjectMocks private UserController userController;
	@Autowired private WebApplicationContext wac;
	private MockMvc mock;
	 
    @Before
    public void setUp() throws Exception {
    	this.mock = MockMvcBuilders.webAppContextSetup(wac).build();
    }
	
    @Test
    public void testJoinView() throws Exception {
    	mock.perform(get("/join"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("joinView"));
    }
    
    @Test
    public void testIdCheck() throws Exception {
    	mock.perform(
    			get("/join/idCheck")
    				.param("id", "test"))
    	.andExpect(status().isOk());
    }
}
