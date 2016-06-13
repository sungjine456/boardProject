package kr.co.person.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import kr.co.person.BoardProjectApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class BoardServiceTest {

	@Autowired
	private BoardService boardService;
	
	@Test
	public void testSave() {
		String message = boardService.save("", "content", 1).getMessage();
		Assert.assertEquals("제목을 입력해주세요.", message);
		message = boardService.save("title", "", 1).getMessage();
		Assert.assertEquals("내용을 입력해주세요.", message);
		message = boardService.save("title", "content", 0).getMessage();
		Assert.assertEquals("유효한 회원이 아닙니다.", message);
		message = boardService.save("title", "content", 1).getMessage();
		Assert.assertEquals("글이 등록 되었습니다.", message);
	}
}
