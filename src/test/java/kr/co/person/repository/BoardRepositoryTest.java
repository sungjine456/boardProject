package kr.co.person.repository;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.person.domain.Board;
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class BoardRepositoryTest {
	
	@Autowired private BoardRepository boardRepository;
	DateTime date = new DateTime();

	@Test
	public void testFind() {
		Board board = boardRepository.findOne(1);
		Assert.assertThat(board.getTitle(), is("title"));
		Assert.assertThat(board.getContent(), is("content"));
	}
	
	@Test
	public void testSave() {
		List<Board> boardList = (List<Board>) boardRepository.findAll();
		Assert.assertThat(boardList.size(), is(1));
		
		User user = new User("tes", "test@naver.com", "test", "test", "img/user/default.png", date, date);
		
		Board boardSave = boardRepository.save(new Board("t", "c", user, date, date));
		Assert.assertThat(boardSave.getTitle(), is("t"));
		Assert.assertThat(boardSave.getContent(), is("c"));
		
		List<Board> boardList2 = (List<Board>) boardRepository.findAll();
		Assert.assertThat(boardList2.size(), is(2));
		
		Board board = boardList2.get(1);
		Assert.assertThat(boardSave.getIdx(), is(board.getIdx()));
		Assert.assertThat(boardSave.getContent(), is(board.getContent()));
		Assert.assertThat(boardSave.getTitle(), is(board.getTitle()));
		Assert.assertThat(boardSave.getHitCount(), is(board.getHitCount()));
	}
	
	@Test
	public void testUpdate(){
		Board board = boardRepository.findOne(1);
		Assert.assertThat(board.getTitle(), is("title"));
		Assert.assertThat(board.getContent(), is("content"));
		board.setContent("cccc");
		board.setTitle("tttt");
		boardRepository.save(board);
		Board boardUpdate = boardRepository.findOne(1);
		Assert.assertThat(boardUpdate.getTitle(), is("tttt"));
		Assert.assertThat(boardUpdate.getContent(), is("cccc"));
	}
}
