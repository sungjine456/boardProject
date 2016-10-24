package kr.co.person.controller;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import kr.co.person.common.CommonCookie;
import kr.co.person.common.CommonMail;
import kr.co.person.common.IsValid;
import kr.co.person.common.Message;
import kr.co.person.common.exception.EmptyStringException;
import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;
import kr.co.person.service.UserService;

@Controller
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired private CommonMail commonMail;
	@Autowired private UserService userService;
	@Autowired private Common common;
	@Autowired private CommonCookie commonCookie;
	@Autowired private Message message;
	
	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String joinView(HttpSession session){
		log.info("execute UserController joinView");
		if(common.sessionComparedToDB(session)){
			return "redirect:/board";
		}
		return "view/user/join";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(@IsValidUser User user, @RequestParam MultipartFile file, HttpSession session, RedirectAttributes rea){
		log.info("execute UserController join");
		String email = user.getEmail();
		OkCheck emailCheck = common.isEmail(email);
		if(!emailCheck.isBool()){
			rea.addFlashAttribute("message", emailCheck.getMessage());
			return "redirect:/join";
		}
		String imgPath = "";
		try {
			imgPath = common.createImg(file, user.getId(), "user");
		} catch (IOException e) {
			rea.addFlashAttribute("message", message.FILE_FAIL_UPLOAD);
			return "redirect:/join";
		}
		String se = File.separator;
		if(StringUtils.isEmpty(imgPath)){
			imgPath = "/img"+se+"user"+se+"default.png";
		}
		user.setImg(imgPath);
		OkCheck ok = userService.join(user);
		if(ok.isBool()){
			try {
				commonMail.sendMail(email, message.ACCESS_THANK_YOU_FOR_JOIN, "<a href='http://localhost:8080/emailAccess?access=" + commonCookie.aesEncode(email) + "'>동의</a>");
			} catch(EmptyStringException e) {
				rea.addFlashAttribute("message", message.USER_RE_EMAIL);
				return "redirect:/join";
			} catch(MessagingException e) {
				rea.addFlashAttribute("message", message.MAIL_FAIL_SEND);
				return "redirect:/join";
			} catch(Exception e){
				rea.addFlashAttribute("message", message.USER_RE_EMAIL);
				return "redirect:/join";
			}
			rea.addFlashAttribute("email", email);
			return "redirect:/emailAccessAgo";
		} else {
			rea.addFlashAttribute("message", ok.getMessage());
			return "redirect:/join";
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
		try {
			id = common.cleanXss(id);
		} catch(EmptyStringException e) {
			map.put("str", message.USER_NO_ID);
			map.put("bool", "false");
			
			return map;
		}
		OkCheck ok = userService.idCheck(id);
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
	public String loginView(HttpSession session, HttpServletRequest req){
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
					try {
						id = commonCookie.aesDecode(val);
					} catch (EmptyStringException e) {
						return "view/user/login";
					} catch (Exception e){
						return "view/user/join";
					}
				}
				if(StringUtils.equals("psvlgnd", key)){
					loginId = val;
				}
			}
		}
		User user = userService.findUserForId(id);
		if(IsValid.isValidObjects(user) && userService.autoLoginCheck(user, loginId)){
			session.setAttribute("loginYn", "Y");
			session.setAttribute("user", user);
			return "redirect:/board";
		}
		return "view/user/login";
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String login(@IsValidUser User user, @RequestParam(required=false) String idSave, HttpSession session, HttpServletResponse res, RedirectAttributes rea){
		log.info("execute UserController login");
		String id = user.getId();
		String password = user.getPassword();
		if(StringUtils.isEmpty(id)){
			rea.addFlashAttribute("message", message.USER_NO_ID);
			return "redirect:/";
		}
		if(StringUtils.isEmpty(password)){
			rea.addFlashAttribute("message", message.USER_NO_PASSWORD);
			return "redirect:/";
		}
		try {
			user = userService.joinCheck(common.cleanXss(id), password);
		} catch(EmptyStringException e) {
			rea.addFlashAttribute("message", message.USER_NO_ID);
			return "redirect:/";
		}
		if(IsValid.isValidUser(user)){
			if(StringUtils.equals(user.getAccess(), "N")){
				rea.addFlashAttribute("email", user.getEmail());
				return "redirect:/emailAccessAgo";
			}
			session.setAttribute("loginYn", "Y");
			session.setAttribute("user", user);
			if(StringUtils.equals(idSave, "ckeck")){
				String loginId = "";
				String enKeyId = "";
				try {
					loginId = common.cookieValueEncryption(new DateTime().toString());
					enKeyId = commonCookie.aesEncode(id);
				} catch(EmptyStringException e) {
					rea.addFlashAttribute("message", message.USER_FAIL_LOGIN);
					return "redirect:/";
				} catch(NoSuchAlgorithmException e) {
					rea.addFlashAttribute("message", message.USER_FAIL_LOGIN);
					return "redirect:/";
				} catch (Exception e){
					rea.addFlashAttribute("message", message.USER_FAIL_LOGIN);
					return "redirect:/";
				}
				if(!userService.autoLogin(user, loginId)){
					rea.addFlashAttribute("message", message.USER_FAIL_LOGIN);
					return "redirect:/";
				}
			    res.addCookie(common.addCookie("psvd", enKeyId));
			    res.addCookie(common.addCookie("psvlgnd", loginId));
			}
			return "redirect:/board";
		} else {
			rea.addFlashAttribute("message", message.USER_WRONG_ID_OR_WRONG_PASSWORD);
			return "redirect:/";
		}
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(HttpSession session, HttpServletRequest req, HttpServletResponse res, RedirectAttributes rea){
		log.info("execute UserController logout");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		User user = (User)session.getAttribute("user");
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
		session.removeAttribute("user");
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
			try {
				commonMail.sendMail(email, message.MAIL_TRANSLATE_PASSWORD_TITLE, ok.getMessage());
			} catch (MessagingException e) {
				rea.addFlashAttribute("message", message.MAIL_FAIL_SEND);
				return "redirect:/";
			}
			rea.addFlashAttribute("message", message.MAIL_SUCCESS_TRANSLATE_PASSWORD);
		} else {
			rea.addFlashAttribute("message", ok.getMessage());
		}
		return "redirect:/";
	}
	
	@RequestMapping(value="/mypage", method=RequestMethod.GET)
	public String myPageView(Model model, HttpSession session, RedirectAttributes rea){
		log.info("execute UserController mypageView");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		model.addAttribute("include", "/view/user/mypage.ftl");
		return "view/frame";
	}
	
	@RequestMapping(value="/changePassword", method=RequestMethod.POST)
	public String changePassword(@RequestParam(required=false) String password, @RequestParam(required=false) String changePassword, HttpSession session, RedirectAttributes rea){
		log.info("execute UserController changePassword");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		if(StringUtils.isEmpty(password)){
			rea.addFlashAttribute("message", message.USER_NO_PASSWORD);
			return "redirect:/mypage";
		}
		if(StringUtils.isEmpty(changePassword)){
			rea.addFlashAttribute("message", message.USER_NO_UPDATE_PASSWORD);
			return "redirect:/mypage";
		}
		if(password.equals(changePassword)){
			rea.addFlashAttribute("message", message.USER_PASSWORD_SAME_UPDATE_PASSWORD);
			return "redirect:/mypage";
		}
		OkCheck ok = userService.changePassword(((User)session.getAttribute("user")).getIdx(), password, changePassword);
		rea.addFlashAttribute("message", ok.getMessage());
		return "redirect:/mypage";
	}
	
	@RequestMapping(value="/leave")
	public String leave(@RequestParam(required=false) String password, HttpSession session, HttpServletResponse res, HttpServletRequest req, RedirectAttributes rea){
		log.info("execute UserController leave");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		if(StringUtils.isEmpty(password)){
			rea.addFlashAttribute("message", message.USER_NO_PASSWORD);
			return "redirect:/mypage";
		}
		User user = (User)session.getAttribute("user");
		int idx = user.getIdx();
		User joinCheck = userService.joinCheck(user.getId(), password);
		if(IsValid.isNotValidUser(joinCheck) || joinCheck.getIdx() != idx){
			rea.addFlashAttribute("message", message.USER_WRONG_USER);
			return "redirect:/";
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
	    if(!userService.leave(idx, loginId)){
	    	rea.addFlashAttribute("message", message.USER_FAIL_LEAVE);
			return "redirect:/mypage";
	    }
		session.setAttribute("loginYn", "N");
		session.removeAttribute("user");
	    res.addCookie(common.removeCookie("psvd"));
	    res.addCookie(common.removeCookie("psvlgnd"));
	    rea.addFlashAttribute("message", message.USER_SUCCESS_LEAVE);
		return "redirect:/";
	}
	
	@RequestMapping(value="/updateView", method=RequestMethod.POST)
	public String updateView(@RequestParam(required=false) String password, Model model, HttpSession session, RedirectAttributes rea){
		log.info("execute UserController updateView");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		if(StringUtils.isEmpty(password) 
				|| !userService.passwordCheck(((User)session.getAttribute("user")).getIdx(), password)){
			rea.addFlashAttribute("message", message.USER_NO_PASSWORD);
			return "redirect:/mypage";
		}
		model.addAttribute("include", "/view/user/update.ftl");
		return "view/frame";
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(@RequestParam MultipartFile ufile, @IsValidUser User updateUser, HttpSession session, RedirectAttributes rea){
		log.info("execute UserController update");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		if(IsValid.isNotValidUser(updateUser)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		String imgPath = "";
		try {
			imgPath = common.createImg(ufile, updateUser.getId(), "user");
		} catch (IOException e) {
			rea.addFlashAttribute("message", message.FILE_FAIL_UPLOAD);
			return "redirect:/";
		}
		String se = File.separator;
		if(StringUtils.isEmpty(imgPath)){
			imgPath = "/img"+se+"user"+se+"default.png";
		}
		String name = updateUser.getName();
		String email = updateUser.getEmail();
		
		OkCheck emailCheck = common.isEmail(email);
		if(!emailCheck.isBool()){
			rea.addFlashAttribute("message", emailCheck.getMessage());
			return "redirect:/mypage";
		}
		if(StringUtils.isEmpty(name)){
			rea.addFlashAttribute("message", message.USER_NO_NAME);
			return "redirect:/mypage";
		}
		try {
			name = common.cleanXss(name);
		} catch(EmptyStringException e) {
			rea.addFlashAttribute("message", message.USER_NO_NAME);
			return "redirect:/mypage";
		}
		User user = (User)session.getAttribute("user");
		int idx = user.getIdx();
		user.setName(name);
		user.setEmail(email);
		boolean isSuccessUpdate = false;
		if(ufile.getSize() != 0){
			if(userService.update(idx, name, email, imgPath)){
				isSuccessUpdate = true;
				user.setImg(imgPath);
			}
		} else {
			if(userService.update(idx, name, email)){
				isSuccessUpdate = true;
			}
		}
		if(isSuccessUpdate){
			rea.addFlashAttribute("message", message.USER_SUCCESS_UPDATE);
		} else {
			rea.addFlashAttribute("message", message.USER_FAIL_UPDATE);
		}
		return "redirect:/mypage";
	}
	
	@RequestMapping(value="/emailAccess", method=RequestMethod.GET)
	public String emailAccess(@RequestParam(required=false) String access, RedirectAttributes rea, HttpSession session){
		log.info("execute UserController emailAccess");
		if(StringUtils.isEmpty(access)){
			rea.addFlashAttribute("message", message.ACCESS_FAIL_ACCESS);
			return "redirect:/";
		}
		String email = "";
		try {
			email = commonCookie.aesDecode(access);
		} catch (EmptyStringException e) {
			rea.addFlashAttribute("message", message.ACCESS_FAIL_ACCESS);
			return "redirect:/";
		} catch (Exception e){
			rea.addFlashAttribute("message", message.ACCESS_FAIL_ACCESS);
			return "redirect:/";
		}
		User user = userService.findUserForEmail(email);
		if(IsValid.isNotValidUser(user)){
			rea.addFlashAttribute("message", message.ACCESS_FAIL_ACCESS);
			return "redirect:/";
		}
		if(StringUtils.equals(user.getAccess(), "Y")){
			session.setAttribute("loginYn", "Y");
			session.setAttribute("user", user);
			return "redirect:/board";
		}
		user = userService.accessEmail(email);
		if(IsValid.isNotValidUser(user)){
			rea.addFlashAttribute("message", message.ACCESS_FAIL_ACCESS);
			return "redirect:/";
		}
		session.setAttribute("loginYn", "Y");
		session.setAttribute("user", user);
		rea.addFlashAttribute("message", message.ACCESS_THANK_YOU_FOR_AGREE);
		return "redirect:/board";
	}
	
	@RequestMapping(value="/emailAccessAgo", method=RequestMethod.GET)
	public String emailAccessAgo(){
		log.info("execute UserController emailAccessAgo");
		return "view/user/emailAccessAgo";
	}
	
	@RequestMapping(value="/emailAccessRe", method=RequestMethod.POST)
	public String reEmailAccess(@RequestParam(required=false) String email, RedirectAttributes rea){
		log.info("execute UserController reEmailAccess");
		OkCheck ok = common.isEmail(email);
		if(!ok.isBool()){
			rea.addFlashAttribute("message", message.USER_NO_EMAIL);
			return "redirect:/";
		}
		try {
			commonMail.sendMail(email, message.ACCESS_THANK_YOU_FOR_JOIN, "<a href='http://localhost:8080/emailAccess?access=" + commonCookie.aesEncode(email) + "'>동의</a>");
		} catch(EmptyStringException e) {
			rea.addFlashAttribute("message", message.USER_NO_EMAIL);
			return "redirect:/";
		} catch(MessagingException e){
			rea.addFlashAttribute("message", message.MAIL_FAIL_SEND);
			return "redirect:/";
		} catch(Exception e){
			rea.addFlashAttribute("message", message.USER_RE_EMAIL);
			return "redirect:/";
		}
		return "redirect:/emailAccessAgo";
	}
	
	@RequestMapping("/interceptorView")
	public String interceptorView(){
		log.info("execute UserController interceptorView");
		return "common/interceptorPage";
	}
}
