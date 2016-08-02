package kr.co.person.service;

import static org.hamcrest.CoreMatchers.*;

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

	@Autowired private CommentService commentService;
	
	@Test
	public void testFindAllCommentByBoard() {
		List<Comment> comments = commentService.findAllCommentByBoard(0);
		Assert.assertThat(comments, is(new ArrayList<Comment>()));
		comments = commentService.findAllCommentByBoard(1);
		Assert.assertThat(comments.size(), is(8));
	}
	
	@Test
	public void testWrite(){
		Assert.assertThat(commentService.write("comment write test", 1, 3), is(false));
		Assert.assertThat(commentService.write("comment write test", 3, 1), is(false));
		Assert.assertThat(commentService.write("comment write test", 3, 3), is(false));
		Assert.assertThat(commentService.write("comment write test", 0, 0), is(false));
		Assert.assertThat(commentService.write("", 1, 1), is(false));
		Assert.assertThat(commentService.write(null, 1, 1), is(false));
		Assert.assertThat(commentService.write("comment write test", 1, 1), is(true));
	}
	
	@Test
	public void testUpdate(){
		Assert.assertThat(commentService.update(0, "comment update test"), is(false));
		Assert.assertThat(commentService.update(9, "comment update test"), is(false));
		Assert.assertThat(commentService.update(9, ""), is(false));
		Assert.assertThat(commentService.update(9, null), is(false));
		// findAllCommentByBoard는 idx 역순으로 리턴 하기때문에 마지막 뎃글로 테스트. 
		Assert.assertThat(commentService.update(5, "comment update test"), is(true));
		List<Comment> comments = commentService.findAllCommentByBoard(1);
		Assert.assertThat(comments.get(0).getComment(), is("comment update test"));
	}
}
