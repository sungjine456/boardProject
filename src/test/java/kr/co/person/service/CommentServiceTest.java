package kr.co.person.service;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.person.BoardProjectApplication;
import kr.co.person.domain.Comment;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class CommentServiceTest {

	@Autowired private CommentService commentService;
	
	@Test
	public void testFindAllCommentByBoard() {
		Pageable pageable = new PageRequest(0, 10, new Sort(
			    new Sort.Order(Direction.DESC, "circle"),
			    new Sort.Order(Direction.ASC, "step")));
		Page<Comment> pages = commentService.findAllCommentByBoard(0, pageable);
		List<Comment> comments = pages.getContent();
		Assert.assertThat(comments.size(), is(0));
		pages = commentService.findAllCommentByBoard(1, pageable);
		comments = pages.getContent();
		Assert.assertThat(comments.size(), is(8));
	}
	
	@Test
	public void testWrite(){
		Pageable pageable = new PageRequest(0, 10, new Sort(
			    new Sort.Order(Direction.DESC, "circle"),
			    new Sort.Order(Direction.ASC, "step")));
		Page<Comment> pages = commentService.findAllCommentByBoard(1, pageable);
		List<Comment> comments = pages.getContent();
		Assert.assertThat(comments.size(), is(8));
		Assert.assertThat(commentService.write("comment write test", 1, 3), is(false));
		Assert.assertThat(commentService.write("comment write test", 100, 1), is(false));
		Assert.assertThat(commentService.write("comment write test", 100, 100), is(false));
		Assert.assertThat(commentService.write("comment write test", 0, 0), is(false));
		Assert.assertThat(commentService.write("comment write test", 1, 1), is(true));
		Page<Comment> reFindPages = commentService.findAllCommentByBoard(1, pageable);
		List<Comment> reFindComments = reFindPages.getContent();
		Assert.assertThat(reFindComments.size(), is(9));
	}
	
	@Test
	public void testUpdate(){
		Assert.assertThat(commentService.update(0, "comment update test"), is(false));
		Assert.assertThat(commentService.update(9, "comment update test"), is(false));
		Assert.assertThat(commentService.update(9, ""), is(false));
		Assert.assertThat(commentService.update(9, null), is(false));
		// findAllCommentByBoard는 idx 역순으로 리턴 하기때문에 마지막 뎃글로 테스트. 
		Assert.assertThat(commentService.update(5, "comment update test"), is(true));
		Pageable pageable = new PageRequest(0, 10, new Sort(
			    new Sort.Order(Direction.DESC, "circle"),
			    new Sort.Order(Direction.ASC, "step")));
		Page<Comment> pages = commentService.findAllCommentByBoard(1, pageable);
		List<Comment> comments = pages.getContent();
		Assert.assertThat(comments.get(0).getComment(), is("comment update test"));
	}
	
	@Test
	public void testReplyWrite(){
		Pageable pageable = new PageRequest(0, 10, new Sort(
			    new Sort.Order(Direction.DESC, "circle"),
			    new Sort.Order(Direction.ASC, "step")));
		Page<Comment> pages = commentService.findAllCommentByBoard(1, pageable);
		List<Comment> comments = pages.getContent();
		Assert.assertThat(comments.size(), is(8));
		Assert.assertThat(commentService.replyWrite(100, "comment", 1, 1), is(false));
		Assert.assertThat(commentService.replyWrite(1, "comment", 100, 1), is(false));
		Assert.assertThat(commentService.replyWrite(1, "comment", 1, 100), is(false));
		Assert.assertThat(commentService.replyWrite(1, "comment", 1, 1), is(true));
		Page<Comment> reFindPages = commentService.findAllCommentByBoard(1, pageable);
		List<Comment> reFindComments = reFindPages.getContent();
		Assert.assertThat(reFindComments.size(), is(9));
	}
}
