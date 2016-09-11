package kr.co.person.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.person.annotation.IsValidUser;
import kr.co.person.common.Common;
import kr.co.person.common.IsValid;
import kr.co.person.common.Message;
import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;
import kr.co.person.service.UserService;

@Controller
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Value("${keyValue}") private String ENCRYPTION_KEY;
	@Autowired private UserService userService;
	@Autowired private Common common;
	@Autowired private Message message;
	
	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String joinView(){
		log.info("execute UserController joinView");
		return "view/user/join";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(@IsValidUser User user, @RequestParam(required=false) MultipartFile file, Model model, HttpSession session, RedirectAttributes rea){
		log.info("execute UserController join");
		String email = user.getEmail();
		if(StringUtils.isEmpty(email)){
			model.addAttribute("message", message.USER_NO_EMAIL);
			return "view/user/join";
		}
		if(!common.isEmail(email)){
			model.addAttribute("message", message.USER_NO_EMAIL_FORMAT);
			return "view/user/join";
		}
		String imgPath = common.createImg(file, user.getId(), "user");
		String se = File.separator;
		if(StringUtils.isEmpty(imgPath)){
			imgPath = "img"+se+"user"+se+"default.png";
		}
		user.setImg(imgPath);
		OkCheck ok = userService.join(user);
		if(ok.isBool()){
			session.setAttribute("loginYn", "Y");
			session.setAttribute("idx", user.getIdx());
			session.setAttribute("id", user.getId());
			session.setAttribute("name", user.getName());
			session.setAttribute("email", user.getEmail());
			session.setAttribute("img", user.getImg());
			rea.addFlashAttribute("message", ok.getMessage());
			return "redirect:/board";
		} else {
			model.addAttribute("message", ok.getMessage());
			return "view/user/join";
		}
	}
	
	@RequestMapping(value="/join/idCheck", method=RequestMethod.POST)
	public @ResponseBody Map<String, String> idCheck(@RequestParam(required=false) String id){
		log.info("execute UserController idCheck");
		Map<String, String> map = new HashMap<String, String>();
		if(StringUtils.isEmpty(id)){
			map.put("str", message.USER_NO_ID);
			map.put("bool", "false");
			
			return map;
		}
		OkCheck ok = userService.idCheck(common.cleanXss(id));
		map.put("str", ok.getMessage());
		map.put("bool", "" + ok.isBool());
		
		return map;
	}
	
	@RequestMapping(value="/join/emailCheck", method=RequestMethod.POST)
	public @ResponseBody Map<String, String> emailCheck(@RequestParam(required=false) String email){
		log.info("execute UserController emailCheck");
		Map<String, String> map = new HashMap<String, String>();
		if(StringUtils.isEmpty(email)){
			map.put("str", message.USER_NO_EMAIL);
			map.put("bool", "false");
			
			return map;
		}
		OkCheck ok = userService.emailCheck(email);
		map.put("str", ok.getMessage());
		map.put("bool", "" + ok.isBool());
		
		return map;
	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String loginView(Model model, HttpSession session, HttpServletRequest req){
		log.info("execute UserController loginView");
		if(IsValid.isValidObjects(session.getAttribute("loginYn")) && session.getAttribute("loginYn").equals("Y")){
			return "redirect:/board";
		}
		String id = "";
		String loginId = "";
		Cookie[] cookies = req.getCookies();
		if(IsValid.isValidArrays(cookies)){
			for(int i = 0; i < cookies.length; i++){
				String key = cookies[i].getName();
				String val = cookies[i].getValue();
				if(StringUtils.equals("psvd", key)){
					id = common.cookieAesDecode(ENCRYPTION_KEY, val);
				}
				if(StringUtils.equals("psvlgnd", key)){
					loginId = val;
				}
			}
		}
		User user = userService.findUserForId(id);
		if(IsValid.isValidObjects(user) && userService.autoLoginCheck(user, loginId)){
			session.setAttribute("loginYn", "Y");
			session.setAttribute("idx", user.getIdx());
			session.setAttribute("id", user.getId());
			session.setAttribute("name", user.getName());
			session.setAttribute("email", user.getEmail());
			session.setAttribute("img", user.getImg());
			return "redirect:/board";
		}
		return "view/user/login";
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String login(@IsValidUser User user, @RequestParam(required=false) String idSave, Model model, HttpSession session, HttpServletResponse res){
		log.info("execute UserController login");
		String id = user.getId();
		String password = user.getPassword();
		if(IsValid.isNotValidObjects(id, password)){
			model.addAttribute("message", message.USER_WRONG_ID_OR_WRONG_PASSWORD);
			return "view/user/login";
		}
		user = userService.joinCheck(common.cleanXss(id), password);
		if(IsValid.isValidObjects(user)){
			session.setAttribute("loginYn", "Y");
			session.setAttribute("idx", user.getIdx());
			session.setAttribute("id", user.getId());
			session.setAttribute("name", user.getName());
			session.setAttribute("email", user.getEmail());
			session.setAttribute("img", user.getImg());
			if(StringUtils.equals(idSave, "ckeck")){
				String loginId = common.cookieValueEncryption(new DateTime().toString()); 
				if(!userService.autoLogin(user, loginId)){
					model.addAttribute("message", message.USER_FAIL_LOGIN);
					return "view/user/login";
				}
				String enKeyId = common.cookieAesEncode(ENCRYPTION_KEY, id);
				if(StringUtils.isEmpty(enKeyId)){
					model.addAttribute("message", message.USER_FAIL_LOGIN);
					return "view/user/login";
				}
			    res.addCookie(common.addCookie("psvd", enKeyId));
			    res.addCookie(common.addCookie("psvlgnd", loginId));
			}
			return "redirect:/board";
		} else {
			model.addAttribute("message", message.USER_WRONG_ID_OR_WRONG_PASSWORD);
			return "view/user/login";
		}
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(HttpSession session, Model model, HttpServletRequest req, HttpServletResponse res, RedirectAttributes rea){
		log.info("execute UserController logout");
		int idx = IsValid.isValidObjects(session.getAttribute("idx"))?(int)session.getAttribute("idx"):0;
		User user = userService.findUserForIdx(idx);
		if(IsValid.isNotValidObjects(user)){
			model.addAttribute("message", message.USER_NO_LOGIN);
			return "view/user/login";
		}
		String loginId = "";
		Cookie[] cookies = req.getCookies();
		if(IsValid.isValidArrays(cookies)){
			for(int i = 0; i < cookies.length; i++){
				String key = cookies[i].getName();
				String val = cookies[i].getValue();
				if(StringUtils.equals("psvlgnd", key)){
					loginId = val;
				}
			}
		}
		if(StringUtils.isNotEmpty(loginId)){
			if(!userService.autoLogout(user, loginId)){
				rea.addFlashAttribute("message", message.USER_FAIL_LOGOUT);
				return "redirect:/board";
			}
		}
	    res.addCookie(common.removeCookie("psvd"));
	    res.addCookie(common.removeCookie("psvlgnd"));
	    session.setAttribute("loginYn", "N");
		session.removeAttribute("idx");
		session.removeAttribute("id");
		session.removeAttribute("name");
		session.removeAttribute("email");
	    rea.addFlashAttribute("message", message.USER_LOGOUT);
		return "redirect:/";
	}
	
	@RequestMapping(value="/translatePassword", method=RequestMethod.POST)
	public String translatePassword(@RequestParam(required=false) String email, RedirectAttributes rea){
		log.info("execute UserController translatePassword");
		if(StringUtils.isEmpty(email)){
			rea.addFlashAttribute("message", message.USER_NO_EMAIL);
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
	public String myPageView(Model model, HttpSession session){
		log.info("execute UserController mypageView");
		if(!sessionComparedToDB(session)){
			model.addAttribute("message", message.USER_NO_LOGIN);
			return "view/user/login";
		}
		model.addAttribute("include", "/view/user/mypage.ftl");
		return "view/board/frame";
	}
	
	@RequestMapping(value="/changePassword", method=RequestMethod.POST)
	public String changePassword(@RequestParam(required=false) String password, @RequestParam(required=false) String changePassword, Model model, HttpSession session, RedirectAttributes rea){
		log.info("execute UserController changePassword");
		if(!sessionComparedToDB(session)){
			model.addAttribute("message", message.USER_NO_LOGIN);
			return "view/user/login";
		}
		if(StringUtils.isEmpty(password)){
			rea.addFlashAttribute("message", message.USER_NO_PASSWORD);
			return "redirect:/update";
		}
		if(StringUtils.isEmpty(changePassword)){
			rea.addFlashAttribute("message", message.USER_NO_UPDATE_PASSWORD);
			return "redirect:/update";
		}
		if(password.equals(changePassword)){
			rea.addFlashAttribute("message", message.USER_PASSWORD_SAME_UPDATE_PASSWORD);
			return "redirect:/update";
		}
		int idx = IsValid.isValidObjects(session.getAttribute("idx"))?(int)session.getAttribute("idx"):0;
		
		OkCheck ok = userService.changePassword(idx, password, changePassword);
		rea.addFlashAttribute("message", ok.getMessage());
		return "redirect:/mypage";
	}
	
	@RequestMapping(value="/leave")
	public String leave(@RequestParam(required=false) String password, Model model, HttpSession session, HttpServletResponse res, HttpServletRequest req, RedirectAttributes rea){
		log.info("execute UserController leave");
		if(!sessionComparedToDB(session)){
			model.addAttribute("message", message.USER_NO_LOGIN);
			return "view/user/login";
		}
		if(StringUtils.isEmpty(password)){
			rea.addFlashAttribute("message", message.USER_NO_PASSWORD);
			return "redirect:/mypage";
		}
		String id = (String)session.getAttribute("id");
		int idx = IsValid.isValidObjects(session.getAttribute("idx"))?(int)session.getAttribute("idx"):0;
		User user = userService.joinCheck(id, password);
		if(IsValid.isNotValidObjects(user) || user.getIdx() != idx){
			model.addAttribute("message", message.USER_WRONG_USER);
			return "view/user/login";
		}
		String loginId = "";
		Cookie[] cookies = req.getCookies();
		if(IsValid.isValidArrays(cookies)){
			for(int i = 0; i < cookies.length; i++){
				String key = cookies[i].getName();
				String val = cookies[i].getValue();
				if(StringUtils.equals("psvlgnd", key)){
					loginId = val;
				}
			}
		}
		if(StringUtils.isNotEmpty(loginId)){
		    if(!userService.leave(idx, loginId)){
		    	rea.addFlashAttribute("message", message.USER_FAIL_LEAVE);
				return "redirect:/mypage";
		    }
		}
		session.setAttribute("loginYn", "N");
		session.removeAttribute("idx");
		session.removeAttribute("id");
		session.removeAttribute("name");
		session.removeAttribute("email");
	    res.addCookie(common.removeCookie("psvd"));
	    res.addCookie(common.removeCookie("psvlgnd"));
	    rea.addFlashAttribute("message", message.USER_SUCCESS_LEAVE);
		return "redirect:/";
	}
	
	@RequestMapping(value="/updateView", method=RequestMethod.POST)
	public String updateView(@RequestParam(required=false) String password, Model model, HttpSession session, RedirectAttributes rea){
		log.info("execute UserController updateView");
		if(!sessionComparedToDB(session)){
			model.addAttribute("message", message.USER_NO_LOGIN);
			return "view/user/login";
		}
		int idx = IsValid.isValidObjects(session.getAttribute("idx"))?(int)session.getAttribute("idx"):0;
		if(StringUtils.isEmpty(password) || !userService.passwordCheck(idx, password)){
			rea.addFlashAttribute("message", message.USER_NO_PASSWORD);
			return "redirect:/mypage";
		}
		model.addAttribute("include", "/view/user/update.ftl");
		return "view/board/frame";
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(@RequestParam(required=false) MultipartFile ufile, @IsValidUser User user, Model model, HttpSession session){
		log.info("execute UserController update");
		if(!sessionComparedToDB(session)){
			model.addAttribute("message", message.USER_NO_LOGIN);
			return "view/user/login";
		}
		String imgPath = common.createImg(ufile, user.getId(), "user");
		String se = File.separator;
		if(StringUtils.isEmpty(imgPath)){
			imgPath = "img"+se+"user"+se+"default.png";
		}
		String name = user.getName();
		String email = user.getEmail();
		int idx = IsValid.isValidObjects(session.getAttribute("idx"))?(int)session.getAttribute("idx"):0;
		if(StringUtils.isEmpty(email)){
			model.addAttribute("include", "/view/user/update.ftl");
			model.addAttribute("message", message.USER_NO_EMAIL);
			return "view/board/frame";
		}
		if(!common.isEmail(email)){
			model.addAttribute("include", "/view/user/update.ftl");
			model.addAttribute("message", message.USER_NO_EMAIL_FORMAT);
			return "view/board/frame";
		}
		if(StringUtils.isEmpty(name)){
			model.addAttribute("include", "/view/user/update.ftl");
			model.addAttribute("message", message.USER_NO_NAME);
			return "view/board/frame";
		}
		name = common.cleanXss(name);
		if(ufile.getSize() != 0){
			if(userService.update(idx, name, email, imgPath)){
				session.setAttribute("name", name);
				session.setAttribute("email", email);
				session.setAttribute("img", imgPath);
				model.addAttribute("include", "/view/user/mypage.ftl");
				model.addAttribute("message", message.USER_SUCCESS_UPDATE);
			} else {
				model.addAttribute("include", "/view/user/update.ftl");
				model.addAttribute("message", message.USER_FAIL_UPDATE);
			}
		} else {
			if(userService.update(idx, name, email)){
				session.setAttribute("name", name);
				session.setAttribute("email", email);
				model.addAttribute("include", "/view/user/mypage.ftl");
				model.addAttribute("message", message.USER_SUCCESS_UPDATE);
			} else {
				model.addAttribute("include", "/view/user/update.ftl");
				model.addAttribute("message", message.USER_FAIL_UPDATE);
			}
		}
		return "view/board/frame";
	}
	
	@RequestMapping("/interceptorView")
	public String interceptorView(){
		log.info("execute UserController interceptorView");
		return "common/interceptorPage";
	}
	
	private boolean sessionComparedToDB(HttpSession session){
		log.info("sessionComparedToDB Method");
		String loginYn = IsValid.isValidObjects(session.getAttribute("loginYn"))?(String)session.getAttribute("loginYn"):"";
		int idx = IsValid.isValidObjects(session.getAttribute("idx"))?(int)session.getAttribute("idx"):0;
		String id = IsValid.isValidObjects(session.getAttribute("id"))?(String)session.getAttribute("id"):"";
		String name = IsValid.isValidObjects(session.getAttribute("name"))?(String)session.getAttribute("name"):"";
		String email = IsValid.isValidObjects(session.getAttribute("email"))?(String)session.getAttribute("email"):"";
		User user = userService.findUserForIdx(idx);
		return (IsValid.isNotValidObjects(user) || !StringUtils.equals(id, user.getId())
				|| !StringUtils.equals(name, user.getName()) || !StringUtils.equals(email, user.getEmail())
				|| !StringUtils.equals(loginYn, "Y"))
				?false:true;
	}
}
