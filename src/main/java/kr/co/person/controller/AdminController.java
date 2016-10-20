package kr.co.person.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.person.common.Common;
import kr.co.person.common.Message;
import kr.co.person.domain.User;

@Controller
public class AdminController {
	private static final Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired private Common common;
	@Autowired private Message message;
	
	@RequestMapping(value="/adminView", method=RequestMethod.GET)
	public String adminView(HttpSession session, RedirectAttributes rea){
		log.info("execute AdminController adminView");
		log.info(((User)session.getAttribute("user")).getAdmin());
		if(!common.adminSessionComparedToDB(session)){
			log.info("?");
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		return "view/admin/adminView";
	}
}
