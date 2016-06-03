package kr.co.person.repository;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import kr.co.person.BoardProjectApplication;
import kr.co.person.domain.Board;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class BoardRepositoryTest {
	
	@Autowired
	private BoardRepository boardRepository;
	private Board board;

	@Test
	public void findTest() {
		board = boardRepository.findOne(1);
		Assert.assertEquals("title", board.getTitle());
		Assert.assertEquals("content", board.getContent());
	}
	
	@Test
	public void saveTest() {
		Date date = new Date();
		board = new Board("t", "c", 1, date, date);
		boardRepository.save(board);
		board = boardRepository.findOne(2);
		Assert.assertEquals("t", board.getTitle());
		Assert.assertEquals("c", board.getContent());
	}
}
