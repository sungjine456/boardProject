package kr.co.person.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import kr.co.person.BoardProjectApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
public class ErrorPageTest {
	
	@Autowired private WebApplicationContext wac;
	private MockMvc mock;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
    	mock = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void errorPage_404() throws Exception{
		mock.perform(get("/error404"))
			.andExpect(view().name("view/error/404error"));
	}
}
