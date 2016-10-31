package kr.co.person.controller;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.person.common.Common;
import kr.co.person.common.CommonCookie;
import kr.co.person.common.CommonMail;
import kr.co.person.common.Message;
import kr.co.person.common.exception.EmptyStringException;
import kr.co.person.domain.User;
import kr.co.person.pojo.CustomPageable;
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
	@Autowired private CommonCookie commonCookie;
	
	private final int PAGE_SIZE = 5;
	private final int PAGE_SIZE_CONTROL_NUM = 1;
	private final int MAX_COUNT_OF_PAGE = 20;
	private String saveSort = "";
	private Direction direction = Direction.DESC;
	
	@RequestMapping(value="/admin/users", method=RequestMethod.GET)
	public String adminView(@RequestParam(required=false, defaultValue="0") int pageNum, @RequestParam(required=false, defaultValue="") String sort, HttpSession session, Model model, RedirectAttributes rea){
		log.info("execute AdminController adminView");
		if(pageNum > 0){
			pageNum -= 1;
		}
		if(StringUtils.isEmpty(sort)){
			sort = "idx";
		}
		if(saveSort.equals(sort)){
			if(direction == Direction.DESC){
				direction = Direction.ASC;
			} else {
				direction = Direction.DESC;
			}
		} else {
			saveSort = sort;
		}
		if(!common.adminSessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		Pageable pageable = new CustomPageable(pageNum, MAX_COUNT_OF_PAGE, direction, sort);
		Page<User> pages = adminService.findUserAll(pageable);
		int startPage = pageNum / PAGE_SIZE * PAGE_SIZE + PAGE_SIZE_CONTROL_NUM;
		int lastPage = (pageNum / PAGE_SIZE + PAGE_SIZE_CONTROL_NUM) * PAGE_SIZE;
		int maxPage = pages.getTotalPages();
		if(lastPage > maxPage){
			lastPage = maxPage;
		}
		
		model.addAttribute("users", pages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("lastPage", lastPage);
		model.addAttribute("maxPage", maxPage);
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
	
	@RequestMapping(value="/admin/emailAccessRe", method=RequestMethod.POST)
	public String reEmailAccess(@RequestParam(required=false) String email, RedirectAttributes rea){
		log.info("execute AdminController reEmailAccess");
		OkCheck ok = common.isEmail(email);
		if(!ok.isBool()){
			rea.addFlashAttribute("message", message.USER_NO_EMAIL);
			return "redirect:/admin/users";
		}
		try {
			commonMail.sendMail(email, message.ACCESS_THANK_YOU_FOR_JOIN, "<a href='http://localhost:8080/emailAccess?access=" + commonCookie.aesEncode(email) + "'>동의</a>");
		} catch(EmptyStringException e) {
			rea.addFlashAttribute("message", message.USER_NO_EMAIL);
		} catch(MessagingException e){
			rea.addFlashAttribute("message", message.MAIL_FAIL_SEND);
		} catch(Exception e){
			rea.addFlashAttribute("message", message.USER_RE_EMAIL);
		}
		return "redirect:/admin/users";
	}
}
