package kr.co.person.controller;

import java.io.File;
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
import kr.co.person.domain.Board;
import kr.co.person.domain.BoardLike;
import kr.co.person.domain.Comment;
import kr.co.person.domain.User;
import kr.co.person.pojo.BoardLikeCount;
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
		int startPage = pageNum / PAGE_SIZE * PAGE_SIZE + PAGE_SIZE_CONTROL_NUM;
		int lastPage = (pageNum / PAGE_SIZE + PAGE_SIZE_CONTROL_NUM) * PAGE_SIZE;
		Page<Board> pages = boardService.findAll(pageable);
		if(IsValid.isNotValidObjects(pages)){
			return "view/board/frame";
		}
		int maxPage = pages.getTotalPages();
		if(lastPage > maxPage){
			lastPage = maxPage;
		}
		model.addAttribute("boardList", pages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("lastPage", lastPage);
		model.addAttribute("maxPage", maxPage);
		return "view/board/frame";
	}
	
	@RequestMapping(value="/boardWrite", method=RequestMethod.GET)
	public String boardWriteView(Model model){
		log.info("execute BoardController boardWriteView");
		model.addAttribute("include", "main/write.ftl");
		return "view/board/frame";
	}

	@RequestMapping(value="/boardWrite", method=RequestMethod.POST)
	public String boardWrite(@IsValidBoard Board board, @RequestParam MultipartFile editImage, Model model, HttpSession session){
		log.info("execute BoardController boardWrite");
		String title = board.getTitle();
		String content = board.getContent();
		if(StringUtils.isEmpty(title) || StringUtils.isEmpty(title.trim())){
			model.addAttribute("message", message.BOARD_NO_TITLE);
			model.addAttribute("include", "main/write.ftl");
			return "view/board/frame";
		}
		if(StringUtils.isEmpty(content) || StringUtils.isEmpty(content.trim())){
			model.addAttribute("message", message.BOARD_NO_CONTENT);
			model.addAttribute("include", "main/write.ftl");
			return "view/board/frame";
		}
		
		if(editImage.getOriginalFilename().split("\\.").length == 2){
			String imgPath = common.createImg(editImage, ((User)session.getAttribute("user")).getId(), "board");
			String se = File.separator;
			if(se.equals("\\")){
				 se += se;
			}
			String[] paths = imgPath.split(se);
	        String filePath = paths[0];
	        String kindPath = paths[1];
	        String fileName = paths[2];
			content = content.replaceAll("<img src=\"[a-zA-Z0-9!@#$%^&*()`~/\\=+:;,]{0,}\">", "<img src="+filePath+se+kindPath+se+fileName+">");
		}
		OkCheck ok = boardService.write(common.cleanXss(title.trim()), content, ((User)session.getAttribute("user")).getIdx());
		if(!ok.isBool()){
			model.addAttribute("message", ok.getMessage());
			model.addAttribute("include", "main/write.ftl");
			return "view/board/frame";
		}
		return "redirect:/board";
	}
	
	@RequestMapping(value="/boardDetail", method=RequestMethod.GET)
	public String boardDetailView(@RequestParam(required=false, defaultValue="0") int boardNum, @RequestParam(required=false, defaultValue="0") int pageNum, Model model, HttpServletRequest req, HttpServletResponse res, RedirectAttributes rea, HttpSession session){
		log.info("execute BoardController boardDetailView");
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
		int userIdx = ((User)session.getAttribute("user")).getIdx();

		Board board = boardService.findBoardForIdx(boardNum);
		BoardLike boardLike = boardService.getBoardLike(boardNum, userIdx);
		long likeCount = boardService.getBoardLikeCount(boardNum);
		if(IsValid.isNotValidObjects(board) || likeCount < 0){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		String like = (IsValid.isNotValidObjects(boardLike))? message.BOARD_LIKE:message.BOARD_LIKE_CANCLE;
		Page<Comment> comments = commentService.findAllCommentByBoard(boardNum, pageable);
		if(IsValid.isNotValidObjects(comments)){
			return "view/board/frame";
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
	public String boardUpdate(@IsValidBoard Board board, RedirectAttributes rea){
		log.info("execute BoardController boardUpdate");
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
			return "redirect:/board";
		}
		if(StringUtils.isEmpty(title) || StringUtils.isEmpty(title.trim())){
			rea.addFlashAttribute("message", message.BOARD_NO_TITLE);
			return "redirect:/boardUpdateView";
		}
		if(StringUtils.isEmpty(content)){
			rea.addFlashAttribute("message", message.BOARD_NO_CONTENT);
			return "redirect:/boardUpdateView";
		}
		if(!boardService.update(num, common.cleanXss(title.trim()), common.cleanXss(content))){
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
		if(IsValid.isNotValidInts(boardNum)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		String commentSentence = comment.getComment();
		Board board = boardService.findBoardForIdx(boardNum);
		if(IsValid.isNotValidObjects(board)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		if(StringUtils.isEmpty(commentSentence)){
			rea.addFlashAttribute("message", message.COMMENT_RE_COMMENT);
			rea.addAttribute("boardNum", boardNum);
			return "redirect:/boardDetail";
		}
		if(!commentService.write(common.enter(common.cleanXss(commentSentence)), ((User)session.getAttribute("user")).getIdx(), boardNum)){
			rea.addFlashAttribute("message", message.COMMENT_RE_COMMENT);
			rea.addAttribute("boardNum", boardNum);
			return "redirect:/boardDetail";
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
	public String updateComment(@RequestParam(required=false, defaultValue="0") int boardNum, @ModelAttribute("Comment") @Valid Comment comment, RedirectAttributes rea){
		log.info("execute BoardController updateComment");
		if(IsValid.isNotValidInts(boardNum)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		int idx = comment.getIdx();
		String commentSentence = comment.getComment();
		if(IsValid.isNotValidInts(boardNum)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(boardNum);
		if(IsValid.isNotValidObjects(board)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		if(IsValid.isNotValidInts(idx)){
			rea.addFlashAttribute("message", message.COMMENT_NO_COMMENT);
			rea.addAttribute("boardNum", boardNum);
			return "redirect:/boardDetail";
		}
		if(StringUtils.isEmpty(commentSentence) || StringUtils.isEmpty(commentSentence.trim())
				|| !commentService.update(idx, common.enter(common.cleanXss(commentSentence)))){
			rea.addFlashAttribute("message", message.COMMENT_RE_COMMENT);
			rea.addAttribute("boardNum", boardNum);
			return "redirect:/boardDetail";
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
	public String commentReplyWrite(@RequestParam(required=false, defaultValue="0") int boardNum, @ModelAttribute("Comment") @Valid Comment comment, HttpSession session, RedirectAttributes rea){
		log.info("execute BoardController commentReplyWrite");
		if(IsValid.isNotValidInts(boardNum)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		String commentSentence = comment.getComment();
		int idx = comment.getIdx();
		Board board = boardService.findBoardForIdx(boardNum);
		if(IsValid.isNotValidObjects(board)){
			rea.addFlashAttribute("message", message.BOARD_NO_BOARD);
			return "redirect:/board";
		}
		if(IsValid.isNotValidInts(idx)){
			rea.addFlashAttribute("message", message.COMMENT_NO_COMMENT);
			rea.addAttribute("boardNum", boardNum);
			return "redirect:/boardDetail";
		}
		if(StringUtils.isEmpty(commentSentence)){
			rea.addFlashAttribute("message", message.COMMENT_RE_COMMENT);
			rea.addAttribute("boardNum", boardNum);
			return "redirect:/boardDetail";
		}
		if(!commentService.replyWrite(idx, common.enter(common.cleanXss(commentSentence)), ((User)session.getAttribute("user")).getIdx(), boardNum)){
			rea.addFlashAttribute("message", message.COMMENT_NO_REPLY);
			rea.addAttribute("boardNum", boardNum);
			return "redirect:/boardDetail";
		}
		rea.addAttribute("boardNum", boardNum);
		return "redirect:/boardDetail";
	}
	
	@RequestMapping(value="/boardLikeCount", method=RequestMethod.POST)
	public @ResponseBody Map<String, String> addBoardLikeCount(@IsValidBoard BoardLikeCount boardLikeCount){
		log.info("execute BoardController addBoardLikeCount");
		Map<String, String> map = new HashMap<String, String>();
		int boardIdx = boardLikeCount.getBoardIdx();
		int userIdx = boardLikeCount.getUserIdx();
		BoardLike boardLike = boardService.getBoardLike(boardIdx, userIdx);
		int count = boardService.getBoardLikeCount(boardIdx);
		String likeStr = "";
		if(IsValid.isNotValidObjects(boardLike)){
			boardService.addBoardLike(boardIdx, userIdx);
			count += 1;
			likeStr = message.BOARD_LIKE_CANCLE;
		} else {
			boardService.removeBoardLike(boardIdx, userIdx);
			count -= 1;
			likeStr = message.BOARD_LIKE;
		}
		map.put("like", likeStr);
		map.put("likeCount", count + "");
		return map;
	}
}
