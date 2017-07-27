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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.person.common.Common;
import kr.co.person.common.CommonMail;
import kr.co.person.common.Encryption;
import kr.co.person.common.IsValid;
import kr.co.person.common.Message;
import kr.co.person.common.exception.EmptyStringException;
import kr.co.person.domain.Board;
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
	@Autowired private Encryption encryption;
	
	private final int PAGE_SIZE = 5;
	private final int PAGE_SIZE_CONTROL_NUM = 1;
	private final int MAX_COUNT_OF_PAGE = 20;
	private String saveSort = "";
	private Direction direction = Direction.DESC;
	
	@GetMapping("/admin/users")
	public String adminUsers(@RequestParam(required=false, defaultValue="0") int pageNum, 
			@RequestParam(required=false, defaultValue="") String sort, HttpSession session, 
			Model model, RedirectAttributes rea){
		log.info("execute AdminController adminUsers");
		if(!common.adminSessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
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
		Pageable pageable = new CustomPageable(pageNum, MAX_COUNT_OF_PAGE, direction, sort);
		int maxPage = adminService.findBoardAll(pageable).getTotalPages();
		if(pageNum > maxPage){
			pageNum = maxPage - 1;
			pageable = new CustomPageable(pageNum, MAX_COUNT_OF_PAGE, Direction.DESC, "idx");
			model.addAttribute("message", message.BOARD_LAST_PAGE_EXCESS);
		}
		Page<User> pages = adminService.findUserAll(pageable);
		if(IsValid.isNotValidObjects(pages)){
			return "redirect:/";
		}
		int startPage = pageNum / PAGE_SIZE * PAGE_SIZE + PAGE_SIZE_CONTROL_NUM;
		int lastPage = (pageNum / PAGE_SIZE + PAGE_SIZE_CONTROL_NUM) * PAGE_SIZE;
		if(lastPage > maxPage){
			lastPage = maxPage;
		}
		
		model.addAttribute("users", pages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("lastPage", lastPage);
		model.addAttribute("maxPage", maxPage);
		model.addAttribute("include", "/view/admin/adminUsers.ftl");
		return "view/frame";
	}
	
	@PostMapping("/admin/translatePassword")
	public String translatePassword(@RequestParam(required=false) String email, 
			RedirectAttributes rea, HttpSession session){
		log.info("execute AdminController translatePassword");
		if(!common.adminSessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		OkCheck okEmail = common.isEmail(email);
		if(!okEmail.isBool()){
			rea.addFlashAttribute("message", okEmail.getMessage());
			return "redirect:/admin/users";
		}
		OkCheck okPassword = userService.translatePassword(email);
		if(okPassword.isBool()){
			try {
				commonMail.sendMail(email, message.MAIL_TRANSLATE_PASSWORD_TITLE, okPassword.getMessage());
			} catch (MessagingException e) {
				rea.addFlashAttribute("message", message.MAIL_FAIL_SEND);
				return "redirect:/admin/users";
			}
			rea.addFlashAttribute("message", message.MAIL_SUCCESS_TRANSLATE_PASSWORD);
		} else {
			rea.addFlashAttribute("message", okPassword.getMessage());
		}
		return "redirect:/admin/users";
	}
	
	@PostMapping("/admin/emailAccessRe")
	public String reEmailAccess(@RequestParam(required=false) String email, 
			RedirectAttributes rea, HttpSession session){
		log.info("execute AdminController reEmailAccess");
		if(!common.adminSessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		OkCheck ok = common.isEmail(email);
		if(!ok.isBool()){
			rea.addFlashAttribute("message", message.USER_NO_EMAIL);
			return "redirect:/admin/users";
		}
		try {
			commonMail.sendMail(email, message.ACCESS_THANK_YOU_FOR_JOIN, "<a href='http://localhost:8080/emailAccess?access=" + encryption.aesEncode(email) + "'>동의</a>");
		} catch(EmptyStringException e) {
			rea.addFlashAttribute("message", message.USER_NO_EMAIL);
		} catch(MessagingException e){
			rea.addFlashAttribute("message", message.MAIL_FAIL_SEND);
		} catch(Exception e){
			rea.addFlashAttribute("message", message.USER_RE_EMAIL);
		}
		return "redirect:/admin/users";
	}
	
	@GetMapping("/admin/boards")
	public String adminBoards(@RequestParam(required=false, defaultValue="0") int pageNum, 
			@RequestParam(required=false, defaultValue="") String sort, HttpSession session, 
			Model model, RedirectAttributes rea){
		log.info("execute AdminController adminBoards");
		if(!common.adminSessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
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
		Pageable pageable = new CustomPageable(pageNum, MAX_COUNT_OF_PAGE, direction, sort);
		int maxPage = adminService.findBoardAll(pageable).getTotalPages();
		if(pageNum > maxPage){
			pageNum = maxPage - 1;
			pageable = new CustomPageable(pageNum, MAX_COUNT_OF_PAGE, Direction.DESC, "idx");
			model.addAttribute("message", message.BOARD_LAST_PAGE_EXCESS);
		}
		Page<Board> pages = adminService.findBoardAll(pageable);
		if(IsValid.isNotValidObjects(pages)){
			return "redirect:/";
		}
		int startPage = pageNum / PAGE_SIZE * PAGE_SIZE + PAGE_SIZE_CONTROL_NUM;
		int lastPage = (pageNum / PAGE_SIZE + PAGE_SIZE_CONTROL_NUM) * PAGE_SIZE;
		if(lastPage > maxPage){
			lastPage = maxPage;
		}
		model.addAttribute("boards", pages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("lastPage", lastPage);
		model.addAttribute("maxPage", maxPage);
		model.addAttribute("include", "/view/admin/adminBoards.ftl");
		return "view/frame";
	}
}
