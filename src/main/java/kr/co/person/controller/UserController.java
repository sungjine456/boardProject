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
	public String join(User user, @RequestParam(required=false) MultipartFile file, Model model, HttpSession session){
		log.info("execute UserController join");
		if(IsValid.isNotValidObjects(user)){
			model.addAttribute("message", message.USER_FAIL_JOIN);
			return "view/user/join";
		}
		if(IsValid.isNotValidObjects(file)){
			model.addAttribute("message", message.FILE_FAIL_UPLOAD);
			return "view/user/join";
		}
		if(!common.isEmail(user.getEmail())){
			model.addAttribute("message", message.USER_NO_EMAIL_FORMAT);
			return "view/user/join";
		}
		String[] strArray = file.getOriginalFilename().split("\\.");
		String ext = "";
		if(strArray.length == 2){
			ext = strArray[1];
		}
		String imgPath = "";
		String se = File.separator;
		if(StringUtils.equalsIgnoreCase(ext, "gif") || StringUtils.equalsIgnoreCase(ext, "jpg") || StringUtils.equalsIgnoreCase(ext, "jpeg") || StringUtils.equalsIgnoreCase(ext, "png")){
			imgPath = common.createImg(file, ext, user.getId(), se, "user");
		}
		if(StringUtils.isEmpty(imgPath)){
			imgPath = "img"+se+"user"+se+"default.png";
		}
		user.setImg(imgPath);
		OkCheck ok = userService.join(user);
		model.addAttribute("message", ok.getMessage());
		if(ok.isBool()){
			session.setAttribute("loginYn", "Y");
			session.setAttribute("idx", user.getIdx());
			session.setAttribute("id", user.getId());
			session.setAttribute("name", user.getName());
			session.setAttribute("email", user.getEmail());
			session.setAttribute("img", user.getImg());
			return "redirect:/board";
		} else {
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
	public String login(User user, @RequestParam(required=false) String idSave, Model model, HttpSession session, HttpServletResponse res){
		log.info("execute UserController login");
		if(IsValid.isNotValidObjects(user)){
			model.addAttribute("message", message.USER_FAIL_LOGIN);
			return "view/user/login";
		}
		String id = common.cleanXss(user.getId());
		String password = user.getPassword();
		user = userService.loginCheck(id, password);
		if(IsValid.isValidObjects(user)){
			session.setAttribute("loginYn", "Y");
			session.setAttribute("idx", user.getIdx());
			session.setAttribute("id", user.getId());
			session.setAttribute("name", user.getName());
			session.setAttribute("email", user.getEmail());
			session.setAttribute("img", user.getImg());
			if(IsValid.isValidObjects(idSave) && idSave.equals("check")){
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
		int idx = (int)session.getAttribute("idx");
		session.setAttribute("loginYn", "N");
		session.removeAttribute("idx");
		session.removeAttribute("id");
		session.removeAttribute("name");
		session.removeAttribute("email");
		rea.addFlashAttribute("message", message.USER_LOGOUT);
		User user = userService.findUserForIdx(idx);
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
		if(IsValid.isNotValidObjects(user)){
			rea.addFlashAttribute("message", message.USER_FAIL_LOGOUT);
			return "redirect:/board";
		}
		if(StringUtils.isNotEmpty(loginId)){
			if(!userService.autoLogout(user, loginId)){
				rea.addFlashAttribute("message", message.USER_FAIL_LOGOUT);
				return "redirect:/board";
			}
		}
	    res.addCookie(common.removeCookie("psvd"));
	    res.addCookie(common.removeCookie("psvlgnd"));
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
		model.addAttribute("id", session.getAttribute("id"));
		model.addAttribute("name", session.getAttribute("name"));
		model.addAttribute("email", session.getAttribute("email"));
		model.addAttribute("include", "/view/user/mypage.ftl");
		return "view/board/frame";
	}
	
	@RequestMapping(value="/changePassword", method=RequestMethod.POST)
	public String changePassword(@RequestParam(required=false) String password, @RequestParam(required=false) String changePassword, Model model, HttpSession session, RedirectAttributes rea){
		log.info("execute UserController changePassword");
		if(StringUtils.isEmpty(password)){
			rea.addFlashAttribute("message", message.USER_NO_PASSWORD);
			return "redirect:/update";
		}
		if(StringUtils.isEmpty(changePassword)){
			rea.addFlashAttribute("message", message.USER_NO_UPDATE_PASSWORD);
			return "redirect:/update";
		}
		int idx = (int)session.getAttribute("idx");
		if(IsValid.isNotValidInts(idx)){
			model.addAttribute("message", message.USER_NO_LOGIN);
			return "view/user/login";
		}
		User user = userService.findUserForIdx(idx);
		if(IsValid.isNotValidObjects(user)){
			model.addAttribute("include", "/view/user/update.ftl");
			model.addAttribute("message", message.USER_FAIL_UPDATE);
			return "view/board/frame";
		}
		model.addAttribute("id", user.getId());
		model.addAttribute("name", user.getName());
		model.addAttribute("email", user.getEmail());
		
		OkCheck ok = userService.changePassword(idx, password, changePassword);
		rea.addFlashAttribute("message", ok.getMessage());
		return "redirect:/mypage";
	}
	
	@RequestMapping(value="/leave")
	public String leave(@RequestParam(required=false) String password, Model model, HttpSession session, HttpServletResponse res, HttpServletRequest req, RedirectAttributes rea){
		log.info("execute UserController leave");
		if(StringUtils.isEmpty(password)){
			rea.addFlashAttribute("message", message.USER_NO_PASSWORD);
			return "redirect:/mypage";
		}
		String id = (String)session.getAttribute("id");
		int idx = (int)session.getAttribute("idx");
		if(StringUtils.isEmpty(id) || IsValid.isNotValidInts(idx)){
			model.addAttribute("message", message.USER_NO_LOGIN);
			return "view/user/login";
		}
		User user = userService.loginCheck(id, password);
		if(IsValid.isNotValidObjects(user)){
			model.addAttribute("message", message.USER_NO_USER);
			return "view/user/login";
		}
		if(user.getIdx() != idx){
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
	    if(!userService.leave(user.getIdx(), loginId)){
	    	rea.addFlashAttribute("message", message.USER_FAIL_REMOVE);
			return "redirect:/mypage";
	    }
		session.setAttribute("loginYn", "N");
		session.removeAttribute("idx");
		session.removeAttribute("id");
		session.removeAttribute("name");
		session.removeAttribute("email");
	    res.addCookie(common.removeCookie("psvd"));
	    res.addCookie(common.removeCookie("psvlgnd"));
	    rea.addFlashAttribute("message", message.USER_SUCCESS_REMOVE);
		return "redirect:/";
	}
	
	@RequestMapping(value="/updateView", method=RequestMethod.POST)
	public String updateView(@RequestParam(required=false) String updatePassword, Model model, HttpSession session, RedirectAttributes rea){
		log.info("execute UserController updateView");
		int idx = (int)session.getAttribute("idx");
		if(IsValid.isNotValidInts(idx)){
			model.addAttribute("include", "/view/user/update.ftl");
			model.addAttribute("message", message.USER_FAIL_UPDATE);
			return "view/board/frame";
		}
		if(StringUtils.isEmpty(updatePassword) || !userService.passwordCheck(idx, updatePassword)){
			rea.addFlashAttribute("message", message.USER_NO_PASSWORD);
			return "redirect:/mypage";
		}
		model.addAttribute("id", session.getAttribute("id"));
		model.addAttribute("name", session.getAttribute("name"));
		model.addAttribute("email", session.getAttribute("email"));
		model.addAttribute("include", "/view/user/update.ftl");
		return "view/board/frame";
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(@RequestParam(required=false) MultipartFile ufile, User user, Model model, HttpSession session){
		log.info("execute UserController update");
		if(IsValid.isNotValidObjects(ufile)){
			model.addAttribute("message", message.FILE_FAIL_UPLOAD);
			return "view/user/join";
		}
		String[] strArray = ufile.getOriginalFilename().split("\\.");
		String ext = "";
		if(strArray.length == 2){
			ext = strArray[1];
		}
		String imgPath = "";
		String id = (String)session.getAttribute("id");
		String se = File.separator;
		if(StringUtils.equalsIgnoreCase(ext, "gif") || StringUtils.equalsIgnoreCase(ext, "jpg") || StringUtils.equalsIgnoreCase(ext, "jpeg") || StringUtils.equalsIgnoreCase(ext, "png")){
			imgPath = common.createImg(ufile, ext, id, se, "user");
		}
		if(IsValid.isNotValidObjects(user)){
			model.addAttribute("include", "/view/user/update.ftl");
			model.addAttribute("message", message.USER_FAIL_UPDATE);
			return "view/board/frame";
		}
		String name = user.getName();
		String email = user.getEmail();
		int idx = (int)session.getAttribute("idx");
		if(!common.isEmail(email)){
			model.addAttribute("include", "/view/user/update.ftl");
			model.addAttribute("message", message.USER_NO_EMAIL_FORMAT);
			return "view/board/frame";
		}
		if(StringUtils.isEmpty(name) || IsValid.isNotValidInts(idx)){
			model.addAttribute("include", "/view/user/update.ftl");
			model.addAttribute("message", message.USER_FAIL_UPDATE);
			return "view/board/frame";
		}
		name = common.cleanXss(name);
		email = common.cleanXss(email);
		if(StringUtils.isNotEmpty(ext)){
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
}
