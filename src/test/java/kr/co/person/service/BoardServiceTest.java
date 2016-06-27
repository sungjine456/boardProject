package kr.co.person.service;

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
import kr.co.person.domain.Board;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class BoardServiceTest {

	@Autowired
	private BoardService boardService;
	private Board board;
	
	@Test
	public void testSave() {
		String message = boardService.write("", "content", 1).getMessage();
		Assert.assertEquals("제목을 입력해주세요.", message);
		message = boardService.write("title", "", 1).getMessage();
		Assert.assertEquals("내용을 입력해주세요.", message);
		message = boardService.write("title", "content", 0).getMessage();
		Assert.assertEquals("유효한 회원이 아닙니다.", message);
		message = boardService.write("title", "content", 1).getMessage();
		Assert.assertEquals("글이 등록 되었습니다.", message);
	}
	
	@Test
	public void testFindAll(){
		List<Board> boardList = boardService.findAll();
		Assert.assertEquals(1, boardList.get(0).getIdx());
		Assert.assertEquals(1, boardList.size());
	}
	
	@Test
	public void testUpdate(){
		board = boardService.findBoardForIdx(1);
		Assert.assertEquals(board.getRegDate(), board.getUpDate());
		Assert.assertFalse(boardService.update(1, "hyun", ""));
		Assert.assertFalse(boardService.update(1, "", "hyun"));
		Assert.assertFalse(boardService.update(5, "hyun", "hyun"));
		Assert.assertTrue(boardService.update(1, "ttttt", "ccccc"));
		board = boardService.findBoardForIdx(1);
		Assert.assertNotEquals(board.getRegDate(), board.getUpDate());
	}
}
