package kr.co.person.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.person.BoardProjectApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class BoardLikeRepositoryTest {
	
	@Autowired private BoardLikeRepository boardLikeRepository;

	@Test
	public void testFindAll() {
		Assert.assertThat(boardLikeRepository.findAll().size(), is(1));
	}

	@Test
	public void testFindByBoardIdxAndUserIdx(){
		Assert.assertThat(boardLikeRepository.findByBoardIdxAndUserIdx(2, 5), is(nullValue()));
		Assert.assertThat(boardLikeRepository.findByBoardIdxAndUserIdx(1, 2), is(notNullValue()));
	}
	
	@Test
	public void test(){
		Assert.assertThat(boardLikeRepository.findByBoardIdx(1).size(), is(1));
	}
}
