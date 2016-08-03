package kr.co.person.service;

import static org.hamcrest.CoreMatchers.*;

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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class BoardServiceTest {

	@Autowired private BoardService boardService;
	private Board board;
	
	@Test
	public void testSave() {
		Assert.assertThat(boardService.write("", "content", 1).getMessage(), is("제목을 입력해주세요."));
		Assert.assertThat(boardService.write("title", "", 1).getMessage(), is("내용을 입력해주세요."));
		Assert.assertThat(boardService.write("title", "content", 0).getMessage(), is("유효한 회원이 아닙니다."));
		Assert.assertThat(boardService.write("title", "content", 1).getMessage(), is("글이 등록 되었습니다."));
	}
	
	@Test
	public void testUpdate(){
		board = boardService.findBoardForIdx(1);
		Assert.assertThat(board.getRegDate(), is(board.getUpDate()));
		Assert.assertThat(boardService.update(1, "hyun", ""), is(false));
		Assert.assertThat(boardService.update(1, "", "hyun"), is(false));
		Assert.assertThat(boardService.update(5, "hyun", "hyun"), is(false));
		Assert.assertThat(boardService.update(1, "ttttt", "ccccc"), is(true));
		board = boardService.findBoardForIdx(1);
		Assert.assertThat(board.getRegDate(), is(not(board.getUpDate())));
	}
	
	@Test
	public void testAddHitCount(){
		board = boardService.findBoardForIdx(1);
		Assert.assertThat(board.getHitCount(), is(0));
		boardService.addHitCount(1);
		board = boardService.findBoardForIdx(1);
		Assert.assertThat(board.getHitCount(), is(1));
		boardService.addHitCount(1);
		board = boardService.findBoardForIdx(1);
		Assert.assertThat(board.getHitCount(), is(2));
	}
	
	@Test
	public void testGetBoardLikeCount(){
		Assert.assertThat(boardService.getBoardLikeCount(0), is(-1));
		Assert.assertThat(boardService.getBoardLikeCount(1), is(1));
		Assert.assertThat(boardService.getBoardLikeCount(2), is(-1));
	}
	
	@Test
	public void testGetBoardLike(){
		Assert.assertThat(boardService.getBoardLike(1, 1), is(nullValue()));
		Assert.assertThat(boardService.getBoardLike(1, 2), is(notNullValue()));
		Assert.assertThat(boardService.getBoardLike(2, 1), is(nullValue()));
		Assert.assertThat(boardService.getBoardLike(2, 2), is(nullValue()));
		Assert.assertThat(boardService.getBoardLike(52, 22), is(nullValue()));
	}
	
	@Test
	public void testAddBoardLike(){
		Assert.assertThat(boardService.getBoardLike(1, 1), is(nullValue()));
		Assert.assertThat(boardService.addBoardLike(0, 0), is(false));
		Assert.assertThat(boardService.addBoardLike(0, 1), is(false));
		Assert.assertThat(boardService.addBoardLike(1, 0), is(false));
		Assert.assertThat(boardService.addBoardLike(66, 350), is(false));
		Assert.assertThat(boardService.addBoardLike(1, 350), is(false));
		Assert.assertThat(boardService.addBoardLike(121, 1), is(false));
		Assert.assertThat(boardService.addBoardLike(1, 2), is(false));
		Assert.assertThat(boardService.addBoardLike(1, 1), is(true));
		Assert.assertThat(boardService.getBoardLike(1, 1), is(notNullValue()));
	}
	
	@Test
	public void testRemoveBoardLike(){
		Assert.assertThat(boardService.getBoardLike(1, 2), is(notNullValue()));
		Assert.assertThat(boardService.removeBoardLike(0, 0), is(false));
		Assert.assertThat(boardService.removeBoardLike(1, 0), is(false));
		Assert.assertThat(boardService.removeBoardLike(0, 2), is(false));
		Assert.assertThat(boardService.removeBoardLike(1, 2), is(true));
		Assert.assertThat(boardService.getBoardLike(1, 2), is(nullValue()));
	}
}
