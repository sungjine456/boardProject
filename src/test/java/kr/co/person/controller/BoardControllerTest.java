package kr.co.person.controller;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.net.URLDecoder;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import kr.co.person.BoardProjectApplication;
import kr.co.person.common.Message;
import kr.co.person.domain.Board;
import kr.co.person.domain.User;
import kr.co.person.service.BoardService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoardProjectApplication.class)
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
    	User user = new User();
    	user.setIdx(1);
    	user.setId("sungjin");
    	user.setName("hong");
    	user.setImg("defaul.png");
    	user.setEmail("sungjin@naver.com");
    	user.setPassword("123123");
    	mockSession.setAttribute("user", user);
    }

    @Test
    public void testMainWrongSession0() throws Exception {
    	mock.perform(
    		get("/board")
    			.sessionAttr("loginYn", "Y")
		    	.sessionAttr("idx", 5))
			.andExpect(status().isFound())
			.andExpect(flash().attribute("message", message.USER_NO_LOGIN))
			.andExpect(redirectedUrl("/"));
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void testMainWrongPageNum() throws Exception {
    	MvcResult result = mock.perform(
    		get("/board")
    			.session(mockSession)
    			.param("pageNum", "100"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/frame"))
    		.andExpect(model().attributeExists("boardList"))
    		.andExpect(model().attributeExists("startPage"))
    		.andExpect(model().attributeExists("lastPage"))
    		.andExpect(model().attributeExists("maxPage"))
    		.andExpect(model().attribute("message", message.BOARD_LAST_PAGE_EXCESS))
    		.andReturn();
    	
    	Page<Board> pages = (Page<Board>)result.getRequest().getAttribute("boardList");
    	Assert.assertThat(pages.getContent().size(), is(1));
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void testMainSuccess() throws Exception {
    	MvcResult result = mock.perform(
    		get("/board")
    			.session(mockSession)
    			.param("pageNum", "1"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/frame"))
    		.andExpect(model().attributeExists("boardList"))
    		.andExpect(model().attributeExists("startPage"))
    		.andExpect(model().attributeExists("lastPage"))
    		.andExpect(model().attributeExists("maxPage"))
    		.andReturn();
    	
    	Page<Board> pages = (Page<Board>)result.getRequest().getAttribute("boardList");
    	Assert.assertThat(pages.getContent().size(), is(1));
    }
    
    @Test
    public void testBoardWriteView() throws Exception {
    	mock.perform(
    		get("/boardWrite")
    			.session(mockSession))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/frame"))
    		.andExpect(model().attribute("include", "board/write.ftl"));
    }
    
    @Test
    public void testBoardWriteNoTitle() throws Exception {
    	mock.perform(
    		fileUpload("/boardWrite")
    			.file(new MockMultipartFile("editImage", "none.png", null, "bar".getBytes()))
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_TITLE))
    		.andExpect(redirectedUrl("/boardWrite"));
    }
    
    @Test
    public void testBoardWriteBlankTitle() throws Exception {
    	mock.perform(
    		fileUpload("/boardWrite")
    			.file(new MockMultipartFile("editImage", "none.png", null, "bar".getBytes()))
    			.param("title", "    ")
    			.session(mockSession))
	    	.andExpect(status().isFound())
	    	.andExpect(flash().attribute("message", message.BOARD_NO_TITLE))
	    	.andExpect(redirectedUrl("/boardWrite"));
    }
    
    @Test
    public void testBoardWriteNoContent() throws Exception {
    	mock.perform(
    		fileUpload("/boardWrite")
    			.file(new MockMultipartFile("editImage", "none.png", null, "bar".getBytes()))
    			.param("title", "testTitle")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_CONTENT))
    		.andExpect(redirectedUrl("/boardWrite"));
    }
    
    @Test
    public void testBoardWriteBlankContent() throws Exception {
    	mock.perform(
    		fileUpload("/boardWrite")
    			.file(new MockMultipartFile("editImage", "none.png", null, "bar".getBytes()))
    			.param("title", "testTitle")
    			.param("content", "    ")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_CONTENT))
    		.andExpect(redirectedUrl("/boardWrite"));
    }
    
    @Test
    public void testBoardWriteSuccess() throws Exception {
    	mock.perform(
    		fileUpload("/boardWrite")
    			.file(new MockMultipartFile("editImage", "none.png", null, "bar".getBytes()))
    			.param("title", "testTitle")
    			.param("content", "testContent")
    			.session(mockSession))
	    	.andExpect(status().isFound())
	    	.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testBoardDetailNoBoardIdx() throws Exception{
    	mock.perform(
    		get("/boardDetail")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/board"))
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD));
    }
    
    @Test
    public void testBoardDetailWrongBoardIdx() throws Exception{
    	mock.perform(
    		get("/boardDetail")
    			.session(mockSession)
    			.param("boardNum", "0"))
    		.andExpect(status().isFound())
    		.andExpect(redirectedUrl("/board"))
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD));
    }
    
    @Test
    public void testBoardDetailSuccess() throws Exception{
    	MvcResult result = mock.perform(
    		get("/boardDetail")
    			.session(mockSession)
    			.param("boardNum", "1"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("view/frame"))
    		.andExpect(model().attributeExists("comments", "board"))
    		.andExpect(model().attribute("startPage", 1))
    		.andExpect(model().attribute("lastPage", 1))
    		.andExpect(model().attribute("maxPage", 1))
    		.andExpect(model().attribute("include", "board/boardDetail.ftl"))
    		.andExpect(model().attribute("likeCount", 1L))
    		.andExpect(model().attribute("like", "좋아요"))
    		.andReturn();
    	
    	Cookie[] cookies = result.getResponse().getCookies();
    	Assert.assertThat(cookies, notNullValue());
    	Assert.assertThat("pht", is(cookies[0].getName()));
    	Assert.assertThat("1 ", is(URLDecoder.decode(cookies[0].getValue(), "UTF-8")));
    }
    
    @Test
    public void testBoardUpdateViewNoBoardIdx() throws Exception {
    	mock.perform(
    		get("/boardUpdateView")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
    		.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testBoardUpdateViewWrongBoardIdx() throws Exception {
    	mock.perform(
    		get("/boardUpdateView")
    			.session(mockSession)
    			.param("num", "6"))
	    	.andExpect(status().isFound())
	    	.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
	    	.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testBoardUpdateViewSuccess() throws Exception {
    	mock.perform(
    		get("/boardUpdateView")
    			.session(mockSession)
    			.param("boardNum", "1"))
	    	.andExpect(status().isOk())
	    	.andExpect(model().attributeExists("include"))
	    	.andExpect(model().attribute("include", "board/update.ftl"))
	    	.andExpect(model().attribute("num", 1))
	    	.andExpect(view().name("view/frame"));
    }
    
    @Test
    public void testBoardUpdateNoBoardIdx() throws Exception {
    	mock.perform(
    		post("/boardUpdate")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
    		.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testBoardUpdateNoTitle() throws Exception {
    	mock.perform(
    		post("/boardUpdate")
    			.param("idx", "1")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_TITLE))
    		.andExpect(redirectedUrl("/boardUpdateView?boardNum=1"));
    }
    
    @Test
    public void testBoardUpdateNoContent() throws Exception {
    	mock.perform(
    		post("/boardUpdate")
	    		.param("idx", "1")
	    		.param("title", "testTitle")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_CONTENT))
    		.andExpect(redirectedUrl("/boardUpdateView?boardNum=1"));
    }
    
    @Test
    public void testBoardUpdateBlankTitle() throws Exception {
    	mock.perform(
    		post("/boardUpdate")
        		.param("idx", "1")
	    		.param("title", "    ")
	    		.param("content", "testContent")
    			.session(mockSession))
	  		.andExpect(status().isFound())
			.andExpect(flash().attribute("message", message.BOARD_NO_TITLE))
			.andExpect(redirectedUrl("/boardUpdateView?boardNum=1"));
    }
    
    @Test
    public void testBoardUpdateSuccess() throws Exception {
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
    public void testWriteCommentNoBoardIdx() throws Exception {
    	mock.perform(
    		post("/writeComment")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
    		.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testWriteCommentWrongBoardIdx() throws Exception {
    	mock.perform(
    		post("/writeComment")
    			.param("boardNum", "8")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
    		.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testWriteCommentNoContent() throws Exception {
    	mock.perform(
			post("/writeComment")
    			.param("boardNum", "1")
    			.session(mockSession))
	    	.andExpect(status().isFound())
	    	.andExpect(flash().attribute("message", message.COMMENT_RE_COMMENT))
	    	.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    }
    
    @Test
    public void testWriteCommentSuccess() throws Exception {
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
    public void testUpdateCommentNoBoardIdx() throws Exception {
    	mock.perform(
    		post("/updateComment")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
    		.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testUpdateCommentWrongBoardIdx() throws Exception {
    	mock.perform(
    		post("/updateComment")
    			.param("boardNum", "6")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
    		.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testUpdateCommentNoCommentIdx() throws Exception {
    	mock.perform(
    		post("/updateComment")
    			.param("boardNum", "1")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.COMMENT_RE_COMMENT))
    		.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    }
    
    @Test
    public void testUpdateCommentNoComment() throws Exception {
    	mock.perform(
    		post("/updateComment")
    			.param("boardNum", "1")
    			.param("idx", "1")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.COMMENT_RE_COMMENT))
    		.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    }
    
    @Test
    public void testUpdateCommentSuccess() throws Exception {
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
    public void testWriteReplyNoBoardIdx() throws Exception {
    	mock.perform(
    		post("/writeReply")
    			.session(mockSession))
			.andExpect(status().isFound())
			.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
			.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testWriteReplyWrongBoardIdx() throws Exception {
    	mock.perform(
    		post("/writeReply")
    			.param("boardNum", "6")
    			.session(mockSession))
			.andExpect(status().isFound())
			.andExpect(flash().attribute("message", message.BOARD_NO_BOARD))
			.andExpect(redirectedUrl("/board"));
    }
    
    @Test
    public void testWriteReplyNoCommentIdx() throws Exception {
    	mock.perform(
    		post("/writeReply")
    			.param("boardNum", "1")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.COMMENT_RE_COMMENT))
    		.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    }
    
    @Test
    public void testWriteReplyWrongCommentIdx() throws Exception {
    	mock.perform(
    		post("/writeReply")
    			.param("boardNum", "1")
    			.param("idx", "6")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.COMMENT_RE_COMMENT))
    		.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    }
    
    @Test
    public void testWriteReplyNoComment() throws Exception {
    	mock.perform(
    		post("/writeReply")
    			.param("boardNum", "1")
    			.param("idx", "1")
    			.session(mockSession))
    		.andExpect(status().isFound())
    		.andExpect(flash().attribute("message", message.COMMENT_RE_COMMENT))
    		.andExpect(model().attribute("boardNum", "1"))
	    	.andExpect(redirectedUrl("/boardDetail?boardNum=1"));
    }
    
    @Test
    public void testWriteReplySuccess() throws Exception {
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
    public void testBoardLikeCountLike() throws Exception {
    	mock.perform(
    		post("/boardLikeCount")
    			.param("boardIdx", "1")
    			.session(mockSession)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("like", is("좋아요 취소")))
    		.andExpect(jsonPath("likeCount", is("2")));
    }
    
    @Test
    public void testBoardLikeCountLikeCancel() throws Exception {
    	User user = new User();
    	user.setIdx(2);
    	user.setId("sungjin");
    	user.setName("hong");
    	user.setImg("defaul.png");
    	user.setEmail("sungjin@naver.com");
    	mockSession.setAttribute("user", user);
    	
    	mock.perform(
    		post("/boardLikeCount")
    			.param("boardIdx", "1")
    			.session(mockSession)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("like", is("좋아요")))
    		.andExpect(jsonPath("likeCount", is("0")));
    }
}
