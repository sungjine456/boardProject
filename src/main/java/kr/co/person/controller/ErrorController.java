package kr.co.person.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
	private static final Logger log = LoggerFactory.getLogger(ErrorController.class);
	
	@GetMapping("/error404")
	public String error404(HttpServletResponse res){
		log.info("execute ErrorController error404");
		res.setStatus(HttpServletResponse.SC_OK);
		return "view/error/404error";
	}
}
