package kr.co.person.repository;

import static org.hamcrest.CoreMatchers.*;

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
		Assert.assertThat(board.getTitle(), is("title"));
		Assert.assertThat(board.getContent(), is("content"));
	}
	
	@Test
	public void testSave() {
		Board board = new Board("t", "c", user, date, date);
		List<Board> boardList = (List<Board>) boardRepository.findAll();
		Assert.assertThat(boardList.size(), is(1));
		Board boardSave = boardRepository.save(board);
		Assert.assertThat(boardSave.getTitle(), is("t"));
		Assert.assertThat(boardSave.getContent(), is("c"));
		List<Board> boardList2 = (List<Board>) boardRepository.findAll();
		Assert.assertThat(boardList2.size(), is(2));
	}
	
	@Test
	public void testUpdate(){
		board = boardRepository.findOne(1);
		Assert.assertThat(board.getTitle(), is("title"));
		Assert.assertThat(board.getContent(), is("content"));
		board.setContent("cccc");
		board.setTitle("tttt");
		board = boardRepository.save(board);
		Assert.assertThat(board.getTitle(), is("tttt"));
		Assert.assertThat(board.getContent(), is("cccc"));
		board = boardRepository.findOne(1);
		Assert.assertThat(board.getTitle(), is("tttt"));
		Assert.assertThat(board.getContent(), is("cccc"));
	}
}
