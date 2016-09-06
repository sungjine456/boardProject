package kr.co.person.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import javax.servlet.http.Cookie;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import kr.co.person.BoardProjectApplication;
import kr.co.person.common.Message;
import kr.co.person.service.BoardService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardProjectApplication.class)
@WebAppConfiguration
@Sql(scripts="classpath:/testDataSql/testData.sql")
public class BoardControllerTest {

	@Mock private BoardService boardService;
	@Autowired private Message message;
	@InjectMocks private BoardController boardController;
	@Autowired private WebApplicationContext wac;
	private MockMvc mock;
	private MockHttpSession mockSession;
	
    @Before
    public void setUp() throws Exception {
    	MockitoAnnotations.initMocks(this);
    	mock = MockMvcBuilders.webAppContextSetup(wac).build();
    	mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
    	mockSession.setAttribute("loginYn", "Y");
    	mockSession.setAttribute("idx", 1);
    	mockSession.setAttribute("id", "sungjin");
    	mockSession.setAttribute("img", "defaul.png");
    	mockSession.setAttribute("name", "hong");
    	mockSession.setAttribute("email", "sungjin@naver.com");
    }

    @Test
    public void testMain() throws Exception {
    	mock.perform(
    		get("/board")
    			.session(mockSession))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/board/frame"))
    		.andExpect(model().attributeExists("boardList"))
    		.andExpect(model().attributeExists("startPage"))
    		.andExpect(model().attributeExists("lastPage"))
    		.andExpect(model().attributeExists("maxPage"));
    }
    @Test
    public void testBoardWriteView() throws Exception {
    	mock.perform(
    		get("/boardWrite")
    			.session(mockSession))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/board/frame"))
    		.andExpect(model().attribute("include", "main/write.ftl"));
    }
    
