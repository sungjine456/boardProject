package kr.co.person.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.person.BoardProjectApplication;
import kr.co.person.domain.User;
import kr.co.person.service.UserService;

@Controller
public class UserController {
	static final Logger log = LoggerFactory.getLogger(BoardProjectApplication.class);
	
	@Autowired UserService userService;
	
	@RequestMapping(value="/addUser", method=RequestMethod.GET)
	public String addUserView(HttpServletRequest req){
		log.info("execute AddUserViewController addUserView");
		return "view/addUser";
	}
	
	@RequestMapping(value="/addUser", method=RequestMethod.POST)
	public String addUser(@ModelAttribute User user, HttpServletRequest req){
		log.info("execute AddUserViewController addUser");
		Date date = new Date();
		user.setRegDate(date);
		user.setUpDate(date);
		
		boolean bool = userService.join(user);
		if(!bool){
			req.setAttribute("message", "회원가입에 실패하셨습니다.");
			return "view/addUser";
		} else {
			req.setAttribute("message", "회원가입에 성공하셨습니다.");
			return "view/login";
		}
	}
	
	@RequestMapping(value="/idCheck", method=RequestMethod.POST)
	public String idCheck(@RequestParam String id, HttpServletRequest req){
		log.info("execute AddUserViewController idCheck");
		boolean bool = userService.idCheck(id);
		if(bool){
			req.setAttribute("message", "가입 가능한 아이디입니다");
		} else {
			req.setAttribute("message", "이미 가입되어 있는 아이디입니다.");
		}
		
		return "common/ajaxPage";
	}
	
	@RequestMapping(value="/emailCheck", method=RequestMethod.POST)
	public String emailCheck(@RequestParam String email, HttpServletRequest req){
		log.info("execute AddUserViewController emailCheck");
		req.setAttribute("message", userService.emailCheck(email));
		
		return "common/ajaxPage";
	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String loginView(){
		log.info("execute AddUserViewController loginView");
		return "view/login";
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String login(@ModelAttribute User user, HttpServletRequest req, HttpServletResponse res){
		log.info("execute AddUserViewController login");
		HttpSession session = req.getSession();
		String id = user.getId();
		String password = user.getPassword();
		user = userService.loginCheck(id, password);
		if(user != null){
			session.setAttribute("idx", user.getIdx());
			session.setAttribute("id", user.getId());
			session.setAttribute("name", user.getName());
			session.setAttribute("email", user.getEmail());
			req.setAttribute("user", user);
			return "view/board";
		} else {
			req.setAttribute("message", "로그인에 실패하셨습니다.");
			return "view/login";
		}
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	public String logout(HttpServletRequest req){
		HttpSession session = req.getSession();
		session.invalidate();
		req.setAttribute("message", "로그아웃 하셨습니다.");
		return "view/login";
	}
}
