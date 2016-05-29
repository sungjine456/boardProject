package kr.co.person.controller;

import java.util.Date;

import javax.servlet.http.Cookie;
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
	
	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String addUserView(HttpServletRequest req){
		log.info("execute AddUserViewController addUserView");
		return "view/user/join";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String addUser(@ModelAttribute User user, HttpServletRequest req){
		log.info("execute AddUserViewController addUser");
		Date date = new Date();
		user.setRegDate(date);
		user.setUpDate(date);
		
		boolean bool = userService.join(user);
		if(!bool){
			req.setAttribute("message", "회원가입에 실패하셨습니다.");
			return "view/user/join";
		} else {
			req.setAttribute("message", "회원가입에 성공하셨습니다.");
			return "view/user/login";
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
	public String loginView(HttpServletRequest req){
		log.info("execute AddUserViewController loginView");
		if(req.getSession().getAttribute("name") != null){
			return "view/board/frame";
		}
		return "view/user/login";
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String login(@ModelAttribute User user, @RequestParam(required=false) String idSave, HttpServletRequest req, HttpServletResponse res){
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
			if(idSave != null && idSave.equals("check")){
				Cookie cookie = new Cookie("saveId", user.getId());
			    cookie.setMaxAge(60*60*24);
			    res.addCookie(cookie);
			}
			return "view/board/frame";
		} else {
			req.setAttribute("message", "로그인에 실패하셨습니다.");
			return "view/user/login";
		}
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(HttpServletRequest req, HttpServletResponse res){
		HttpSession session = req.getSession();
		session.invalidate();
		Cookie cookie = new Cookie("saveId", null);
		cookie.setMaxAge(0);
	    res.addCookie(cookie);
		req.setAttribute("message", "로그아웃 하셨습니다.");
		return "view/user/login";
	}
	
	@RequestMapping(value="/translatePassword", method=RequestMethod.POST)
	public String translatePassword(@RequestParam String email, HttpServletRequest req){
		log.info("execute AddUserViewController translatePassword");
		String password = userService.translatePassword(email);
		if(password != null){
			req.setAttribute("message", "비밀번호가 " + password + "로 수정되었습니다.");
			return "view/user/login";
		} else {
			req.setAttribute("message", "비밀번호 수정을 실패했습니다.");
			return "view/user/login";
		}
	}
}
