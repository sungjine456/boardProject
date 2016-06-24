package kr.co.person.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	@RequestMapping(value="/error404", method=RequestMethod.GET)
	public String error404(HttpServletResponse res){
		log.info("ErrorController execute");
		res.setStatus(HttpServletResponse.SC_OK);
		return "view/error/404error";
	}
}
