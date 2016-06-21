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
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class BoardRepositoryTest {
	
	@Autowired
	private BoardRepository boardRepository;
	private Board board;
	Date date = new Date();
	private User user = new User("sungjin", "sungjin@naver.com", "123456", "홍길동", date, date);

	@Test
	public void testFind() {
		board = boardRepository.findOne(1);
		Assert.assertEquals("title", board.getTitle());
		Assert.assertEquals("content", board.getContent());
	}
	
	@Test
	public void testSave() {
		Board board = new Board("t", "c", user, date, date);
		Assert.assertEquals("t", board.getTitle());
		Board boardSave = boardRepository.save(board);
		Assert.assertEquals("t", boardSave.getTitle());
		Assert.assertEquals("c", boardSave.getContent());
	}
}
