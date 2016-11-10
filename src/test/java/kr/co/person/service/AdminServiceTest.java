package kr.co.person.service;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.person.BoardProjectApplication;
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class AdminServiceTest {
	
	@Autowired AdminService adminService;

	@Test
	public void findUserAllTest(){
		Pageable pageable = new PageRequest(0, 10, Direction.DESC, "idx");
		Page<User> pages = adminService.findUserAll(pageable);
		List<User> users = pages.getContent();
		Assert.assertThat(users.size(), is(3));
	}
}
