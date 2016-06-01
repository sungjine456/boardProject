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
import kr.co.person.domain.OkCheck;
import kr.co.person.domain.User;
import kr.co.person.service.UserService;

@Controller
public class UserController {
	static final Logger log = LoggerFactory.getLogger(BoardProjectApplication.class);
	
	@Autowired UserService userService;
	
	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String joinView(HttpServletRequest req){
		log.info("execute AddUserViewController addUserView");
		return "view/user/join";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(@ModelAttribute User user, HttpServletRequest req, HttpServletResponse res){
		log.info("execute AddUserViewController addUser");
		Date date = new Date();
		user.setRegDate(date);
		user.setUpDate(date);
		
		OkCheck ok = userService.join(user);
		req.setAttribute("message", ok.getMessage());
		if(ok.isBool()){
			HttpSession session = req.getSession();
			session.setAttribute("idx", user.getIdx());
			session.setAttribute("id", user.getId());
			session.setAttribute("name", user.getName());
			session.setAttribute("email", user.getEmail());
			req.setAttribute("user", user);
		    
			return "view/board/frame";
		} else {
			return "view/user/join";
		}
	}
	
	@RequestMapping(value="/idCheck", method=RequestMethod.POST)
	public String idCheck(@RequestParam String id, HttpServletRequest req){
		log.info("execute AddUserViewController idCheck");
		req.setAttribute("message", userService.idCheck(id).getMessage());
		req.setAttribute("bool", userService.idCheck(id).isBool());
		
		return "common/ajaxPage";
	}
	
	@RequestMapping(value="/emailCheck", method=RequestMethod.POST)
	public String emailCheck(@RequestParam String email, HttpServletRequest req){
		log.info("execute AddUserViewController emailCheck");
		req.setAttribute("message", userService.emailCheck(email).getMessage());
		req.setAttribute("bool", userService.emailCheck(email).isBool());
		
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
				Cookie cookie = new Cookie("saveId", idSave);
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
		req.setAttribute("message", "로그아웃 하셨습니다.");
		return "view/user/login";
	}
	
	@RequestMapping(value="/translatePassword", method=RequestMethod.POST)
	public String translatePassword(@RequestParam String email, HttpServletRequest req){
		log.info("execute AddUserViewController translatePassword");
		if(email == null || email.equals("")){
			req.setAttribute("message", "이메일을 입력해주세요.");
		}
		String password = userService.translatePassword(email);
		if(password != null){
			req.setAttribute("message", "비밀번호가 " + password + "로 수정되었습니다.");
		} else {
			req.setAttribute("message", "비밀번호 수정을 실패했습니다.");
		}
		return "view/user/login";
	}
	
	@RequestMapping(value="/mypage", method=RequestMethod.GET)
	public String mypageView(HttpServletRequest req){
		log.info("execute AddUserViewController mypageView");
		if(req.getSession().getAttribute("idx") == null){
			log.info("execute AddUserViewController no login");
			req.setAttribute("message", "로그인 후 이용해 주세요.");
			return "view/user/login";
		}
		int idx = (int)req.getSession().getAttribute("idx");
		User user = userService.findUserForIdx(idx);
		req.setAttribute("id", user.getId());
		req.setAttribute("name", user.getName());
		req.setAttribute("email", user.getEmail());
		return "view/user/mypage";
	}
	
	@RequestMapping(value="/changePassword", method=RequestMethod.POST)
	public String changePassword(@RequestParam String password, @RequestParam String changePassword, HttpServletRequest req){
		log.info("execute AddUserViewController mypageView");
		if(req.getSession().getAttribute("idx") == null){
			log.info("execute AddUserViewController no login");
			req.setAttribute("message", "로그인 후 이용해 주세요.");
			return "view/user/login";
		}
		int idx = (int)req.getSession().getAttribute("idx");
		User user = userService.findUserForIdx(idx);
		req.setAttribute("id", user.getId());
		req.setAttribute("name", user.getName());
		req.setAttribute("email", user.getEmail());
		
		OkCheck ok = userService.changePassword(idx, password, changePassword);
		req.setAttribute("message", ok.getMessage());
		return "view/user/mypage";
	}
}
