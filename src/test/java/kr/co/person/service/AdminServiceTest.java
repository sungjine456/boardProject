package kr.co.person.service;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import kr.co.person.BoardProjectApplication;
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class AdminServiceTest {
	
	@Autowired AdminService adminService;

	@Test
	public void findUserAllTest(){
		List<User> users = adminService.findUserAll();
		Assert.assertThat(users.size(), is(3));
	}
}
