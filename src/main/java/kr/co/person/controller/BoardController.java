package kr.co.person.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/board")
public class BoardController {
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String main(){
		return "view/board/frame";
	}
}