    @Test
    public void testBoardWrite() throws Exception {
    	MockMultipartFile isFile = new MockMultipartFile("editImage", "none.png", null, "bar".getBytes());
    	MockMultipartFile isNotFile = new MockMultipartFile("editImage", null, null, "bar".getBytes());
    	
    	mock.perform(
    		fileUpload("/boardWrite")
    			.session(mockSession))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/board/frame"))
    		.andExpect(model().attribute("message", message.FILE_FAIL_UPLOAD))
    		.andExpect(model().attribute("include", "main/write.ftl"));
    	
    	mock.perform(
    		fileUpload("/boardWrite")
    			.file(isNotFile)
    			.session(mockSession))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/board/frame"))
    		.andExpect(model().attribute("message", message.FILE_FAIL_UPLOAD))
    		.andExpect(model().attribute("include", "main/write.ftl"));
    	
    	mock.perform(
    		fileUpload("/boardWrite")
    			.file(isFile)
    			.session(mockSession))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/board/frame"))
    		.andExpect(model().attribute("message", message.BOARD_NO_TITLE))
    		.andExpect(model().attribute("include", "main/write.ftl"));
    	
    	mock.perform(
    		fileUpload("/boardWrite")
    			.file(isFile)
    			.param("title", "    ")
    			.session(mockSession))
	    	.andExpect(status().isOk())
	    	.andExpect(view().name("view/board/frame"))
	    	.andExpect(model().attribute("message", message.BOARD_NO_TITLE))
	    	.andExpect(model().attribute("include", "main/write.ftl"));
    	
    	mock.perform(
    		fileUpload("/boardWrite")
    			.file(isFile)
    			.param("title", "testTitle")
    			.session(mockSession))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/board/frame"))
    		.andExpect(model().attribute("message", message.BOARD_NO_CONTENT))
    		.andExpect(model().attribute("include", "main/write.ftl"));

    	mock.perform(
    		fileUpload("/boardWrite")
    			.file(isFile)
    			.param("title", "testTitle")
    			.param("content", "testContent")
    			.session(mockSession))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testBoardDetail() throws Exception{
    	mock.perform(
    		get("/boardDetail")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/board"))
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD));
    	
    	mock.perform(
    		get("/boardDetail")
    			.session(mockSession)
    			.param("boardNum", "0"))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/board"))
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD));
    	
    	MvcResult result = mock.perform(
    		get("/boardDetail")
    			.session(mockSession)
    			.param("boardNum", "1"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/board/frame"))
    		.andExpect(model().attributeExists("comments", "board"))
    		.andExpect(model().attribute("startPage", 1))
    		.andExpect(model().attribute("lastPage", 1))
    		.andExpect(model().attribute("maxPage", 1))
    		.andExpect(model().attribute("include", "main/boardDetail.ftl"))
    		.andExpect(model().attribute("likeCount", 1L))
    		.andExpect(model().attribute("like", "좋아요"))
    		.andReturn();
    	
    	Cookie[] cookies = result.getResponse().getCookies();
    	Assert.assertThat(cookies, notNullValue());
    	Assert.assertThat("pht", is(cookies[0].getName()));
    	Assert.assertThat("1 ", is(cookies[0].getValue()));
    }
    
    @Test
    public void testBoardUpdateView() throws Exception {
    	mock.perform(
    		get("/boardUpdateView")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
    		.andExpect(redirectedUrl("/board"));

    	mock.perform(
    		get("/boardUpdateView")
    			.session(mockSession)
    			.param("num", "6"))
	    	.andExpect(status().isFound())
	    	.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
	    	.andExpect(redirectedUrl("/board"));

    	mock.perform(
    		get("/boardUpdateView")
    			.session(mockSession)
    			.param("num", "1"))
	    	.andExpect(status().isOk())
	    	.andExpect(model().attributeExists("include"))
	    	.andExpect(model().attribute("include", "main/update.ftl"))
	    	.andExpect(model().attribute("num", 1))
	    	.andExpect(view().name("view/board/frame"));
    }
    
    @Test
    public void testBoardUpdate() throws Exception {
    	mock.perform(
    		post("/boardUpdate")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
    		.andExpect(redirectedUrl("/board"));
    	
    	mock.perform(
    		post("/boardUpdate")
    			.param("idx", "1")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_TITLE))
    		.andExpect(redirectedUrl("/boardUpdateView"));
    	
    	mock.perform(
    		post("/boardUpdate")
	    		.param("idx", "1")
	    		.param("title", "testTitle")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_CONTENT))
    		.andExpect(redirectedUrl("/boardUpdateView"));
    	
    	mock.perform(
    		post("/boardUpdate")
        		.param("idx", "1")
	    		.param("title", "    ")
	    		.param("content", "testContent")
    			.session(mockSession))
	  		.andExpect(status().isFound())
			.andExpect(flash().attribute("message", message.BOARD_NO_TITLE))
			.andExpect(redirectedUrl("/boardUpdateView"));

    	mock.perform(
    		post("/boardUpdate")
    			.param("idx", "1")
    			.param("title", "testTitle")
    			.param("content", "testContent")
    			.session(mockSession))
			.andExpect(status().isFound())
			.andExpect(flash().attribute("message", message.BOARD_SUCCESS_UPDATE))
			.andExpect(model().attribute("boardNum", "1"))
			.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    }
    
    @Test
    public void testWriteComment() throws Exception {
    	mock.perform(
    		post("/writeComment")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
    		.andExpect(redirectedUrl("/board"));
    	
    	mock.perform(
    		post("/writeComment")
    			.param("boardNum", "8")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
    		.andExpect(redirectedUrl("/board"));

    	mock.perform(
			post("/writeComment")
    			.param("boardNum", "1")
    			.session(mockSession))
	    	.andExpect(status().isFound())
	    	.andExpect(flash().attribute("message", message.COMMENT_RE_COMMENT))
	    	.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    	
    	mock.perform(
			post("/writeComment")
    			.param("boardNum", "1")
    			.param("content", "test")
    			.session(mockSession))
	    	.andExpect(status().isFound())
	    	.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    }
    
    @Test
    public void testUpdateCommentView() throws Exception {
    	mock.perform(
    		post("/updateCommentView")
    			.param("boardNum", "1")
    			.param("comment", "testComment")
    			.param("idx", "1")
    			.session(mockSession))
    		.andExpect(status().isOk())
    		.andExpect(model().attribute("boardNum", 1))
    		.andExpect(model().attribute("comment", "testComment"))
    		.andExpect(model().attribute("idx", 1))
    		.andExpect(view().name("view/board/ajax/commentUpdate"));
    }
    
    @Test
    public void testUpdateComment() throws Exception {
    	mock.perform(
    		post("/updateComment")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
    		.andExpect(redirectedUrl("/board"));
    	
    	mock.perform(
    		post("/updateComment")
    			.param("boardNum", "6")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
    		.andExpect(redirectedUrl("/board"));
    	
    	mock.perform(
    		post("/updateComment")
    			.param("boardNum", "1")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.COMMENT_NO_COMMENT))
    		.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    	
    	mock.perform(
    		post("/updateComment")
    			.param("boardNum", "1")
    			.param("idx", "1")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.COMMENT_RE_COMMENT))
    		.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    	
    	mock.perform(
			post("/writeComment")
				.param("boardNum", "1")
				.param("idx", "1")
    			.session(mockSession))
	    	.andExpect(status().isFound())
	    	.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    }
    
    @Test
    public void testReplyView() throws Exception {
    	mock.perform(
    		post("/replyView")
    			.param("boardNum", "1")
    			.param("commentIdx", "1")
    			.session(mockSession))
    		.andExpect(status().isOk())
    		.andExpect(model().attribute("boardNum", 1))
    		.andExpect(model().attribute("commentIdx", 1))
    		.andExpect(view().name("view/board/ajax/commentReply"));
    }
    
    @Test
    public void testWriteReply() throws Exception {
    	mock.perform(
    		post("/writeReply")
    			.session(mockSession))
			.andExpect(status().isFound())
			.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
			.andExpect(redirectedUrl("/board"));
    	
    	mock.perform(
    		post("/writeReply")
    			.param("boardNum", "6")
    			.session(mockSession))
			.andExpect(status().isFound())
			.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
			.andExpect(redirectedUrl("/board"));
    	
    	mock.perform(
    		post("/writeReply")
    			.param("boardNum", "1")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.COMMENT_NO_COMMENT))
    		.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    	
    	mock.perform(
    		post("/writeReply")
    			.param("boardNum", "1")
    			.param("idx", "6")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.COMMENT_RE_COMMENT))
    		.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    	
    	mock.perform(
    		post("/writeReply")
    			.param("boardNum", "1")
    			.param("idx", "1")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.COMMENT_RE_COMMENT))
    		.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    	
    	mock.perform(
			post("/writeReply")
				.param("boardNum", "1")
				.param("idx", "1")
				.param("content", "testComment")
    			.session(mockSession))
	    	.andExpect(status().isFound())
	    	.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    }
    
    @Test
    public void testBoardLikeCount() throws Exception {
    	mock.perform(
    		post("/boardLikeCount")
    			.param("boardIdx", "1")
    			.param("userIdx", "1")
    			.session(mockSession)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("like", is("좋아요 취소")))
    		.andExpect(jsonPath("likeCount", is("2")));
    	
    	mock.perform(
    		post("/boardLikeCount")
    			.param("boardIdx", "1")
    			.param("userIdx", "1")
    			.session(mockSession)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("like", is("좋아요")))
    		.andExpect(jsonPath("likeCount", is("1")));
    	
    	mock.perform(
    		post("/boardLikeCount")
    			.param("boardIdx", "1")
    			.param("userIdx", "2")
    			.session(mockSession)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("like", is("좋아요")))
    		.andExpect(jsonPath("likeCount", is("0")));
    }
}
