package kr.co.person.common;

import static org.hamcrest.CoreMatchers.is;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.person.BoardProjectApplication;
import kr.co.person.domain.Board;
import kr.co.person.domain.Comment;
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
public class IsValidTest {

	@Test
	public void testIsValidInts() {
		Assert.assertThat(IsValid.isValidInts(-1), is(false));
		Assert.assertThat(IsValid.isValidInts(0), is(false));
		Assert.assertThat(IsValid.isValidInts(1), is(true));
		Assert.assertThat(IsValid.isValidInts(0, 0), is(false));
		Assert.assertThat(IsValid.isValidInts(1, 0), is(false));
		Assert.assertThat(IsValid.isValidInts(0, 1), is(false));
		Assert.assertThat(IsValid.isValidInts(1, 1), is(true));
		Assert.assertThat(IsValid.isValidInts(0, 0, 0), is(false));
		Assert.assertThat(IsValid.isValidInts(0, 0, 1), is(false));
		Assert.assertThat(IsValid.isValidInts(0, 1, 0), is(false));
		Assert.assertThat(IsValid.isValidInts(1, 0, 0), is(false));
		Assert.assertThat(IsValid.isValidInts(0, 1, 1), is(false));
		Assert.assertThat(IsValid.isValidInts(1, 0, 1), is(false));
		Assert.assertThat(IsValid.isValidInts(1, 1, 0), is(false));
		Assert.assertThat(IsValid.isValidInts(1, 1, 1), is(true));
	}
	
	@Test
	public void testIsNotValidInts() {
		Assert.assertThat(IsValid.isNotValidInts(-1), is(true));
		Assert.assertThat(IsValid.isNotValidInts(0), is(true));
		Assert.assertThat(IsValid.isNotValidInts(1), is(false));
		Assert.assertThat(IsValid.isNotValidInts(0, 0), is(true));
		Assert.assertThat(IsValid.isNotValidInts(1, 0), is(true));
		Assert.assertThat(IsValid.isNotValidInts(0, 1), is(true));
		Assert.assertThat(IsValid.isNotValidInts(1, 1), is(false));
		Assert.assertThat(IsValid.isNotValidInts(0, 0, 0), is(true));
		Assert.assertThat(IsValid.isNotValidInts(0, 0, 1), is(true));
		Assert.assertThat(IsValid.isNotValidInts(0, 1, 0), is(true));
		Assert.assertThat(IsValid.isNotValidInts(1, 0, 0), is(true));
		Assert.assertThat(IsValid.isNotValidInts(0, 1, 1), is(true));
		Assert.assertThat(IsValid.isNotValidInts(1, 0, 1), is(true));
		Assert.assertThat(IsValid.isNotValidInts(1, 1, 0), is(true));
		Assert.assertThat(IsValid.isNotValidInts(1, 1, 1), is(false));
	}

	@Test
	public void testIsValidObjects() {
		Assert.assertThat(IsValid.isValidObjects(new Object()), is(true));
		Assert.assertThat(IsValid.isValidObjects(null, null), is(false));
		Assert.assertThat(IsValid.isValidObjects(new Object(), null), is(false));
		Assert.assertThat(IsValid.isValidObjects(null, new Object()), is(false));
		Assert.assertThat(IsValid.isValidObjects(new Object(), new Object()), is(true));
		Assert.assertThat(IsValid.isValidObjects(null, null, null), is(false));
		Assert.assertThat(IsValid.isValidObjects(null, null, new Object()), is(false));
		Assert.assertThat(IsValid.isValidObjects(null, new Object(), null), is(false));
		Assert.assertThat(IsValid.isValidObjects(new Object(), null, null), is(false));
		Assert.assertThat(IsValid.isValidObjects(null, new Object(), new Object()), is(false));
		Assert.assertThat(IsValid.isValidObjects(new Object(), null, new Object()), is(false));
		Assert.assertThat(IsValid.isValidObjects(new Object(), new Object(), null), is(false));
		Assert.assertThat(IsValid.isValidObjects(new Object(), new Object(), new Object()), is(true));
	}
	
	@Test
	public void testIsNotValidObjects() {
		Assert.assertThat(IsValid.isNotValidObjects(new Object()), is(false));
		Assert.assertThat(IsValid.isNotValidObjects(null, null), is(true));
		Assert.assertThat(IsValid.isNotValidObjects(new Object(), null), is(true));
		Assert.assertThat(IsValid.isNotValidObjects(null, new Object()), is(true));
		Assert.assertThat(IsValid.isNotValidObjects(new Object(), new Object()), is(false));
		Assert.assertThat(IsValid.isNotValidObjects(null, null, null), is(true));
		Assert.assertThat(IsValid.isNotValidObjects(null, null, new Object()), is(true));
		Assert.assertThat(IsValid.isNotValidObjects(null, new Object(), null), is(true));
		Assert.assertThat(IsValid.isNotValidObjects(new Object(), null, null), is(true));
		Assert.assertThat(IsValid.isNotValidObjects(null, new Object(), new Object()), is(true));
		Assert.assertThat(IsValid.isNotValidObjects(new Object(), null, new Object()), is(true));
		Assert.assertThat(IsValid.isNotValidObjects(new Object(), new Object(), null), is(true));
		Assert.assertThat(IsValid.isNotValidObjects(new Object(), new Object(), new Object()), is(false));
	}
	
