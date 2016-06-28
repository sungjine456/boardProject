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
import kr.co.person.domain.Comment;
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class CommnetRepositoryTest {
	
	@Autowired
	private CommentRepository commentRepository;
	Date date = new Date();
	private User user = new User("tes", "test@naver.com", "test", "test", date, date);
	private Board board = new Board("title", "content", user, date, date);
	private Comment comment;

	@Test
	public void testFindOne() {
		comment = commentRepository.findOne(1);
		Assert.assertEquals("comment", comment.getComment());
	}
	
	@Test
	public void testSave(){
		comment = new Comment("c", user, board, date, date);
		comment = commentRepository.save(comment);
		Assert.assertEquals("c", comment.getComment());
	}
	
	@Test
	public void testFindAll(){
		comment = new Comment("c", user, board, date, date);
		comment = commentRepository.save(comment);
		List<Comment> comments = commentRepository.findAll();
		Assert.assertEquals("comment", comments.get(0).getComment());
		Assert.assertEquals("c", comments.get(1).getComment());
	}
	
	@Test
	public void testFindAllByBoard(){
		List<Comment> comments = commentRepository.findAllByBoardIdx(1);
		Assert.assertEquals("comment", comments.get(0).getComment());
		comment = new Comment("c", user, board, date, date);
		comment = commentRepository.save(comment);
		Assert.assertEquals(1, comments.size());
		Assert.assertEquals("comment", comments.get(0).getComment());
	}
}
