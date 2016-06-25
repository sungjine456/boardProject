package kr.co.person.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.person.common.Common;
import kr.co.person.domain.Board;
import kr.co.person.domain.OkCheck;
import kr.co.person.service.BoardService;

@Controller
public class BoardController {
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);
	@Autowired
	private BoardService boardService;
	@Autowired
	private Common common;
	
	@RequestMapping(value="/board", method=RequestMethod.GET)
	public String main(HttpServletRequest req, RedirectAttributes rea){
		log.info("BoardController main execute");
		req.setAttribute("boardList", boardService.findAll());
		req.setAttribute("message", rea.getFlashAttributes().get("message"));
		return "view/board/frame";
	}
	
	@RequestMapping(value="/boardWrite", method=RequestMethod.GET)
	public String boardWriteView(HttpServletRequest req){
		req.setAttribute("include", "main/write.ftl");
		return "view/board/frame";
	}

	@RequestMapping(value="/boardWrite", method=RequestMethod.POST)
	public String boardWrite(@RequestParam String title, @RequestParam String content, HttpServletRequest req){
		log.info("BoardController boardWrite execute");
		log.info("BoardController boardWrite title : " + title + ",   content : " + content);
		HttpSession session = req.getSession();
		if(StringUtils.isEmpty(title)){
			req.setAttribute("message", "제목을 입력해주세요.");
			req.setAttribute("include", "main/write.ftl");
			return "view/board/frame";
		}
		if(StringUtils.isEmpty(content)){
			req.setAttribute("message", "내용을 입력해주세요.");
			req.setAttribute("include", "main/write.ftl");
			return "view/board/frame";
		}
		OkCheck ok = boardService.write(common.cleanXss(title), common.cleanXss(content), (int)session.getAttribute("idx"));
		if(!ok.isBool()){
			req.setAttribute("message", ok.getMessage());
			req.setAttribute("include", "main/write.ftl");
			return "view/board/frame";
		}
		return "redirect:/board";
	}
	
	@RequestMapping(value="/boardDetail", method=RequestMethod.GET)
	public String boardDetailView(@RequestParam int num, HttpServletRequest req, RedirectAttributes rea){
		log.info("BoardController boardDetailView execute");
		Board board = boardService.findOne(num);
		String message = (String)rea.getFlashAttributes().get("message");
		if(StringUtils.isNotEmpty(message)){
			req.setAttribute("message", message);
		}
		if(board == null){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/board";
		}
		req.setAttribute("include", "main/boardDetail.ftl");
		req.setAttribute("board", board);
		req.setAttribute("num", num);
		return "view/board/frame";
	}
	
	@RequestMapping(value="/boardUpdateView")
	public String boardUpdateView(@RequestParam int num, HttpServletRequest req, RedirectAttributes rea){
		log.info("BoardController boardUpdateView execute");
		Board board = boardService.findOne(num);
		if(board == null){
			rea.addFlashAttribute("message", "존재하지 않는 글입니다.");
			return "redirect:/boardDetail";
		}
		req.setAttribute("include", "main/update.ftl");
		req.setAttribute("board", board);
		req.setAttribute("num", num);
		return "view/board/frame";
	}
	
	@RequestMapping(value="/boardUpdate", method=RequestMethod.POST)
	public String boardUpdate(@RequestParam int num, @RequestParam String title, @RequestParam String content, HttpServletRequest req, RedirectAttributes rea){
		log.info("BoardController boardUpdate execute");
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
}
