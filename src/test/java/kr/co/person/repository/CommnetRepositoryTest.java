package kr.co.person.repository;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
	
	@Autowired private CommentRepository commentRepository;
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
	public void testGetCommentListBoardIdxAndCircleAndStepAndDepth(){
		List<Comment> comments = commentRepository.getCommentList(1, 1, 1, 1);
		Assert.assertThat(comments.size(), is(3));
		Assert.assertThat(comments.get(0).getIdx(), is(3));
		Assert.assertThat(comments.get(1).getIdx(), is(6));
		Assert.assertThat(comments.get(2).getIdx(), is(7));
		comments = commentRepository.getCommentList(1, 1, 2, 1);
		Assert.assertThat(comments.size(), is(2));
		Assert.assertThat(comments.get(0).getIdx(), is(6));
		Assert.assertThat(comments.get(1).getIdx(), is(7));
		comments = commentRepository.getCommentList(1, 1, 3, 2);
		Assert.assertThat(comments.size(), is(2));
		Assert.assertThat(comments.get(0).getIdx(), is(6));
		Assert.assertThat(comments.get(1).getIdx(), is(7));
		comments = commentRepository.getCommentList(1, 1, 3, 1);
		Assert.assertThat(comments.size(), is(2));
		Assert.assertThat(comments.get(0).getIdx(), is(6));
		Assert.assertThat(comments.get(1).getIdx(), is(7));
	}
	
	@Test
	public void testGetCommentListBoardIdx(){
		Pageable pageable = new PageRequest(0, 10, new Sort(
			    new Sort.Order(Direction.DESC, "circle"),
			    new Sort.Order(Direction.ASC, "step")));
		Page<Comment> pages = commentRepository.findByBoardIdx(1, pageable);
		List<Comment> comments = pages.getContent();
		Assert.assertThat(comments.size(), is(8));
		Assert.assertThat(comments.get(0).getIdx(), is(5));
		Assert.assertThat(comments.get(1).getIdx(), is(8));
		Assert.assertThat(comments.get(2).getIdx(), is(1));
		Assert.assertThat(comments.get(3).getIdx(), is(2));
		Assert.assertThat(comments.get(4).getIdx(), is(3));
		Assert.assertThat(comments.get(5).getIdx(), is(4));
		Assert.assertThat(comments.get(6).getIdx(), is(6));
		Assert.assertThat(comments.get(7).getIdx(), is(7));
	}
	
	@Test
	public void testGetCommentListBoardIdxAndCircle(){
		List<Comment> comments = commentRepository.getCommentList(1, 1);
		Assert.assertThat(comments.size(), is(6));
		Assert.assertThat(comments.get(5).getIdx(), is(1));
		Assert.assertThat(comments.get(4).getIdx(), is(2));
		Assert.assertThat(comments.get(3).getIdx(), is(3));
		Assert.assertThat(comments.get(2).getIdx(), is(4));
		Assert.assertThat(comments.get(1).getIdx(), is(6));
		Assert.assertThat(comments.get(0).getIdx(), is(7));
	}
	
	@Test
	public void testGetCommentListBoardIdxAndCircleAndStep(){
		List<Comment> comments = commentRepository.getCommentList(1, 1, 1);
		Assert.assertThat(comments.size(), is(4));
		Assert.assertThat(comments.get(0).getIdx(), is(3));
		Assert.assertThat(comments.get(1).getIdx(), is(4));
		Assert.assertThat(comments.get(2).getIdx(), is(6));
		Assert.assertThat(comments.get(3).getIdx(), is(7));
	}
	
	@Test
	public void testSaveComment(){
		User user = new User("tes", "test@naver.com", "test", "test", "img/user/default.png", date, date);
		Board board = new Board("title", "content", user, date, date);
		
		Comment comment = new Comment("testComment", 0, 0, 0, user, board, date, date);
		commentRepository.saveComment(comment);
		List<Comment> comments = commentRepository.findAll();
		int size = comments.size() - 1;
		Assert.assertThat(comments.get(size).getCircle(), is(comments.get(size).getIdx()));
	}
	
	@Test
	public void testUpdateComment(){
		List<Comment> comments = commentRepository.getCommentList(1, 1, 1);
		Assert.assertThat(comments.size(), is(4));
		Assert.assertThat(comments.get(0).getStep(), is(2));
		Assert.assertThat(comments.get(1).getStep(), is(3));
		Assert.assertThat(comments.get(2).getStep(), is(4));
		Assert.assertThat(comments.get(3).getStep(), is(5));
		commentRepository.updateComment(1, 1, 1);
		comments = commentRepository.getCommentList(1, 1, 1);
		Assert.assertThat(comments.size(), is(4));
		Assert.assertThat(comments.get(0).getStep(), is(3));
		Assert.assertThat(comments.get(1).getStep(), is(4));
		Assert.assertThat(comments.get(2).getStep(), is(5));
		Assert.assertThat(comments.get(3).getStep(), is(6));
	}
}
