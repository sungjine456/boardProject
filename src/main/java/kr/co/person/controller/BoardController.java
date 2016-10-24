package kr.co.person.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.person.annotation.IsValidBoard;
import kr.co.person.common.Common;
import kr.co.person.common.IsValid;
import kr.co.person.common.Message;
import kr.co.person.common.exception.EmptyStringException;
import kr.co.person.domain.Board;
import kr.co.person.domain.BoardLike;
import kr.co.person.domain.Comment;
import kr.co.person.domain.User;
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
	public String main(@RequestParam(required=false, defaultValue="0") int pageNum, Model model, HttpServletRequest req, HttpSession session){
		log.info("execute BoardController main");
		if(pageNum > 0){
			pageNum -= 1;
		}
		User user = (User)session.getAttribute("user");
		log.info(user.getImg());
		Pageable pageable = new CustomPageable(pageNum, BOARD_MAX_COUNT_OF_PAGE, Direction.DESC, "idx");
		int startPage = pageNum / PAGE_SIZE * PAGE_SIZE + PAGE_SIZE_CONTROL_NUM;
		int lastPage = (pageNum / PAGE_SIZE + PAGE_SIZE_CONTROL_NUM) * PAGE_SIZE;
		Page<Board> pages = boardService.findAll(pageable);
		if(IsValid.isNotValidObjects(pages)){
			return "view/frame";
		}
		int maxPage = pages.getTotalPages();
		if(lastPage > maxPage){
			lastPage = maxPage;
		}
		model.addAttribute("boardList", pages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("lastPage", lastPage);
		model.addAttribute("maxPage", maxPage);
		return "view/frame";
	}
	
	@RequestMapping(value="/boardWrite", method=RequestMethod.GET)
	public String boardWriteView(Model model, HttpSession session, RedirectAttributes rea){
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		log.info("execute BoardController boardWriteView");
		model.addAttribute("include", "board/write.ftl");
		return "view/frame";
	}

	@RequestMapping(value="/boardWrite", method=RequestMethod.POST)
	public String boardWrite(@IsValidBoard Board board, @RequestParam MultipartFile editImage, HttpSession session, RedirectAttributes rea){
		log.info("execute BoardController boardWrite");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		String title = board.getTitle();
		String content = board.getContent();
		if(StringUtils.isEmpty(title) || StringUtils.isEmpty(title.trim())){
			rea.addFlashAttribute("message", message.BOARD_NO_TITLE);
			return "redirect:/boardWrite";
		}
		if(StringUtils.isEmpty(content) || StringUtils.isEmpty(content.trim())){
			rea.addFlashAttribute("message", message.BOARD_NO_CONTENT);
			return "redirect:/boardWrite";
		}
		User user = (User)session.getAttribute("user");
		if(IsValid.isNotValidUser(user)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		if(editImage.getOriginalFilename().split("\\.").length == 2){
			String imgPath = "";
			try {
				imgPath = common.createImg(editImage, user.getId(), "board");
			} catch (IOException e) {
				rea.addFlashAttribute("message", message.FILE_FAIL_UPLOAD);
				return "redirect:/boardWrite";
			}
			String se = File.separator;
			if(se.equals("\\")){
				 se += se;
			}
			String[] paths = imgPath.split(se);
	        String filePath = paths[0];
	        String kindPath = paths[1];
	        String fileName = paths[2];
			content = content.replaceAll("<img src=\"[a-zA-Z0-9!@#$%^&*()`~/\\=+:;,]{0,}\">", "<img src=/"+filePath+se+kindPath+se+fileName+">");
		}
		try {
			title = common.cleanXss(title.trim());
		} catch(EmptyStringException e) {
			rea.addFlashAttribute("message", message.BOARD_NO_TITLE);
			return "redirect:/boardWrite";
		}
		OkCheck ok = boardService.write(title, content, user.getIdx());
		if(!ok.isBool()){
			rea.addFlashAttribute("message", ok.getMessage());
			return "redirect:/boardWrite";
		}
		return "redirect:/board";
	}
	
	@RequestMapping(value="/boardDetail", method=RequestMethod.GET)
	public String boardDetailView(@RequestParam(required=false, defaultValue="0") int boardNum, @RequestParam(required=false, defaultValue="0") int pageNum, Model model, HttpServletRequest req, HttpServletResponse res, RedirectAttributes rea, HttpSession session){
		log.info("execute BoardController boardDetailView");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		if(IsValid.isNotValidInts(boardNum)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		if(pageNum > 0){
			pageNum -= 1;
		}
		Pageable pageable = new CustomPageable(pageNum, COMMENT_MAX_COUNT_OF_PAGE, new Sort(
			    new Sort.Order(Direction.DESC, "circle"),
			    new Sort.Order(Direction.ASC, "step")));
		int startPage = pageNum / PAGE_SIZE * PAGE_SIZE + PAGE_SIZE_CONTROL_NUM;
		int lastPage = (pageNum / PAGE_SIZE + PAGE_SIZE_CONTROL_NUM) * PAGE_SIZE;

		User user = (User)session.getAttribute("user");
		if(IsValid.isNotValidUser(user)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		Board board = boardService.findBoardForIdx(boardNum);
		BoardLike boardLike = boardService.getBoardLike(boardNum, user);
		long likeCount = boardService.getBoardLikeCount(boardNum);
		if(IsValid.isNotValidBoard(board) || likeCount < 0){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		String like = (IsValid.isNotValidObjects(boardLike))? message.BOARD_LIKE:message.BOARD_LIKE_CANCLE;
		Page<Comment> comments = commentService.findAllCommentByBoard(boardNum, pageable);
		if(IsValid.isNotValidObjects(comments)){
			return "view/frame";
		}
		int maxPage = comments.getTotalPages();
		if(lastPage > maxPage){
			lastPage = maxPage;
		}
		if(maxPage == 0){
			lastPage = 1;
		}
		model.addAttribute("comments", comments);
		model.addAttribute("startPage", startPage);
		model.addAttribute("lastPage", lastPage);
		model.addAttribute("maxPage", maxPage);
		model.addAttribute("include", "board/boardDetail.ftl");
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
						if(value == boardNum){
							addHit = false;
						}
					}
					if(addHit){
						boardService.addHitCount(boardNum);
						res.addCookie(common.addCookie("pht", val + boardNum + " "));
						addHit = false;
					}
				}
			}
			if(addHit){
			    res.addCookie(common.addCookie("pht", boardNum + " "));
			    boardService.addHitCount(boardNum);
			}
		} else {
			res.addCookie(common.addCookie("pht", boardNum + " "));
		    boardService.addHitCount(boardNum);
		}
		return "view/frame";
	}
	
	@RequestMapping(value="/boardUpdateView", method=RequestMethod.GET)
	public String boardUpdateView(@RequestParam(required=false, defaultValue="0") int boardNum, Model model, RedirectAttributes rea, HttpSession session){
		log.info("execute BoardController boardUpdateView");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		if(IsValid.isNotValidInts(boardNum)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(boardNum);
		if(IsValid.isNotValidBoard(board)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		model.addAttribute("include", "board/update.ftl");
		model.addAttribute("board", board);
		model.addAttribute("num", boardNum);
		return "view/frame";
	}
	
	@RequestMapping(value="/boardUpdate", method=RequestMethod.POST)
	public String boardUpdate(@IsValidBoard Board board, RedirectAttributes rea, HttpSession session){
		log.info("execute BoardController boardUpdate");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		int num = board.getIdx();
		String title = board.getTitle();
		String content = board.getContent();
		if(IsValid.isNotValidInts(num)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		Board findBoard = boardService.findBoardForIdx(num);
		if(IsValid.isNotValidBoard(findBoard)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		if(StringUtils.isEmpty(title) || StringUtils.isEmpty(title.trim())){
			rea.addFlashAttribute("message", message.BOARD_NO_TITLE);
			return "redirect:/boardUpdateView";
		}
		try {
			title = common.cleanXss(title.trim());
		} catch(EmptyStringException e) {
			rea.addFlashAttribute("message", message.BOARD_NO_TITLE);
			rea.addAttribute("num", num);
			return "redirect:/boardUpdateView";
		}
		try {
			content = common.cleanXss(content);
		} catch(EmptyStringException e) {
			rea.addFlashAttribute("message", message.BOARD_NO_CONTENT);
			rea.addAttribute("num", num);
			return "redirect:/boardUpdateView";
		}
		if(!boardService.update(num, title, content)){
			rea.addFlashAttribute("message", message.BOARD_FAIL_UPDATE);
			rea.addAttribute("num", num);
			return "redirect:/boardUpdateView";
		}
		rea.addFlashAttribute("message", message.BOARD_SUCCESS_UPDATE);
		rea.addAttribute("boardNum", num);
		return "redirect:/boardDetail";
	}
	
	@RequestMapping(value="/writeComment", method=RequestMethod.POST)
	public String writeComment(@RequestParam(required=false, defaultValue="0") int boardNum, @ModelAttribute("Comment") @Valid Comment comment, HttpSession session, RedirectAttributes rea){
		log.info("execute BoardController writeComment");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		if(IsValid.isNotValidInts(boardNum)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		String commentSentence = comment.getComment();
		Board board = boardService.findBoardForIdx(boardNum);
		User user = (User)session.getAttribute("user");
		if(IsValid.isNotValidBoard(board)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		if(IsValid.isNotValidUser(user)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		try {
			if(!commentService.write(common.enter(common.cleanXss(commentSentence)), user.getIdx(), boardNum)){
				rea.addFlashAttribute("message", message.COMMENT_RE_COMMENT);
			}
		} catch(EmptyStringException e) {
			rea.addFlashAttribute("message", message.COMMENT_RE_COMMENT);
		}
		rea.addAttribute("boardNum", boardNum);
		return "redirect:/boardDetail";
	}
	
	@RequestMapping(value="/updateCommentView", method=RequestMethod.POST)
	public String updateCommentView(@RequestParam(required=false, defaultValue="0") int boardNum, @ModelAttribute("Comment") @Valid Comment comment, Model model){
		log.info("execute BoardController updateCommentView");
		model.addAttribute("boardNum", boardNum);
		model.addAttribute("comment", comment.getComment());
		model.addAttribute("idx", comment.getIdx());
		return "view/board/ajax/commentUpdate";
	}
	
	@RequestMapping(value="/updateComment", method=RequestMethod.POST)
	public String updateComment(@RequestParam(required=false, defaultValue="0") int boardNum, @ModelAttribute("Comment") @Valid Comment comment, RedirectAttributes rea, HttpSession session){
		log.info("execute BoardController updateComment");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		if(IsValid.isNotValidInts(boardNum)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		int idx = comment.getIdx();
		String commentSentence = comment.getComment();
		Board board = boardService.findBoardForIdx(boardNum);
		if(IsValid.isNotValidBoard(board)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		try {
			if(IsValid.isNotValidInts(idx) || StringUtils.isEmpty(common.cleanXss(commentSentence)) || StringUtils.isEmpty(commentSentence.trim())
					|| !commentService.update(idx, common.enter(commentSentence))){
				rea.addFlashAttribute("message", message.COMMENT_RE_COMMENT);
			}
		} catch(EmptyStringException e) {
			rea.addFlashAttribute("message", message.COMMENT_RE_COMMENT);
		}
		rea.addAttribute("boardNum", boardNum);
		return "redirect:/boardDetail";
	}
	
	@RequestMapping(value="/replyView", method=RequestMethod.POST)
	public String commentReplyView(@RequestParam(required=false, defaultValue="0") int boardNum, @RequestParam(required=false, defaultValue="0") int commentIdx, Model model){
		log.info("execute BoardController commentReplyView");
		model.addAttribute("boardNum", boardNum);
		model.addAttribute("commentIdx", commentIdx);
		return "view/board/ajax/commentReply";
	}
	
	@RequestMapping(value="/writeReply", method=RequestMethod.POST)
	public String commentReplyWrite(@RequestParam(required=false, defaultValue="0") int boardNum, @ModelAttribute("Comment") @Valid Comment comment, Model model, HttpSession session, RedirectAttributes rea){
		log.info("execute BoardController commentReplyWrite");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		if(IsValid.isNotValidInts(boardNum)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		String commentSentence = comment.getComment();
		int idx = comment.getIdx();
		Board board = boardService.findBoardForIdx(boardNum);
		User user = (User)session.getAttribute("user");
		if(IsValid.isNotValidBoard(board)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		if(IsValid.isNotValidInts(idx) || StringUtils.isEmpty(commentSentence)){
			rea.addFlashAttribute("message", message.COMMENT_RE_COMMENT);
			rea.addAttribute("boardNum", boardNum);
			return "redirect:/boardDetail";
		}
		if(IsValid.isNotValidUser(user)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		try {
			if(!commentService.replyWrite(idx, common.enter(common.cleanXss(commentSentence)), user.getIdx(), boardNum)){
				rea.addFlashAttribute("message", message.COMMENT_NO_REPLY);
				rea.addAttribute("boardNum", boardNum);
				return "redirect:/boardDetail";
			}
		} catch(EmptyStringException e) {
			rea.addFlashAttribute("message", message.COMMENT_NO_REPLY);
		}
		rea.addAttribute("boardNum", boardNum);
		return "redirect:/boardDetail";
	}
	
	@RequestMapping(value="/boardLikeCount", method=RequestMethod.POST)
	public @ResponseBody Map<String, String> addBoardLikeCount(@RequestParam(required=false, defaultValue="0") int boardIdx, HttpSession session){
		log.info("execute BoardController addBoardLikeCount");
		Map<String, String> map = new HashMap<String, String>();
		User user = (User)session.getAttribute("user");
		BoardLike boardLike = boardService.getBoardLike(boardIdx, user);
		int count = boardService.getBoardLikeCount(boardIdx);
		String likeStr = "";
		if(IsValid.isNotValidObjects(boardLike)){
			boardService.addBoardLike(boardIdx, user);
			count += 1;
			likeStr = message.BOARD_LIKE_CANCLE;
		} else {
			boardService.removeBoardLike(boardIdx, user);
			count -= 1;
			likeStr = message.BOARD_LIKE;
		}
		map.put("like", likeStr);
		map.put("likeCount", count + "");
		return map;
	}
}
