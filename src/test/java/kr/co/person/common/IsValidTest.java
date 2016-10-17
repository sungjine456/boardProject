package kr.co.person.common;

import static org.hamcrest.CoreMatchers.is;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import kr.co.person.BoardProjectApplication;
import kr.co.person.domain.Board;
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
public class IsValidTest {

	@Test
	public void testIsValidInts() {
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
		DateTime date = new DateTime();
		User user = null;
		Assert.assertThat(IsValid.isValidUser(user), is(false));
		user = new User();
		Assert.assertThat(IsValid.isValidUser(user), is(false));
		user = new User("id", "email", "password", "name", "img", date, date);
		Assert.assertThat(IsValid.isValidUser(user), is(true));
	}

	@Test
	public void testIsNotValidUser(){
		DateTime date = new DateTime();
		User user = null;
		Assert.assertThat(IsValid.isNotValidUser(user), is(true));
		user = new User();
		Assert.assertThat(IsValid.isNotValidUser(user), is(true));
		user = new User("id", "email", "password", "name", "img", date, date);
		Assert.assertThat(IsValid.isNotValidUser(user), is(false));
	}
	
	@Test
	public void testIsValidBoard(){
		DateTime date = new DateTime();
		Board board = null;
		Assert.assertThat(IsValid.isValidBoard(board), is(false));
		board = new Board();
		Assert.assertThat(IsValid.isValidBoard(board), is(false));
		board = new Board("title", "content", new User(), date, date);
		Assert.assertThat(IsValid.isValidBoard(board), is(true));
	}
	
	@Test
	public void testIsNotValidBoard(){
		DateTime date = new DateTime();
		Board board = null;
		Assert.assertThat(IsValid.isNotValidBoard(board), is(true));
		board = new Board();
		Assert.assertThat(IsValid.isNotValidBoard(board), is(true));
		board = new Board("title", "content", new User(), date, date);
		Assert.assertThat(IsValid.isNotValidBoard(board), is(false));
	}
}