	@Test
	public void testIsValidArrays() {
		Assert.assertThat(IsValid.isValidArrays(new Object[0]), is(false));
		Assert.assertThat(IsValid.isValidArrays(new Object[1]), is(true));
		Assert.assertThat(IsValid.isValidArrays(null, null), is(false));
		Assert.assertThat(IsValid.isValidArrays(new Object[0], null), is(false));
		Assert.assertThat(IsValid.isValidArrays(null, new Object[0]), is(false));
		Assert.assertThat(IsValid.isValidArrays(new Object[1], null), is(false));
		Assert.assertThat(IsValid.isValidArrays(null, new Object[1]), is(false));
		Assert.assertThat(IsValid.isValidArrays(new Object[0], new Object[0]), is(false));
		Assert.assertThat(IsValid.isValidArrays(new Object[1], new Object[1]), is(true));
		Assert.assertThat(IsValid.isValidArrays(null, null, null), is(false));
		Assert.assertThat(IsValid.isValidArrays(null, null, new Object[0]), is(false));
		Assert.assertThat(IsValid.isValidArrays(null, new Object[0], null), is(false));
		Assert.assertThat(IsValid.isValidArrays(new Object[0], null, null), is(false));
		Assert.assertThat(IsValid.isValidArrays(null, null, new Object[1]), is(false));
		Assert.assertThat(IsValid.isValidArrays(null, new Object[1], null), is(false));
		Assert.assertThat(IsValid.isValidArrays(new Object[1], null, null), is(false));
		Assert.assertThat(IsValid.isValidArrays(null, new Object[1], new Object[1]), is(false));
		Assert.assertThat(IsValid.isValidArrays(new Object[1], null, new Object[1]), is(false));
		Assert.assertThat(IsValid.isValidArrays(new Object[1], new Object[1], null), is(false));
		Assert.assertThat(IsValid.isValidArrays(new Object[0], new Object[0], new Object[0]), is(false));
		Assert.assertThat(IsValid.isValidArrays(new Object[1], new Object[1], new Object[1]), is(true));
	}
	
	@Test
	public void testIsNotValidArrays() {
		Assert.assertThat(IsValid.isNotValidArrays(new Object[1]), is(false));
		Assert.assertThat(IsValid.isNotValidArrays(new Object[0]), is(true));
		Assert.assertThat(IsValid.isNotValidArrays(null, null), is(true));
		Assert.assertThat(IsValid.isNotValidArrays(new Object[1], null), is(true));
		Assert.assertThat(IsValid.isNotValidArrays(null, new Object[1]), is(true));
		Assert.assertThat(IsValid.isNotValidArrays(new Object[0], new Object[0]), is(true));
		Assert.assertThat(IsValid.isNotValidArrays(new Object[1], new Object[1]), is(false));
		Assert.assertThat(IsValid.isNotValidArrays(null, null, null), is(true));
		Assert.assertThat(IsValid.isNotValidArrays(null, null, new Object[1]), is(true));
		Assert.assertThat(IsValid.isNotValidArrays(null, new Object[1], null), is(true));
		Assert.assertThat(IsValid.isNotValidArrays(new Object[1], null, null), is(true));
		Assert.assertThat(IsValid.isNotValidArrays(null, new Object[1], new Object[1]), is(true));
		Assert.assertThat(IsValid.isNotValidArrays(new Object[1], null, new Object[1]), is(true));
		Assert.assertThat(IsValid.isNotValidArrays(new Object[1], new Object[1], null), is(true));
		Assert.assertThat(IsValid.isNotValidArrays(new Object[0], new Object[0], new Object[0]), is(true));
		Assert.assertThat(IsValid.isNotValidArrays(new Object[1], new Object[1], new Object[1]), is(false));
	}
	
	@Test
	public void testIsValidUser(){
		LocalDateTime date = LocalDateTime.now();
		User user = null;
		Assert.assertThat(IsValid.isValidUser(user), is(false));
		user = new User();
		Assert.assertThat(IsValid.isValidUser(user), is(false));
		user = new User("id", "email", "", "name", "img", date, date);
		Assert.assertThat(IsValid.isValidUser(user), is(false));
		user = new User("id", "", "password", "name", "img", date, date);
		Assert.assertThat(IsValid.isValidUser(user), is(false));
		user = new User("", "email", "password", "name", "img", date, date);
		Assert.assertThat(IsValid.isValidUser(user), is(false));
		user = new User("id", "email", "password", "name", "img", date, date);
		Assert.assertThat(IsValid.isValidUser(user), is(false));
		user = new User("id", "email", "password", "name", "img", date, date);
		user.setIdx(1);
		Assert.assertThat(IsValid.isValidUser(user), is(true));
	}

