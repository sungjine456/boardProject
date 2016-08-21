package kr.co.person.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.person.common.Common;
import kr.co.person.common.IsValid;
import kr.co.person.common.Message;
import kr.co.person.domain.Board;
import kr.co.person.domain.BoardLike;
import kr.co.person.domain.Comment;
import kr.co.person.pojo.BoardLikeCount;
import kr.co.person.pojo.CommentNum;
import kr.co.person.pojo.CustomPageable;
import kr.co.person.pojo.OkCheck;
import kr.co.person.service.BoardService;
import kr.co.person.service.CommentService;

@Controller
public class BoardController {
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired private BoardService boardService;
	@Autowired private CommentService commentService;
	@Autowired private Common common;
	@Autowired private Message message;
	private final int PAGE_SIZE = 5;
	private final int PAGE_SIZE_CONTROL_NUM = 1;
	private final int BOARD_MAX_COUNT_OF_PAGE = 10;
	private final int COMMENT_MAX_COUNT_OF_PAGE = 20;
	
	@RequestMapping(value="/board", method=RequestMethod.GET)
	public String main(@RequestParam(required=false, defaultValue="0") int pageNum, Model model, HttpServletRequest req){
		log.info("execute BoardController main");
		if(pageNum > 0){
			pageNum -= 1;
		}
		Pageable pageable = new CustomPageable(pageNum, BOARD_MAX_COUNT_OF_PAGE, Direction.DESC, "idx");
		int startNum = pageNum / PAGE_SIZE * PAGE_SIZE + PAGE_SIZE_CONTROL_NUM;
		int lastNum = (pageNum / PAGE_SIZE + PAGE_SIZE_CONTROL_NUM) * PAGE_SIZE;
		Page<Board> pages = boardService.findAll(pageable);
		if(IsValid.isNotValidObjects(pages)){
			return "view/user/login";
		}
		int lastPage = pages.getTotalPages();
		if(lastNum > lastPage){
			lastNum = lastPage;
		}
		model.addAttribute("boardList", pages);
		model.addAttribute("startNum", startNum);
		model.addAttribute("lastNum", lastNum);
		model.addAttribute("lastPage", lastPage);
		return "view/board/frame";
	}
	
	@RequestMapping(value="/boardWrite", method=RequestMethod.GET)
	public String boardWriteView(Model model){
		log.info("execute BoardController boardWriteView");
		model.addAttribute("include", "main/write.ftl");
		return "view/board/frame";
	}

	@RequestMapping(value="/boardWrite", method=RequestMethod.POST)
	public String boardWrite(Board board, Model model, HttpSession session){
		log.info("execute BoardController boardWrite");
		if(IsValid.isNotValidObjects(board)){
			model.addAttribute("message", message.BOARD_WRONG_BOARD);
			model.addAttribute("include", "main/write.ftl");
			return "view/board/frame";
		}
		String title = board.getTitle();
		String content = board.getContent();
		if(StringUtils.isEmpty(title)){
			model.addAttribute("message", message.BOARD_NO_TITLE);
			model.addAttribute("include", "main/write.ftl");
			return "view/board/frame";
		}
		if(StringUtils.isEmpty(content)){
			model.addAttribute("message", message.BOARD_NO_CONTENT);
			model.addAttribute("include", "main/write.ftl");
			return "view/board/frame";
		}
		OkCheck ok = boardService.write(common.cleanXss(title), common.enter(common.cleanXss(content)), (int)session.getAttribute("idx"));
		if(!ok.isBool()){
			model.addAttribute("message", ok.getMessage());
			model.addAttribute("include", "main/write.ftl");
			return "view/board/frame";
		}
		return "redirect:/board";
	}
	
