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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		log.info("execute UserController addUserView");
		return "view/user/join";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(@ModelAttribute User user, HttpServletRequest req, HttpServletResponse res){
		log.info("execute UserController addUser");
		HttpSession session = req.getSession();
		Date date = new Date();
		user.setRegDate(date);
		user.setUpDate(date);
		
		OkCheck ok = userService.join(user);
		session.setAttribute("message", ok.getMessage());
		if(ok.isBool()){
			session.setAttribute("loginYn", "Y");
			session.setAttribute("idx", user.getIdx());
			session.setAttribute("id", user.getId());
			session.setAttribute("name", user.getName());
			session.setAttribute("email", user.getEmail());
		    
			return "redirect:/board";
		} else {
			return "view/user/join";
		}
	}
	
	@RequestMapping(value="/idCheck", method=RequestMethod.POST)
	public String idCheck(@RequestParam String id, HttpServletRequest req){
		log.info("execute UserController idCheck");
		HttpSession session = req.getSession();
		session.setAttribute("message", userService.idCheck(id).getMessage());
		req.setAttribute("bool", userService.idCheck(id).isBool());
		
		return "common/ajaxPage";
	}
	
	@RequestMapping(value="/emailCheck", method=RequestMethod.POST)
	public String emailCheck(@RequestParam String email, HttpServletRequest req){
		log.info("execute UserController emailCheck");
		HttpSession session = req.getSession();
		session.setAttribute("message", userService.emailCheck(email).getMessage());
		req.setAttribute("bool", userService.emailCheck(email).isBool());
		
		return "common/ajaxPage";
	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String loginView(HttpServletRequest req, RedirectAttributes rea){
		log.info("execute UserController loginView");
		HttpSession session = req.getSession();
		if(session.getAttribute("loginYn") != null && session.getAttribute("loginYn").equals("Y")){
			return "redirect:/board";
		}
		session.setAttribute("message", rea.getFlashAttributes().get("message"));
		return "view/user/login";
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String login(@ModelAttribute User user, @RequestParam(required=false) String idSave, HttpServletRequest req, HttpServletResponse res){
		log.info("execute UserController login");
		HttpSession session = req.getSession();
		String id = user.getId();
		String password = user.getPassword();
		user = userService.loginCheck(id, password);
		if(user != null){
			session.setAttribute("loginYn", "Y");
			session.setAttribute("idx", user.getIdx());
			session.setAttribute("id", user.getId());
			session.setAttribute("name", user.getName());
			session.setAttribute("email", user.getEmail());
			if(idSave != null && idSave.equals("check")){
				Cookie cookie = new Cookie("saveId", id);
			    cookie.setMaxAge(60*60*24);
			    res.addCookie(cookie);
			}
			return "redirect:/board";
		} else {
			session.setAttribute("message", "로그인에 실패하셨습니다.");
			return "view/user/login";
		}
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(HttpServletRequest req, HttpServletResponse res){
		HttpSession session = req.getSession();
		session.setAttribute("loginYn", "N");
		session.removeAttribute("idx");
		session.removeAttribute("id");
		session.removeAttribute("name");
		session.removeAttribute("email");
		session.setAttribute("message", "로그아웃 하셨습니다.");
		return "redirect:/";
	}
	
	@RequestMapping(value="/translatePassword", method=RequestMethod.POST)
	public String translatePassword(@RequestParam String email, RedirectAttributes rea){
		log.info("execute UserController translatePassword");
		if(email == null || email.equals("")){
			rea.addFlashAttribute("message", "이메일을 입력해주세요.");
		}
		String password = userService.translatePassword(email);
		if(password != null){
			rea.addFlashAttribute("message", "비밀번호가 " + password + "로 수정되었습니다.");
		} else {
			rea.addFlashAttribute("message", "비밀번호 수정을 실패했습니다.");
		}
		return "redirect:/";
	}
	
	@RequestMapping(value="/mypage", method=RequestMethod.GET)
	public String mypageView(HttpServletRequest req){
		log.info("execute UserController mypageView");
		req.setAttribute("id", req.getSession().getAttribute("id"));
		req.setAttribute("name", req.getSession().getAttribute("name"));
		req.setAttribute("email", req.getSession().getAttribute("email"));
		return "view/user/mypage";
	}
	
	@RequestMapping(value="/changePassword", method=RequestMethod.POST)
	public String changePassword(@RequestParam String password, @RequestParam String changePassword, HttpServletRequest req){
		log.info("execute UserController mypageView");
		HttpSession session = req.getSession();
		if(password == null || password.equals("")){
			session.setAttribute("message", "페스워드를 입력해주세요");
		}
		if(changePassword == null || changePassword.equals("")){
			session.setAttribute("message", "수정할 페스워드를 입력해주세요");
		}
		int idx = (int)req.getSession().getAttribute("idx");
		User user = userService.findUserForIdx(idx);
		req.setAttribute("id", user.getId());
		req.setAttribute("name", user.getName());
		req.setAttribute("email", user.getEmail());
		
		OkCheck ok = userService.changePassword(idx, password, changePassword);
		session.setAttribute("message", ok.getMessage());
		return "redirect:/mypage";
	}
	
	@RequestMapping(value="/leave")
	public String leave(@RequestParam String password, HttpServletRequest req){
		log.info("execute UserController leave");
		HttpSession session = req.getSession();
		if(password == null || password.equals("")){
			session.setAttribute("message", "password를 입력해주세요.");
			return "redirect:/mypage";
		}
		User user = userService.loginCheck((String)req.getSession().getAttribute("id"), password);
		if(user == null || user.getIdx() != (int)req.getSession().getAttribute("idx")){
			session.setAttribute("message", "존재하지 않는 아이디입니다.");
			return "redirect:/";
		}
		boolean bool = userService.leave(user.getIdx());
		if(bool){
			session.setAttribute("message", "탈퇴에 성공하셨습니다.");
			return "redirect:/";
		} else {
			session.setAttribute("message", "탈퇴에 실패하셨습니다.");
			return "redirect:/mypage";
		}
	}
}