	@Test
	public void testIsNotValidUser(){
		LocalDateTime date = LocalDateTime.now();
		User user = null;
		Assert.assertThat(IsValid.isNotValidUser(user), is(true));
		user = new User();
		Assert.assertThat(IsValid.isNotValidUser(user), is(true));
		user = new User("id", "email", "", "name", "img", date, date);
		Assert.assertThat(IsValid.isNotValidUser(user), is(true));
		user = new User("id", "", "password", "name", "img", date, date);
		Assert.assertThat(IsValid.isNotValidUser(user), is(true));
		user = new User("", "email", "password", "name", "img", date, date);
		Assert.assertThat(IsValid.isNotValidUser(user), is(true));
		user = new User("id", "email", "password", "name", "img", date, date);
		Assert.assertThat(IsValid.isNotValidUser(user), is(true));
		user = new User("id", "email", "password", "name", "img", date, date);
		user.setIdx(1);
		Assert.assertThat(IsValid.isNotValidUser(user), is(false));
	}
	
	@Test
	public void testIsValidBoard(){
		LocalDateTime date = LocalDateTime.now();
		Board board = null;
		User user = new User();
		Assert.assertThat(IsValid.isValidBoard(board), is(false));
		board = new Board();
		Assert.assertThat(IsValid.isValidBoard(board), is(false));
		board = new Board("title", "content", user, date, date);
		Assert.assertThat(IsValid.isValidBoard(board), is(false));
		user = new User("test", "test@av.co", "123", "test", "img/test", date, date);
		user.setIdx(1);
		board = new Board("title", "content", user, date, date);
		Assert.assertThat(IsValid.isValidBoard(board), is(false));
		board = new Board("title", "content", user, date, date);
		board.setIdx(1);
		Assert.assertThat(IsValid.isValidBoard(board), is(true));
	}
	
	@Test
	public void testIsNotValidBoard(){
		LocalDateTime date = LocalDateTime.now();
		Board board = null;
		User user = new User();
		Assert.assertThat(IsValid.isNotValidBoard(board), is(true));
		board = new Board();
		Assert.assertThat(IsValid.isNotValidBoard(board), is(true));
		board = new Board("title", "content", user, date, date);
		Assert.assertThat(IsValid.isNotValidBoard(board), is(true));
		user = new User("test", "test@av.co", "123", "test", "img/test", date, date);
		user.setIdx(1);
		board = new Board("title", "content", user, date, date);
		Assert.assertThat(IsValid.isNotValidBoard(board), is(true));
		board = new Board("title", "content", user, date, date);
		board.setIdx(1);
		Assert.assertThat(IsValid.isNotValidBoard(board), is(false));
	}
	
	@Test
	public void testIsValidComment(){
		LocalDateTime date = LocalDateTime.now();
		Comment comment = null;
		User user = new User();
		Board board = new Board();
		Assert.assertThat(IsValid.isValidComment(comment), is(false));
		comment = new Comment();
		Assert.assertThat(IsValid.isValidComment(comment), is(false));
		comment = new Comment("comment", 0, 0, 0, user, board, date, date);
		Assert.assertThat(IsValid.isValidComment(comment), is(false));
		user = new User("test", "test@av.co", "123", "test", "img/test", date, date);
		board = new Board("title", "content", user, date, date);
		comment = new Comment("comment", 0, 0, 0, user, board, date, date);
		Assert.assertThat(IsValid.isValidComment(comment), is(false));
		user.setIdx(1);
		board.setIdx(1);
		comment.setIdx(1);
		Assert.assertThat(IsValid.isValidComment(comment), is(true));
	}
	
	@Test
	public void testIsNotValidComment(){
		LocalDateTime date = LocalDateTime.now();
		Comment comment = null;
		User user = new User();
		Board board = new Board();
		Assert.assertThat(IsValid.isNotValidComment(comment), is(true));
		comment = new Comment();
		Assert.assertThat(IsValid.isNotValidComment(comment), is(true));
		comment = new Comment("comment", 0, 0, 0, user, board, date, date);
		Assert.assertThat(IsValid.isNotValidComment(comment), is(true));
		user = new User("test", "test@av.co", "123", "test", "img/test", date, date);
		board = new Board("title", "content", user, date, date);
		comment = new Comment("comment", 0, 0, 0, user, board, date, date);
		Assert.assertThat(IsValid.isNotValidComment(comment), is(true));
		user.setIdx(1);
		board.setIdx(1);
		comment.setIdx(1);
		Assert.assertThat(IsValid.isNotValidComment(comment), is(false));
	}
}