	@RequestMapping(value="/boardDetail", method=RequestMethod.GET)
	public String boardDetailView(@RequestParam(required=false, defaultValue="0") int num, @RequestParam(required=false, defaultValue="0") int pageNum, Model model, HttpServletRequest req, HttpServletResponse res, RedirectAttributes rea, HttpSession session){
		log.info("execute BoardController boardDetailView");
		if(IsValid.isNotValidInts(num)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		if(pageNum > 0){
			pageNum -= 1;
		}
		Pageable pageable = new CustomPageable(pageNum, COMMENT_MAX_COUNT_OF_PAGE, new Sort(
			    new Sort.Order(Direction.DESC, "circle"),
			    new Sort.Order(Direction.ASC, "step")));
		int startNum = pageNum / PAGE_SIZE * PAGE_SIZE + PAGE_SIZE_CONTROL_NUM;
		int lastNum = (pageNum / PAGE_SIZE + PAGE_SIZE_CONTROL_NUM) * PAGE_SIZE;
		int userIdx = (int)session.getAttribute("idx");

		Board board = boardService.findBoardForIdx(num);
		BoardLike boardLike = boardService.getBoardLike(num, userIdx);
		long likeCount = boardService.getBoardLikeCount(num);
		if(IsValid.isNotValidObjects(board) || likeCount < 0){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		String like = (IsValid.isNotValidObjects(boardLike))? message.BOARD_LIKE:message.BOARD_LIKE_CANCLE;
		Page<Comment> comments = commentService.findAllCommentByBoard(num, pageable);
		if(IsValid.isNotValidObjects(comments)){
			return "view/board/frame";
		}
		int lastPage = comments.getTotalPages();
		if(lastNum > lastPage){
			lastNum = lastPage;
		}
		model.addAttribute("comments", comments);
		model.addAttribute("startNum", startNum);
		model.addAttribute("lastNum", lastNum);
		model.addAttribute("lastPage", lastPage);
		model.addAttribute("include", "main/boardDetail.ftl");
		model.addAttribute("board", board);
		model.addAttribute("likeCount", likeCount);
		model.addAttribute("like", like);
		Cookie[] cookies = req.getCookies();
		if(IsValid.isValidArrays(cookies)){
			boolean addHit = true;
			for(int i = 0; i < cookies.length; i++){
				String key = cookies[i].getName();
				String val = cookies[i].getValue();
				if(StringUtils.equals("pht", key)){
					String[] vals = val.split(" ");
					int length = vals.length;
					for(int j = 0; j < length; j++){
						int value = Integer.parseInt(vals[j]);
						if(value == num){
							addHit = false;
						}
					}
					if(addHit){
						boardService.addHitCount(num);
						res.addCookie(common.addCookie("pht", val + num + " "));
						addHit = false;
					}
				}
			}
			if(addHit){
			    res.addCookie(common.addCookie("pht", num + " "));
			    boardService.addHitCount(num);
			}
		} else {
			res.addCookie(common.addCookie("pht", num + " "));
		    boardService.addHitCount(num);
		}
		return "view/board/frame";
	}
	
	@RequestMapping(value="/boardUpdateView")
	public String boardUpdateView(@RequestParam(required=false, defaultValue="0") int num, Model model, RedirectAttributes rea){
		log.info("execute BoardController boardUpdateView");
		if(IsValid.isNotValidInts(num)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(num);
		if(IsValid.isNotValidObjects(board)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		model.addAttribute("include", "main/update.ftl");
		model.addAttribute("board", board);
		model.addAttribute("num", num);
		return "view/board/frame";
	}
	
	@RequestMapping(value="/boardUpdate", method=RequestMethod.POST)
	public String boardUpdate(Board board, RedirectAttributes rea){
		log.info("execute BoardController boardUpdate");
		if(IsValid.isNotValidObjects(board)){
			rea.addFlashAttribute("message", message.BOARD_WRONG_BOARD);
			return "redirect:/boardDetail";
		}
		int num = board.getIdx();
		String title = board.getTitle();
		String content = board.getContent();
		if(IsValid.isNotValidInts(num)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		Board findBoard = boardService.findBoardForIdx(num);
		if(IsValid.isNotValidObjects(findBoard)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/boardDetail";
		}
		if(StringUtils.isEmpty(title)){
			rea.addFlashAttribute("message", message.BOARD_NO_TITLE);
			return "redirect:/boardUpdateView";
		}
		if(StringUtils.isEmpty(content)){
			rea.addFlashAttribute("message", message.BOARD_NO_CONTENT);
			return "redirect:/boardUpdateView";
		}
		if(!boardService.update(num, common.cleanXss(title), common.cleanXss(content))){
			rea.addFlashAttribute("message", message.BOARD_FAIL_UPDATE);
			rea.addAttribute("num", num);
			return "redirect:/boardUpdateView";
		}
		rea.addFlashAttribute("message", message.BOARD_SUCCESS_UPDATE);
		rea.addAttribute("num", num);
		return "redirect:/boardDetail";
	}
	
	@RequestMapping(value="/writeComment", method=RequestMethod.POST)
	public String writeComment(CommentNum CommentNum, HttpSession session, RedirectAttributes rea){
		log.info("execute BoardController writeComment");
		if(IsValid.isNotValidObjects(CommentNum)){
			rea.addFlashAttribute("message", message.COMMENT_WRONG_COMMENT);
			return "redirect:/boardDetail";
		}
		int boardIdx = CommentNum.getNum();
		String commentSentence = CommentNum.getComment();
		if(IsValid.isNotValidInts(boardIdx)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(boardIdx);
		if(IsValid.isNotValidObjects(board)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/boardDetail";
		}
		if(StringUtils.isEmpty(commentSentence)){
			rea.addFlashAttribute("message", message.COMMENT_RE_COMMENT);
			rea.addAttribute("num", boardIdx);
			return "redirect:/boardDetail";
		}
		if(!commentService.write(common.enter(common.cleanXss(commentSentence)), (int)session.getAttribute("idx"), boardIdx)){
			rea.addFlashAttribute("message", message.COMMENT_RE_COMMENT);
			rea.addAttribute("num", boardIdx);
			return "redirect:/boardDetail";
		}
		rea.addAttribute("num", boardIdx);
		return "redirect:/boardDetail";
	}
	
	@RequestMapping(value="/updateCommentView", method=RequestMethod.POST)
	public String updateCommentView(CommentNum commentNum, Model model){
		log.info("execute BoardController updateCommentView");
		model.addAttribute("comment", commentNum.getComment());
		model.addAttribute("num", commentNum.getNum());
		model.addAttribute("idx", commentNum.getIdx());
		return "view/board/ajax/commentUpdate";
	}
	
	@RequestMapping(value="/updateComment", method=RequestMethod.POST)
	public String updateComment(CommentNum commentNum, RedirectAttributes rea){
		log.info("execute BoardController updateComment");
		if(IsValid.isNotValidObjects(commentNum)){
			rea.addFlashAttribute("message", message.COMMENT_WRONG_COMMENT);
			return "redirect:/board";
		}
		int num = commentNum.getNum();
		int idx = commentNum.getIdx();
		String comment = commentNum.getComment();
		if(IsValid.isNotValidInts(num)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(num);
		if(IsValid.isNotValidObjects(board)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		if(IsValid.isNotValidInts(idx)){
			rea.addAttribute("num", num);
			return "redirect:/boardDetail";
		}
		if(StringUtils.isEmpty(comment)){
			rea.addAttribute("num", num);
			return "redirect:/boardDetail";
		}
		if(!commentService.update(idx, common.enter(common.cleanXss(comment)))){
			rea.addAttribute("num", num);
			return "redirect:/boardDetail";
		}
		rea.addAttribute("num", num);
		return "redirect:/boardDetail";
	}
	
	@RequestMapping(value="replyView", method=RequestMethod.POST)
	public String commentReplyView(CommentNum commentNum, Model model){
		log.info("execute BoardController commentReplyView");
		model.addAttribute("num", commentNum.getNum());
		model.addAttribute("idx", commentNum.getIdx());
		return "view/board/ajax/commentReply";
	}
	
	@RequestMapping(value="writeReply", method=RequestMethod.POST)
	public String commentReplyWrite(CommentNum commentNum, HttpSession session, RedirectAttributes rea){
		log.info("execute BoardController commentReplyWrite");
		if(IsValid.isNotValidObjects(commentNum)){
			rea.addFlashAttribute("message", message.COMMENT_WRONG_COMMENT);
			return "redirect:/board";
		}
		int bnum = commentNum.getNum();
		int idx = commentNum.getIdx();
		String comment = commentNum.getComment();
		if(IsValid.isNotValidInts(bnum)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(bnum);
		if(IsValid.isNotValidObjects(board)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		if(IsValid.isNotValidInts(idx)){
			rea.addFlashAttribute("message", message.COMMENT_NO_COMMENT);
			rea.addAttribute("num", bnum);
			return "redirect:/boardDetail";
		}
		if(StringUtils.isEmpty(comment)){
			rea.addFlashAttribute("message", message.COMMENT_RE_COMMENT);
			rea.addAttribute("num", bnum);
			return "redirect:/boardDetail";
		}
		if(!commentService.replyWrite(idx, common.enter(common.cleanXss(comment)), (int)session.getAttribute("idx"), bnum)){
			rea.addFlashAttribute("message", message.COMMENT_NO_REPLY);
			rea.addAttribute("num", bnum);
			return "redirect:/boardDetail";
		}
		rea.addAttribute("num", bnum);
		return "redirect:/boardDetail";
	}
	
	@RequestMapping(value="boardLikeCount", method=RequestMethod.POST)
	public @ResponseBody Map<String, String> addBoardLikeCount(BoardLikeCount boardLikeCount){
		log.info("execute BoardController addBoardLikeCount");
		Map<String, String> map = new HashMap<String, String>();
		int boardIdx = boardLikeCount.getBoardIdx();
		int userIdx = boardLikeCount.getUserIdx();
		BoardLike boardLike = boardService.getBoardLike(boardIdx, userIdx);
		int count = boardService.getBoardLikeCount(boardIdx);
		String likeStr = "";
		if(IsValid.isNotValidObjects(boardLike)){
			boardService.addBoardLike(boardIdx, userIdx);
			likeStr = message.BOARD_LIKE_CANCLE;
		} else {
			boardService.removeBoardLike(boardIdx, userIdx);
			likeStr = message.BOARD_LIKE;
		}
		map.put("like", likeStr);
		map.put("likeCount", count + "");
		return map;
	}
}
