package kr.co.person.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.person.common.Common;
import kr.co.person.common.Message;
import kr.co.person.service.AdminService;

@Controller
public class AdminController {
	private static final Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired private Common common;
	@Autowired private Message message;
	@Autowired private AdminService adminService;
	
	@RequestMapping(value="/adminView/users", method=RequestMethod.GET)
	public String adminView(HttpSession session, Model model, RedirectAttributes rea){
		log.info("execute AdminController adminView");
		if(!common.adminSessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		model.addAttribute("users", adminService.findUserAll());
		model.addAttribute("include", "/view/admin/adminView.ftl");
		return "view/board/frame";
	}
}
