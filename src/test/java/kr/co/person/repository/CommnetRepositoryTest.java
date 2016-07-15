package kr.co.person.repository;

import static org.hamcrest.CoreMatchers.is;

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
	private Comment comment;

	@Test
	public void testFindOne() {
		comment = commentRepository.findOne(1);
		Assert.assertThat(comment.getComment(), is("comment"));
	}
	
	@Test
	public void testSave(){
		User user = new User("tes", "test@naver.com", "test", "test", "img/user/default.png", date, date);
		Board board = new Board("title", "content", user, date, date);
		
		comment = commentRepository.save(new Comment("c", 0, 0, 0, 0, user, board, date, date));
		Assert.assertThat(comment.getComment(), is("c"));
	}
	
	@Test
	public void testFindAll(){
		List<Comment> comments = commentRepository.findAll();
		Assert.assertThat(comments.size(), is(1));
		Assert.assertThat(comments.get(0).getComment(), is("comment"));
	}
	
	@Test
	public void testFindAllByBoard(){
		List<Comment> comments = commentRepository.findAllByBoardIdx(1);
		Assert.assertThat(comments.size(), is(1));
		Assert.assertThat(comments.get(0).getComment(), is("comment"));
		comments = commentRepository.findAllByBoardIdx(2);
		Assert.assertThat(comments.size(), is(0));
	}
}
