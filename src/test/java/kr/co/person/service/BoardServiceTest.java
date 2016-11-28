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
import kr.co.person.domain.Board;
import kr.co.person.domain.User;
import kr.co.person.pojo.OkObjectCheck;
import kr.co.person.repository.BoardRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class BoardServiceTest {

	@Autowired private BoardService boardService;
	@Autowired private BoardRepository boardRepository;
	
	@Test
	public void testWrite() {
		Board board = boardRepository.findOne(2);
		Assert.assertThat(board, is(nullValue()));
		Assert.assertThat(boardService.write("title", "content", 1), is(true));
		board = boardRepository.findOne(2);
		Assert.assertThat(board, is(notNullValue()));
		Assert.assertThat(board.getTitle(), is("title"));
		Assert.assertThat(board.getContent(), is("content"));
		Assert.assertThat(board.getHitCount(), is(0));
	}
	
	@Test
	@Transactional
	public void testFindAll(){
		Pageable pageable = new PageRequest(1000, 10, Direction.DESC, "idx");
		Page<Board> page = boardService.findAll(pageable);
		List<Board> boards = page.getContent();
		Assert.assertThat(page, notNullValue());
		Assert.assertThat(page.getTotalPages(), is(1));
		Assert.assertThat(page.getTotalElements(), is(1L));
		Assert.assertThat(boards.size(), is(0));
		pageable = new PageRequest(0, 10, Direction.DESC, "idx");
		page = boardService.findAll(pageable);
		boards = page.getContent();
		Assert.assertThat(page.getTotalPages(), is(1));
		Assert.assertThat(page.getTotalElements(), is(1L));
		Assert.assertThat(boards.get(0).getIdx(), is(1));
		Assert.assertThat(boards.get(0).getContent(), is("content"));
		Assert.assertThat(boards.get(0).getTitle(), is("title"));
		Assert.assertThat(boards.get(0).getHitCount(), is(0));
		Assert.assertThat(boards.get(0).getUser().getIdx(), is(1));
	}
	
	@Test
	public void testFindBoardForIdx(){
		OkObjectCheck<Board> boardCheck = boardService.findBoardForIdx(-1);
		Assert.assertThat(boardCheck.isBool(), is(false));
		boardCheck = boardService.findBoardForIdx(0);
		Assert.assertThat(boardCheck.isBool(), is(false));
		boardCheck = boardService.findBoardForIdx(100);
		Assert.assertThat(boardCheck.isBool(), is(false));
		boardCheck = boardService.findBoardForIdx(1);
		Assert.assertThat(boardCheck.isBool(), is(true));
		Assert.assertThat(boardCheck.getObject().getTitle(), is("title"));
		Assert.assertThat(boardCheck.getObject().getContent(), is("content"));
	}
	
	@Test
	public void testIsNotBoardForIdx(){
		Assert.assertThat(boardService.isNotBoardForIdx(1), is(false));
		Assert.assertThat(boardService.isNotBoardForIdx(100), is(true));
	}
	
	@Test
	public void testUpdate(){
		String newTitle = "ttttt";
		String newContent = "ccccc";
		Board findBoard = boardRepository.findOne(1);
		Assert.assertThat(findBoard.getRegDate(), is(findBoard.getUpdateDate()));
		Assert.assertThat(findBoard.getTitle(), not(newTitle));
		Assert.assertThat(findBoard.getContent(), not(newContent));
		Assert.assertThat(boardService.update(1, newTitle, newContent), is(true));
		findBoard = boardRepository.findOne(1);
		Assert.assertThat(findBoard.getRegDate(), not(findBoard.getUpdateDate()));
		Assert.assertThat(findBoard.getTitle(), is(newTitle));
		Assert.assertThat(findBoard.getContent(), is(newContent));
	}
	
	@Test
	public void testAddHitCount(){
		Board board = boardRepository.findOne(1);
		Assert.assertThat(board.getHitCount(), is(0));
		boardService.addHitCount(1);
		board = boardRepository.findOne(1);
		Assert.assertThat(board.getHitCount(), is(1));
		boardService.addHitCount(1);
		board = boardRepository.findOne(1);
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
