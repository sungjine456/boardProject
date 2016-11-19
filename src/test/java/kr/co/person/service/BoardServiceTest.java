package kr.co.person.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.person.BoardProjectApplication;
import kr.co.person.common.Message;
import kr.co.person.domain.Board;
import kr.co.person.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class BoardServiceTest {

	@Autowired private BoardService boardService;
	@Autowired private Message message;
	private Board board;
	
	@Test
	public void testWrite() {
		board = boardService.findBoardForIdx(2);
		Assert.assertThat(board.getIdx(), is(0));
		Assert.assertThat(boardService.write("title", "content", 1).getMessage(), is(message.BOARD_SUCCESS_WRITE));
		board = boardService.findBoardForIdx(2);
		Assert.assertThat(board, notNullValue());
	}
	
	@Test
	@Transactional
	public void testFindAll(){
		Pageable pageable = new PageRequest(0, 10, Direction.DESC, "idx");
		Page<Board> page = boardService.findAll(pageable);
		List<Board> boards = page.getContent();
		Assert.assertThat(page.getTotalPages(), is(1));
		Assert.assertThat(page.getTotalElements(), is(1L));
		Assert.assertThat(boards.get(0).getIdx(), is(1));
		Assert.assertThat(boards.get(0).getContent(), is("content"));
		Assert.assertThat(boards.get(0).getTitle(), is("title"));
		Assert.assertThat(boards.get(0).getHitCount(), is(0));
		Assert.assertThat(boards.get(0).getUser().getIdx(), is(1));
	}
	
	@Test
	public void test(){
		Board board = boardService.findBoardForIdx(-1);
		Assert.assertThat(board.getTitle(), is(nullValue()));
		board = boardService.findBoardForIdx(0);
		Assert.assertThat(board.getTitle(), is(nullValue()));
		board = boardService.findBoardForIdx(100);
		Assert.assertThat(board.getTitle(), is(nullValue()));
		board = boardService.findBoardForIdx(1);
		Assert.assertThat(board.getTitle(), is("title"));
		Assert.assertThat(board.getContent(), is("content"));
	}
	
	@Test
	public void testUpdate(){
		String newTitle = "ttttt";
		String newContent = "ccccc";
		board = boardService.findBoardForIdx(1);
		Assert.assertThat(board.getRegDate(), is(board.getUpdateDate()));
		Assert.assertThat(board.getTitle(), not(newTitle));
		Assert.assertThat(board.getContent(), not(newContent));
		Assert.assertThat(boardService.update(1, newTitle, newContent), is(true));
		board = boardService.findBoardForIdx(1);
		Assert.assertThat(board.getRegDate(), not(board.getUpdateDate()));
		Assert.assertThat(board.getTitle(), is(newTitle));
		Assert.assertThat(board.getContent(), is(newContent));
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
		User user = new User();
		user.setIdx(1);
		Assert.assertThat(boardService.getBoardLike(1, user), is(nullValue()));
		Assert.assertThat(boardService.getBoardLike(2, user), is(nullValue()));
		user.setIdx(2);
		Assert.assertThat(boardService.getBoardLike(1, user), is(notNullValue()));
		Assert.assertThat(boardService.getBoardLike(2, user), is(nullValue()));
		user.setIdx(22);
		Assert.assertThat(boardService.getBoardLike(52, user), is(nullValue()));
	}
	
	@Test
	public void testAddBoardLike(){
		User user = new User();
		user.setIdx(0);
		Assert.assertThat(boardService.addBoardLike(0, user), is(false));
		Assert.assertThat(boardService.addBoardLike(1, user), is(false));
		user.setIdx(1);
		Assert.assertThat(boardService.getBoardLike(1, user), is(nullValue()));
		Assert.assertThat(boardService.addBoardLike(0, user), is(false));
		Assert.assertThat(boardService.addBoardLike(121, user), is(false));
		Assert.assertThat(boardService.addBoardLike(1, user), is(true));
		Assert.assertThat(boardService.getBoardLike(1, user), is(notNullValue()));
		user.setIdx(2);
		Assert.assertThat(boardService.addBoardLike(1, user), is(false));
		user.setIdx(350);
		Assert.assertThat(boardService.addBoardLike(66, user), is(false));
		Assert.assertThat(boardService.addBoardLike(1, user), is(false));
	}
	
	@Test
	public void testRemoveBoardLike(){
		User user = new User();
		user.setIdx(2);
		Assert.assertThat(boardService.getBoardLike(1, user), is(notNullValue()));
		user.setIdx(0);
		Assert.assertThat(boardService.removeBoardLike(0, user), is(false));
		Assert.assertThat(boardService.removeBoardLike(1, user), is(false));
		user.setIdx(2);
		Assert.assertThat(boardService.removeBoardLike(0, user), is(false));
		Assert.assertThat(boardService.removeBoardLike(1, user), is(true));
		Assert.assertThat(boardService.getBoardLike(1, user), is(nullValue()));
	}
}
