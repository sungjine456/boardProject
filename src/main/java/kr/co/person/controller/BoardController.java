package kr.co.person.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import kr.co.person.domain.Comment;
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
	public String main(@RequestParam(required=false) Integer num, Model model, HttpServletRequest req, RedirectAttributes rea){
		log.info("BoardController main execute");
		log.info("BoardController main execute num : " + num);
		log.info("BoardController main execute message  :  " + rea.getFlashAttributes().get("message"));
		if(IsValid.isNotValid(num)){
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
		model.addAttribute("message", rea.getFlashAttributes().get("message"));
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
	public String boardWrite(@RequestParam(required=false) String title, @RequestParam(required=false) String content, Model model, HttpSession session){
		log.info("BoardController boardWrite execute");
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
	public String boardDetailView(@RequestParam(required=false) Integer num, Model model, RedirectAttributes rea){
		log.info("BoardController boardDetailView execute");
		if(IsValid.isNotValid(num)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(num);
		String message = (String)rea.getFlashAttributes().get("message");
		if(StringUtils.isNotEmpty(message)){
			model.addAttribute("message", message);
		}
		if(IsValid.isNotValid(board)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		List<Comment> comments = commentService.findAllCommentByBoard(board.getIdx());
		model.addAttribute("comments", comments);
		model.addAttribute("include", "main/boardDetail.ftl");
		model.addAttribute("board", board);
		model.addAttribute("num", num);
		return "view/board/frame";
	}
	
	@RequestMapping(value="/boardUpdateView")
	public String boardUpdateView(@RequestParam(required=false) Integer num, Model model, RedirectAttributes rea){
		log.info("BoardController boardUpdateView execute");
		if(IsValid.isNotValid(num)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(num);
		if(IsValid.isNotValid(board)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/boardDetail";
		}
		model.addAttribute("include", "main/update.ftl");
		model.addAttribute("board", board);
		model.addAttribute("num", num);
		return "view/board/frame";
	}
	
	@RequestMapping(value="/boardUpdate", method=RequestMethod.POST)
	public String boardUpdate(@RequestParam(required=false) Integer num, @RequestParam(required=false) String title, @RequestParam(required=false) String content, RedirectAttributes rea){
		log.info("BoardController boardUpdate execute");
		if(IsValid.isNotValid(num)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(num);
		if(IsValid.isNotValid(board)){
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
	public String writeComment(@RequestParam(required=false) Integer num, @RequestParam(required=false) String comment, HttpSession session, RedirectAttributes rea){
		log.info("BoardController writeComment execute");
		if(IsValid.isNotValid(num)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(num);
		if(IsValid.isNotValid(board)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/boardDetail";
		}
		if(StringUtils.isEmpty(comment)){
			rea.addAttribute("num", num);
			return "redirect:/boardDetail";
		}
		if(!commentService.write(common.enter(common.cleanXss(comment)), (int)session.getAttribute("idx"), num)){
			rea.addAttribute("num", num);
			return "redirect:/boardDetail";
		}
		rea.addAttribute("num", num);
		return "redirect:/boardDetail";
	}
	
	@RequestMapping(value="/updateCommentView", method=RequestMethod.POST)
	public String updateCommentView(@RequestParam(required=false) Integer num, @RequestParam(required=false) Integer idx, @RequestParam(required=false) String comment, Model model){
		log.info("BoardController updateCommentView execute");
		model.addAttribute("comment", comment);
		model.addAttribute("num", num);
		model.addAttribute("idx", idx);
		return "view/board/ajax/commentUpdate";
	}
	
	@RequestMapping(value="/updateComment", method=RequestMethod.POST)
	public String updateComment(@RequestParam(required=false) Integer upnum, @RequestParam(required=false) Integer upidx, @RequestParam(required=false) String comment, RedirectAttributes rea){
		log.info("BoardController updateComment execute");
		if(IsValid.isNotValid(upnum)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		Board board = boardService.findBoardForIdx(upnum);
		if(IsValid.isNotValid(board)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		if(IsValid.isNotValid(upidx)){
			rea.addAttribute("num", upnum);
			return "redirect:/boardDetail";
		}
		if(StringUtils.isEmpty(comment)){
			rea.addAttribute("num", upnum);
			return "redirect:/boardDetail";
		}
		if(!commentService.update(upidx, common.enter(common.cleanXss(comment)))){
			rea.addAttribute("num", upnum);
			return "redirect:/boardDetail";
		}
		rea.addAttribute("num", upnum);
		return "redirect:/boardDetail";
	}
	
	@RequestMapping(value="replyView", method=RequestMethod.POST)
	public String commentReplyView(@RequestParam(required=false) Integer num, @RequestParam(required=false) Integer idx, Model model){
		log.info("BoardController commentReplyView execute");
		model.addAttribute("num", num);
		model.addAttribute("idx", idx);
		return "view/board/ajax/commentReply";
	}
	
	@RequestMapping(value="writeReply", method=RequestMethod.POST)
	public String commentReplyWrite(@RequestParam(required=false) Integer bnum, @RequestParam(required=false) Integer cidx, @RequestParam(required=false) String comment, HttpSession session, RedirectAttributes rea){
		log.info("BoardController commentReplyWrite execute");
		if(IsValid.isNotValid(bnum)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		log.info("BoardController commentReplyWrite execute 1");
		Board board = boardService.findBoardForIdx(bnum);
		if(IsValid.isNotValid(board)){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		log.info("BoardController commentReplyWrite execute 2");
		if(IsValid.isNotValid(cidx)){
			rea.addFlashAttribute("message", "seq가 없습니다.");
			rea.addAttribute("num", bnum);
			return "redirect:/boardDetail";
		}
		if(StringUtils.isEmpty(comment)){
			rea.addFlashAttribute("message", "comment가 없습니다.");
			rea.addAttribute("num", bnum);
			return "redirect:/boardDetail";
		}
		if(!commentService.replyWrite(cidx, common.enter(common.cleanXss(comment)), (int)session.getAttribute("idx"), bnum)){
			rea.addFlashAttribute("message", "답글작성에 실패했습니다.");
			rea.addAttribute("num", bnum);
			return "redirect:/boardDetail";
		}
		rea.addAttribute("num", bnum);
		return "redirect:/boardDetail";
	}
}
