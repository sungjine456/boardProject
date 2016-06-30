package kr.co.person.service;

import java.util.ArrayList;
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
import kr.co.person.domain.Comment;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class CommentServiceTest {

	@Autowired
	private CommentService commentService;
	
	@Test
	public void testFindAllCommentByBoard() {
		List<Comment> comments = commentService.findAllCommentByBoard(0);
		Assert.assertEquals(new ArrayList<Comment>(), comments);
		comments = commentService.findAllCommentByBoard(1);
		Assert.assertEquals(1, comments.size());
	}
	
	@Test
	public void testWrite(){
		Assert.assertFalse(commentService.write("comment write test", 1, 3));
		Assert.assertFalse(commentService.write("comment write test", 3, 1));
		Assert.assertFalse(commentService.write("comment write test", 3, 3));
		Assert.assertFalse(commentService.write("comment write test", 0, 0));
		Assert.assertFalse(commentService.write("", 1, 1));
		Assert.assertFalse(commentService.write(null, 1, 1));
		Assert.assertTrue(commentService.write("comment write test", 1, 1));
	}
	
	@Test
	public void testUpdate(){
		Assert.assertFalse(commentService.update(0, "comment update test"));
		Assert.assertFalse(commentService.update(3, "comment update test"));
		Assert.assertFalse(commentService.update(3, ""));
		Assert.assertFalse(commentService.update(3, null));
		Assert.assertTrue(commentService.update(1, "comment update test"));
		List<Comment> comments = commentService.findAllCommentByBoard(1);
		Assert.assertEquals("comment update test", comments.get(0).getComment());
	}
}
