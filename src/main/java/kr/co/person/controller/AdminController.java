package kr.co.person.controller;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.person.common.Common;
import kr.co.person.common.CommonMail;
import kr.co.person.common.Message;
import kr.co.person.pojo.OkCheck;
import kr.co.person.service.AdminService;
import kr.co.person.service.UserService;

@Controller
public class AdminController {
	private static final Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired private Common common;
	@Autowired private Message message;
	@Autowired private AdminService adminService;
	@Autowired private UserService userService;
	@Autowired private CommonMail commonMail;
	
	@RequestMapping(value="/admin/users", method=RequestMethod.GET)
	public String adminView(HttpSession session, Model model, RedirectAttributes rea){
		log.info("execute AdminController adminView");
		if(!common.adminSessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		model.addAttribute("users", adminService.findUserAll());
		model.addAttribute("include", "/view/admin/adminView.ftl");
		return "view/frame";
	}
	
	@RequestMapping(value="/admin/translatePassword", method=RequestMethod.POST)
	public String translatePassword(@RequestParam(required=false) String email, RedirectAttributes rea){
		log.info("execute AdminController translatePassword");
		if(StringUtils.isEmpty(email)){
			rea.addFlashAttribute("message", message.USER_NO_EMAIL);
			return "redirect:/admin/users";
		}
		OkCheck ok = userService.translatePassword(email);
		if(ok.isBool()){
			try {
				commonMail.sendMail(email, message.MAIL_TRANSLATE_PASSWORD_TITLE, ok.getMessage());
			} catch (MessagingException e) {
				rea.addFlashAttribute("message", message.MAIL_FAIL_SEND);
				return "redirect:/admin/users";
			}
			rea.addFlashAttribute("message", message.MAIL_SUCCESS_TRANSLATE_PASSWORD);
		} else {
			rea.addFlashAttribute("message", ok.getMessage());
		}
		return "redirect:/admin/users";
	}
}
