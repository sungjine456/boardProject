package kr.co.person.repository;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.joda.time.DateTime;
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
	DateTime date = new DateTime();
	private Comment comment;

	@Test
	public void testFindOne() {
		comment = commentRepository.findOne(1);
		Assert.assertThat(comment.getComment(), is("comment1"));
	}
	
	@Test
	public void testSave(){
		User user = new User("tes", "test@naver.com", "test", "test", "img/user/default.png", date, date);
		Board board = new Board("title", "content", user, date, date);
		
		comment = commentRepository.save(new Comment("c", 0, 0, 0, user, board, date, date));
		Assert.assertThat(comment.getComment(), is("c"));
	}
	
	@Test
	public void testFindAll(){
		List<Comment> comments = commentRepository.findAll();
		Assert.assertThat(comments.size(), is(8));
		Assert.assertThat(comments.get(0).getComment(), is("comment1"));
	}
	
	@Test
	public void testFindByBoard(){
		List<Comment> comments = commentRepository.findByBoardIdx(1);
		Assert.assertThat(comments.size(), is(8));
		Assert.assertThat(comments.get(0).getComment(), is("comment1"));
		comments = commentRepository.findByBoardIdx(2);
		Assert.assertThat(comments.size(), is(0));
	}
	
	@Test
	public void testFindByCircleAndStepGreaterThanAndDepthLessThanEqual(){
		List<Comment> comments = commentRepository.findByBoardIdxAndCircleAndStepGreaterThanAndDepthLessThanEqualOrderByStepAsc(1, 1, 1, 1);
		Assert.assertThat(comments.size(), is(3));
		comments = commentRepository.findByBoardIdxAndCircleAndStepGreaterThanAndDepthLessThanEqualOrderByStepAsc(1, 1, 2, 1);
		Assert.assertThat(comments.size(), is(2));
		comments = commentRepository.findByBoardIdxAndCircleAndStepGreaterThanAndDepthLessThanEqualOrderByStepAsc(1, 1, 3, 2);
		Assert.assertThat(comments.size(), is(2));
	}
	
	@Test
	public void testFindByOrderByCircleDescStepAsc(){
		List<Comment> comments = commentRepository.findByBoardIdxOrderByCircleDescStepAsc(1);
		Assert.assertThat(comments.size(), is(8));
		Assert.assertThat(comments.get(0).getIdx(), is(8));
		Assert.assertThat(comments.get(1).getIdx(), is(7));
		Assert.assertThat(comments.get(2).getIdx(), is(6));
		Assert.assertThat(comments.get(3).getIdx(), is(4));
		Assert.assertThat(comments.get(4).getIdx(), is(3));
		Assert.assertThat(comments.get(5).getIdx(), is(2));
		Assert.assertThat(comments.get(6).getIdx(), is(1));
		Assert.assertThat(comments.get(7).getIdx(), is(5));
	}
	
	@Test
	public void testFindByBoardIdxAndCircleOrderByStepDesc(){
		List<Comment> comments = commentRepository.findByBoardIdxAndCircleOrderByStepDesc(1, 1);
		Assert.assertThat(comments.size(), is(5));
		Assert.assertThat(comments.get(4).getIdx(), is(2));
		Assert.assertThat(comments.get(3).getIdx(), is(3));
		Assert.assertThat(comments.get(2).getIdx(), is(4));
		Assert.assertThat(comments.get(1).getIdx(), is(6));
		Assert.assertThat(comments.get(0).getIdx(), is(7));
	}
	
	@Test
	public void testFindByBoardIdxAndCircleAndStepGreaterThan(){
		List<Comment> comments = commentRepository.findByBoardIdxAndCircleAndStepGreaterThan(1, 1, 1);
		Assert.assertThat(comments.size(), is(4));
		Assert.assertThat(comments.get(0).getIdx(), is(3));
		Assert.assertThat(comments.get(1).getIdx(), is(4));
		Assert.assertThat(comments.get(2).getIdx(), is(6));
		Assert.assertThat(comments.get(3).getIdx(), is(7));
	}
}
