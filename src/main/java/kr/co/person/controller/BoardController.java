package kr.co.person.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.person.common.Common;
import kr.co.person.common.IsValid;
import kr.co.person.domain.Board;
import kr.co.person.domain.BoardLike;
import kr.co.person.domain.Comment;
import kr.co.person.pojo.BoardLikeCount;
import kr.co.person.pojo.CommentNum;
import kr.co.person.pojo.OkCheck;
import kr.co.person.service.BoardService;
import kr.co.person.service.CommentService;

@Controller
public class BoardController {
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired private BoardService boardService;
	@Autowired private CommentService commentService;
	@Autowired private Common common;
	
	@RequestMapping(value="/board", method=RequestMethod.GET)
	public String main(@RequestParam(required=false) Integer num, Model model, HttpServletRequest req){
		log.info("BoardController main execute");
		log.info("BoardController main execute num : " + num);
		if(IsValid.isNotValidObjects(num)){
			num = 0;
		} else {
			num -= 1;
		}
		Pageable pageable = new PageRequest(num, 10, Direction.DESC, "idx");
		int startNum = num / 5 * 5 + 1;
		int lastNum = (num / 5 + 1) * 5;
		Page<Board> pages = boardService.findAll(pageable);
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
		model.addAttribute("include", "main/write.ftl");
		return "view/board/frame";
	}

