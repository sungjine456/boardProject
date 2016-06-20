package kr.co.person.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.person.common.Common;
import kr.co.person.service.BoardService;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	@Autowired
	private Common common;
	
	@RequestMapping(value="/board", method=RequestMethod.GET)
	public String main(HttpServletRequest req){
		req.setAttribute("boardList", boardService.findAll());
		return "view/board/frame";
	}
	
	@RequestMapping(value="/boardWrite", method=RequestMethod.GET)
	public String boardWriteView(HttpServletRequest req){
		req.setAttribute("include", "main/write.ftl");
		return "view/board/frame";
	}

	@RequestMapping(value="/boardWrite", method=RequestMethod.POST)
	public String boardWrite(@RequestParam String title, @RequestParam String content, HttpServletRequest req){
		HttpSession session = req.getSession();
		boardService.write(common.cleanXss(title), common.cleanXss(content), (int)session.getAttribute("idx"));
		return "view/board/frame";
	}
}
