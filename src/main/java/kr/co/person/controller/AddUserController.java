package kr.co.person.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.person.BoardProjectApplication;
import kr.co.person.domain.User;
import kr.co.person.service.UserService;

@Controller
public class AddUserController {
	static final Logger log = LoggerFactory.getLogger(BoardProjectApplication.class);
	
	@Autowired UserService userService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String addUserView(HttpServletRequest req){
		log.info("execute AddUserViewController");
		req.setAttribute("message", "");
		return "view/addUser";
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String addUser(@ModelAttribute User user, HttpServletRequest req){
		log.info("execute AddUserController");
		Date date = new Date();
		user.setRegDate(date);
		user.setUpDate(date);
		
		boolean bool = userService.create(user);
		if(!bool){
			req.setAttribute("message", "회원가입에 실패하셨습니다.");
			return "view/addUser";
		} else {
			req.setAttribute("message", "회원가입에 성공하셨습니다.");
			return "view/addUser";
		}
	}
	
	@RequestMapping(value="/idCheck", method=RequestMethod.POST)
	public String idCheck(@ModelAttribute User user, HttpServletRequest req){
		log.info("execute idCheckController");
		boolean bool = userService.idCheck(user.getId());
		if(bool){
			req.setAttribute("message", "가입 가능한 아이디입니다");
		} else {
			req.setAttribute("message", "이미 가입되어 있는 아이디입니다.");
		}
		
		return "view/ajaxPage";
	}
}