	@RequestMapping(value="/boardWrite", method=RequestMethod.POST)
	public String boardWrite(Board board, Model model, HttpSession session){
		log.info("BoardController boardWrite execute");
		if(IsValid.isNotValidObjects(board)){
			model.addAttribute("message", "잘못된 내용입니다.");
			model.addAttribute("include", "main/write.ftl");
			return "view/board/frame";
		}
		String title = board.getTitle();
		String content = board.getContent();
		log.info("BoardController boardWrite title : " + title + ",   content : " + content);
		if(StringUtils.isEmpty(title)){
			model.addAttribute("message", "제목을 입력해주세요.");
			model.addAttribute("include", "main/write.ftl");
			return "view/board/frame";
		}
		if(StringUtils.isEmpty(content)){
			model.addAttribute("message", "내용을 입력해주세요.");
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
	public String boardDetailView(@RequestParam(required=false) Integer num, Model model, HttpServletRequest req, HttpServletResponse res, RedirectAttributes rea, HttpSession session){
		log.info("execute BoardController DetialView");
		if(IsValid.isNotValidObjects(num)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		int userIdx = (int)session.getAttribute("idx");
		Board board = boardService.findBoardForIdx(num);
		BoardLike boardLike = boardService.getBoardLike(num, userIdx);
		long likeCount = boardService.getBoardLikeCount(num);
		if(IsValid.isNotValidObjects(board) || likeCount < 0){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		String like = (IsValid.isNotValidObjects(boardLike))? "좋아요":"좋아요 취소";
		List<Comment> comments = commentService.findAllCommentByBoard(num);
		model.addAttribute("comments", comments);
		model.addAttribute("include", "main/boardDetail.ftl");
		model.addAttribute("board", board);
		model.addAttribute("likeCount", likeCount);
		model.addAttribute("like", like);
		Cookie[] cookies = req.getCookies();
		if(IsValid.isValidArrays(cookies)){
			boolean isHit = true;
			for(int i = 0; i < cookies.length; i++){
				String key = cookies[i].getName();
				String val = cookies[i].getValue();
				if(StringUtils.equals("hit", key)){
					isHit = false;
					boolean bool = true;
					String[] vals = val.split(" ");
					int length = vals.length;
					for(int j = 0; j < length; j++){
						int value = Integer.parseInt(vals[j]);
						if(value == num){
							bool = false;
						}
					}
					if(bool){
						boardService.addHitCount(num);
						res.addCookie(common.addCookie("hit", val + num + " "));
					}
				}
			}
			if(isHit){
			    res.addCookie(common.addCookie("hit", num + " "));
			    boardService.addHitCount(num);
			}
		} else {
			res.addCookie(common.addCookie("hit", num + " "));
		    boardService.addHitCount(num);
		}
		return "view/board/frame";
	}
	
	@RequestMapping(value="/boardUpdateView")
	public String boardUpdateView(@RequestParam(required=false) Integer num, Model model, RedirectAttributes rea){
		log.info("BoardController boardUpdateView execute");
		if(IsValid.isNotValidObjects(num)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(num);
		if(IsValid.isNotValidObjects(board)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/boardDetail";
		}
		model.addAttribute("include", "main/update.ftl");
		model.addAttribute("board", board);
		model.addAttribute("num", num);
		return "view/board/frame";
	}
	
	@RequestMapping(value="/boardUpdate", method=RequestMethod.POST)
	public String boardUpdate(Board board, RedirectAttributes rea){
		log.info("BoardController boardUpdate execute");
		if(IsValid.isNotValidObjects(board)){
			rea.addFlashAttribute("message", "잘못된 내용입니다.");
			return "redirect:/boardDetail";
		}
		int num = board.getIdx();
		String title = board.getTitle();
		String content = board.getContent();
		if(IsValid.isNotValidInts(num)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		Board findBoard = boardService.findBoardForIdx(num);
		if(IsValid.isNotValidObjects(findBoard)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/boardDetail";
		}
		log.info("BoardController boardUpdate title : " + title + ",   content : " + content);
		if(StringUtils.isEmpty(title)){
			rea.addFlashAttribute("message", "제목을 입력해주세요.");
			return "redirect:/boardUpdateView";
		}
		if(StringUtils.isEmpty(content)){
			rea.addFlashAttribute("message", "내용을 입력해주세요.");
			return "redirect:/boardUpdateView";
		}
		if(!boardService.update(num, common.cleanXss(title), common.cleanXss(content))){
			rea.addFlashAttribute("message", "수정에 실패하셨습니다.");
			rea.addAttribute("num", num);
			return "redirect:/boardUpdateView";
		}
		rea.addFlashAttribute("message", "수정에 성공하셨습니다.");
		rea.addAttribute("num", num);
		return "redirect:/boardDetail";
	}
	
	@RequestMapping(value="/writeComment", method=RequestMethod.POST)
	public String writeComment(CommentNum CommentNum, HttpSession session, RedirectAttributes rea){
		log.info("BoardController writeComment execute");
		if(IsValid.isNotValidObjects(CommentNum)){
			rea.addFlashAttribute("message", "잘못된 내용입니다.");
			return "redirect:/boardDetail";
		}
		int num = CommentNum.getNum();
		String commentSentence = CommentNum.getComment();
		log.info("BoardController writeComment execute num : " + num + ", comment : " + commentSentence);
		if(IsValid.isNotValidInts(num)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(num);
		if(IsValid.isNotValidObjects(board)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/boardDetail";
		}
		if(StringUtils.isEmpty(commentSentence)){
			rea.addFlashAttribute("message", "댓글을 다시 작성해주세요.");
			rea.addAttribute("num", num);
			return "redirect:/boardDetail";
		}
		if(!commentService.write(common.enter(common.cleanXss(commentSentence)), (int)session.getAttribute("idx"), num)){
			rea.addFlashAttribute("message", "댓글을 다시 작성해주세요.");
			rea.addAttribute("num", num);
			return "redirect:/boardDetail";
		}
		rea.addAttribute("num", num);
		return "redirect:/boardDetail";
	}
	
	@RequestMapping(value="/updateCommentView", method=RequestMethod.POST)
	public String updateCommentView(CommentNum commentNum, Model model){
		log.info("BoardController updateCommentView execute");
		model.addAttribute("comment", commentNum.getComment());
		model.addAttribute("num", commentNum.getNum());
		model.addAttribute("idx", commentNum.getIdx());
		return "view/board/ajax/commentUpdate";
	}
	
	@RequestMapping(value="/updateComment", method=RequestMethod.POST)
	public String updateComment(CommentNum commentNum, RedirectAttributes rea){
		log.info("BoardController updateComment execute");
		if(IsValid.isNotValidObjects(commentNum)){
			rea.addFlashAttribute("message", "잘못된 내용입니다.");
			return "redirect:/board";
		}
		int num = commentNum.getNum();
		int idx = commentNum.getIdx();
		String comment = commentNum.getComment();
		if(IsValid.isNotValidInts(num)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(num);
		if(IsValid.isNotValidObjects(board)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
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
		log.info("BoardController commentReplyView execute");
		model.addAttribute("num", commentNum.getNum());
		model.addAttribute("idx", commentNum.getIdx());
		return "view/board/ajax/commentReply";
	}
	
	@RequestMapping(value="writeReply", method=RequestMethod.POST)
	public String commentReplyWrite(CommentNum commentNum, HttpSession session, RedirectAttributes rea){
		log.info("BoardController commentReplyWrite execute");
		if(IsValid.isNotValidObjects(commentNum)){
			rea.addFlashAttribute("message", "잘못된 내용입니다.");
			return "redirect:/board";
		}
		int bnum = commentNum.getNum();
		int idx = commentNum.getIdx();
		log.info("BoardController commentReplyWrite execute bnum : " + bnum + ", idx : " + idx);
		String comment = commentNum.getComment();
		if(IsValid.isNotValidInts(bnum)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(bnum);
		if(IsValid.isNotValidObjects(board)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		if(IsValid.isNotValidInts(idx)){
			rea.addFlashAttribute("message", "seq가 없습니다.");
			rea.addAttribute("num", bnum);
			return "redirect:/boardDetail";
		}
		if(StringUtils.isEmpty(comment)){
			rea.addFlashAttribute("message", "comment가 없습니다.");
			rea.addAttribute("num", bnum);
			return "redirect:/boardDetail";
		}
		if(!commentService.replyWrite(idx, common.enter(common.cleanXss(comment)), (int)session.getAttribute("idx"), bnum)){
			rea.addFlashAttribute("message", "답글작성에 실패했습니다.");
			rea.addAttribute("num", bnum);
			return "redirect:/boardDetail";
		}
		rea.addAttribute("num", bnum);
		return "redirect:/boardDetail";
	}
	
	@RequestMapping(value="addBoardLikeCount", method=RequestMethod.POST)
	public void addBoardLikeCount(BoardLikeCount boardLikeCount){
		log.info("execute BoardController addBoardLikeCount");
	}
}
