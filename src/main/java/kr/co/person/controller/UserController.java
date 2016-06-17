package kr.co.person.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.person.common.Common;
import kr.co.person.domain.OkCheck;
import kr.co.person.domain.User;
import kr.co.person.service.UserService;

@Controller
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	@Value("${keyValue}")
	private String ENCRYPTION_KEY;
	
	@Autowired 
	private UserService userService;
	@Autowired 
	private Common common;
	
	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String joinView(){
		log.info("execute UserController addUserView");
		return "view/user/join";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(@ModelAttribute User user, HttpServletRequest req){
		log.info("execute UserController addUser");
		HttpSession session = req.getSession();
		if(user == null){
			req.setAttribute("message", "회원가입에 실패하셨습니다.");
			return "view/user/join";
		}
		user.setId(common.cleanXss(user.getId()));
		user.setName(common.cleanXss(user.getName()));
		
		OkCheck ok = userService.join(user);
		req.setAttribute("message", ok.getMessage());
		if(ok.isBool()){
			session.setAttribute("loginYn", "Y");
			session.setAttribute("idx", user.getIdx());
			session.setAttribute("id", user.getId());
			session.setAttribute("name", user.getName());
			session.setAttribute("email", user.getEmail());
			return "view/board/frame";
		} else {
			return "view/user/join";
		}
	}
	
	@RequestMapping(value="/join/idCheck", method=RequestMethod.POST)
	public @ResponseBody Map<String, String> idCheck(@RequestParam String id, HttpServletRequest req){
		log.info("execute UserController idCheck");
		Map<String, String> map = new HashMap<String, String>();
		if(StringUtils.isEmpty(id)){
			map.put("str", "아이디를 다시입력해주세요.");
			map.put("bool", "false");
			
			return map;
		}
		OkCheck ok = userService.idCheck(common.cleanXss(id));
		map.put("str", ok.getMessage());
		map.put("bool", "" + ok.isBool());
		
		return map;
	}
	
	@RequestMapping(value="/join/emailCheck", method=RequestMethod.POST)
	public @ResponseBody Map<String, String> emailCheck(@RequestParam String email, HttpServletRequest req){
		log.info("execute UserController emailCheck");
		Map<String, String> map = new HashMap<String, String>();
		if(StringUtils.isEmpty(email)){
			map.put("str", "이메일을 다시입력해주세요.");
			map.put("bool", "false");
			
			return map;
		}
		OkCheck ok = userService.emailCheck(email);
		map.put("str", ok.getMessage());
		map.put("bool", "" + ok.isBool());
		
		return map;
	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String loginView(@RequestParam(value="message",required=false) String message, HttpServletRequest req, RedirectAttributes rea){
		log.info("execute UserController loginView");
		HttpSession session = req.getSession();
		if(StringUtils.isNotEmpty(message)){
			log.info("execute UserController getMessage");
			req.setAttribute("message", message);
			return "view/user/login";
		}
		log.info("execute UserController no message");
		String id = "";
		String ip = req.getRemoteAddr();
		Cookie[] cookies = req.getCookies();
		if(cookies != null){
			for(int i = 0; i < cookies.length; i++){
				String key = cookies[i].getName();
				String val = cookies[i].getValue();
				if("saveId".equals(key)){
					id = common.cookieAesDecode(ENCRYPTION_KEY, val);
				}
			}
		}
		User user = userService.findUserForId(id);
		if(user != null && userService.autoLogin(user, ip)){
			session.setAttribute("loginYn", "Y");
			session.setAttribute("idx", user.getIdx());
			session.setAttribute("id", user.getId());
			session.setAttribute("name", user.getName());
			session.setAttribute("email", user.getEmail());
			return "view/board/frame";
		}
		if(session.getAttribute("loginYn") != null && session.getAttribute("loginYn").equals("Y")){
			return "view/board/frame";
		}
		req.setAttribute("message", rea.getFlashAttributes().get("message"));
		return "view/user/login";
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String login(@ModelAttribute User user, @RequestParam(required=false) String idSave, HttpServletRequest req, HttpServletResponse res){
		log.info("execute UserController login");
		HttpSession session = req.getSession();
		if(user == null){
			req.setAttribute("message", "로그인에 실패하셨습니다.");
			return "view/user/login";
		}
		String id = common.cleanXss(user.getId());
		user = userService.findUserForId(id);
		if(user != null){
			session.setAttribute("loginYn", "Y");
			session.setAttribute("idx", user.getIdx());
			session.setAttribute("id", user.getId());
			session.setAttribute("name", user.getName());
			session.setAttribute("email", user.getEmail());
			if(idSave != null && idSave.equals("check")){
				String ip = req.getRemoteAddr();
				if(!userService.autoLogin(user, ip)){
					req.setAttribute("message", "로그인에 실패하셨습니다.");
					return "view/user/login";
				}
				String enKeyId = common.cookieAesEncode(ENCRYPTION_KEY, id);
				if(StringUtils.isEmpty(enKeyId)){
					req.setAttribute("message", "로그인에 실패하셨습니다.");
					return "view/user/login";
				}
				Cookie cookie = new Cookie("saveId", enKeyId);
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
	public String logout(HttpServletRequest req, HttpServletResponse res, RedirectAttributes rea){
		String url = req.getRequestURI();
		HttpSession session = req.getSession();
		int idx = (int)session.getAttribute("idx");
		if(idx == 0){
			return url;
		}
		session.setAttribute("loginYn", "N");
		session.removeAttribute("idx");
		session.removeAttribute("id");
		session.removeAttribute("name");
		session.removeAttribute("email");
		rea.addFlashAttribute("message", "로그아웃 하셨습니다.");
		String ip = req.getRemoteAddr();
		User user = userService.findUserForIdx(idx);
		if(user == null || !userService.autoLogout(user, ip)){
			return url;
		}
		Cookie cookie = new Cookie("saveId", null);
		cookie.setMaxAge(0);
	    res.addCookie(cookie);
		return "redirect:/";
	}
	
	@RequestMapping(value="/translatePassword", method=RequestMethod.POST)
	public String translatePassword(@RequestParam String email, RedirectAttributes rea){
		log.info("execute UserController translatePassword");
		if(StringUtils.isEmpty(email)){
			rea.addFlashAttribute("message", "이메일을 입력해주세요.");
			return "redirect:/";
		}
		OkCheck ok = userService.translatePassword(email);
		if(ok.isBool()){
			rea.addFlashAttribute("message", ok.getMessage());
		} else {
			rea.addFlashAttribute("message", ok.getMessage());
		}
		return "redirect:/";
	}
	
	@RequestMapping(value="/mypage", method=RequestMethod.GET)
	public String mypageView(HttpServletRequest req, RedirectAttributes rea){
		log.info("execute UserController mypageView");
		req.setAttribute("id", req.getSession().getAttribute("id"));
		req.setAttribute("name", req.getSession().getAttribute("name"));
		req.setAttribute("email", req.getSession().getAttribute("email"));
		req.setAttribute("message", rea.getFlashAttributes().get("message"));
		return "view/user/mypage";
	}
	
	@RequestMapping(value="/changePassword", method=RequestMethod.POST)
	public String changePassword(@RequestParam String password, @RequestParam String changePassword, HttpServletRequest req, RedirectAttributes rea){
		log.info("execute UserController mypageView");
		if(StringUtils.isEmpty(password)){
			rea.addFlashAttribute("message", "페스워드를 입력해주세요");
			return "redirect:/mypage";
		}
		if(StringUtils.isEmpty(changePassword)){
			rea.addFlashAttribute("message", "수정할 페스워드를 입력해주세요");
			return "redirect:/mypage";
		}
		int idx = (int)req.getSession().getAttribute("idx");
		User user = userService.findUserForIdx(idx);
		req.setAttribute("id", user.getId());
		req.setAttribute("name", user.getName());
		req.setAttribute("email", user.getEmail());
		
		OkCheck ok = userService.changePassword(idx, password, changePassword);
		rea.addFlashAttribute("message", ok.getMessage());
		return "redirect:/mypage";
	}
	
	@RequestMapping(value="/leave")
	public String leave(@RequestParam String password, HttpServletRequest req, HttpServletResponse res, RedirectAttributes rea){
		log.info("execute UserController leave");
		HttpSession session = req.getSession();
		if(StringUtils.isEmpty(password)){
			rea.addFlashAttribute("message", "password를 입력해주세요.");
			return "redirect:/mypage";
		}
		User user = userService.loginCheck((String)req.getSession().getAttribute("id"), password);
		if(user == null || user.getIdx() != (int)req.getSession().getAttribute("idx")){
			rea.addFlashAttribute("message", "존재하지 않는 아이디입니다.");
			return "redirect:/";
		}
		boolean bool = userService.leave(user.getIdx());
		if(bool){
			session.setAttribute("loginYn", "N");
			session.removeAttribute("idx");
			session.removeAttribute("id");
			session.removeAttribute("name");
			session.removeAttribute("email");
			Cookie cookie = new Cookie("saveId", null);
			cookie.setMaxAge(0);
		    res.addCookie(cookie);
		    rea.addFlashAttribute("message", "탈퇴에 성공하셨습니다.");
			return "redirect:/";
		} else {
			rea.addFlashAttribute("message", "탈퇴에 실패하셨습니다.");
			return "redirect:/mypage";
		}
	}
	
	@RequestMapping("/interceptorView")
	public String interceptorView(){
		return "common/interceptorPage";
	}
}
