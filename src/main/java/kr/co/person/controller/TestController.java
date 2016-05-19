package kr.co.person.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {
	@RequestMapping("/")
	public ModelAndView getBlog(ModelAndView mv) {
		mv.addObject("Title", "hello");
		mv.addObject("message", "hi");
		mv.setViewName("test");
		return mv;
	}
}
