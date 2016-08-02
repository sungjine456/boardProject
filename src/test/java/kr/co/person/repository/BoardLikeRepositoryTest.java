package kr.co.person.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import kr.co.person.BoardProjectApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class BoardLikeRepositoryTest {
	
	@Autowired private BoardLikeRepository boardLikeRepository;

	@Test
	public void testFindAll() {
		Assert.assertThat(boardLikeRepository.findAll().size(), is(1));
	}

	@Test
	public void testCount() {
		Assert.assertThat(boardLikeRepository.count(), is(1L));
	}
	
	@Test
	public void testFindByBoardIdxAndUserIdx(){
		Assert.assertThat(boardLikeRepository.findByBoardIdxAndUserIdx(2, 5), is(nullValue()));
		Assert.assertThat(boardLikeRepository.findByBoardIdxAndUserIdx(1, 2), is(notNullValue()));
	}
}
