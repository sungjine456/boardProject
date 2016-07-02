package kr.co.person.repository;

import java.util.Date;
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
	private User user = new User("tes", "test@naver.com", "test", "test", date, date);

	@Test
	public void testFind() {
		board = boardRepository.findOne(1);
		Assert.assertEquals("title", board.getTitle());
		Assert.assertEquals("content", board.getContent());
	}
	
	@Test
	public void testSave() {
		Board board = new Board("t", "c", user, date, date);
		List<Board> boardList = (List<Board>) boardRepository.findAll();
		Assert.assertEquals(1, boardList.size());
		Board boardSave = boardRepository.save(board);
		Assert.assertEquals("t", boardSave.getTitle());
		Assert.assertEquals("c", boardSave.getContent());
		List<Board> boardList2 = (List<Board>) boardRepository.findAll();
		Assert.assertEquals(2, boardList2.size());
	}
	
	@Test
	public void testUpdate(){
		board = boardRepository.findOne(1);
		Assert.assertEquals("title", board.getTitle());
		Assert.assertEquals("content", board.getContent());
		board.setContent("cccc");
		board.setTitle("tttt");
		board = boardRepository.save(board);
		Assert.assertEquals("tttt", board.getTitle());
		Assert.assertEquals("cccc", board.getContent());
	}
}
