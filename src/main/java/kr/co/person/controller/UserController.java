package kr.co.person.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
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
import kr.co.person.pojo.OkObjectCheck;
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
	public String joinView(){
		log.info("execute UserController joinView");
		return "view/user/join";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(@IsValidUser User user, @RequestParam MultipartFile file, HttpServletRequest req, RedirectAttributes rea){
		log.info("execute UserController join");
		String id = common.cleanXss(user.getId());
		@SuppressWarnings("unchecked")
		OkObjectCheck<String> passwordCheck = (OkObjectCheck<String>)req.getAttribute("password");
		String name = common.cleanXss(user.getName());
		String email = user.getEmail();
		OkCheck emailCheck = common.isEmail(email);
		if(!emailCheck.isBool()){
			rea.addFlashAttribute("message", emailCheck.getMessage());
			return "redirect:/join";
		}
		if(StringUtils.isEmpty(id)){
			rea.addFlashAttribute("message", message.USER_WRONG_ID);
			return "redirect:/join";
		}
		if(StringUtils.isEmpty(name)){
			rea.addFlashAttribute("message", message.USER_NO_NAME);
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
		user.setId(id);
		user.setName(name);
		user.setPassword(passwordCheck.getObject());
		user.setEmail(email);
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
			rea.addFlashAttribute("message", ok.getMessage());
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
		id = common.cleanXss(id);
		if(StringUtils.isEmpty(id)){
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
		if(common.sessionComparedToDB(session)){
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
						return "view/user/login";
					}
				}
				if(StringUtils.equals("psvlgnd", key)){
					try {
						loginId = URLDecoder.decode(val, "UTF-8");
					} catch (UnsupportedEncodingException uee) {
						return "view/user/login";
					}
				}
			}
		}
		OkObjectCheck<User> ouc = userService.findUserForId(id);
		User user = ouc.getObject();
		if(ouc.isBool() && userService.autoLoginCheck(user, loginId)){
			session.setAttribute("loginYn", "Y");
			session.setAttribute("user", user);
			return "redirect:/board";
		}
		return "view/user/login";
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String login(@RequestParam(required=false) String id, @RequestParam(required=false) String idSave, HttpSession session, HttpServletRequest req, HttpServletResponse res, RedirectAttributes rea){
		log.info("execute UserController login");
		@SuppressWarnings("unchecked")
		OkObjectCheck<String> passwordCheck = (OkObjectCheck<String>)req.getAttribute("password");
		if(StringUtils.isEmpty(id)){
			rea.addFlashAttribute("message", message.USER_NO_ID);
			return "redirect:/";
		}
		if(!passwordCheck.isBool()){
			rea.addFlashAttribute("message", passwordCheck.getMessage());
			return "redirect:/";
		}
		id = common.cleanXss(id);
		if(StringUtils.isEmpty(id)){
			rea.addFlashAttribute("message", message.USER_NO_ID);
			return "redirect:/";			
		}
		OkObjectCheck<User> ouc = userService.confirmUserPassword(id, passwordCheck.getObject());
		if(!ouc.isBool()){
			rea.addFlashAttribute("message", ouc.getMessage());
			return "redirect:/";
		}
		User user = ouc.getObject();
		
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
					loginId = common.cookieValueEncryption(LocalDateTime.now().toString());
					enKeyId = commonCookie.aesEncode(id);
					res.addCookie(common.addCookie("psvd", enKeyId));
				    res.addCookie(common.addCookie("psvlgnd", loginId));
				} catch(EmptyStringException e) {
					rea.addFlashAttribute("message", message.USER_FAIL_LOGIN);
					return "redirect:/";
				} catch(NoSuchAlgorithmException e) {
					rea.addFlashAttribute("message", message.USER_FAIL_LOGIN);
					return "redirect:/";
				} catch (UnsupportedEncodingException uee){
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
				if(StringUtils.equals("psvlgnd", key)){
					try {
						loginId = URLDecoder.decode(cookies[i].getValue(), "UTF-8");
					} catch (UnsupportedEncodingException uee) {
						return "redirect:/";
					}
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
		OkCheck okEmail = common.isEmail(email);
		if(!okEmail.isBool()){
			rea.addFlashAttribute("message", okEmail.getMessage());
			return "redirect:/";
		}
		OkCheck okPassword = userService.translatePassword(email);
		if(okPassword.isBool()){
			try {
				commonMail.sendMail(email, message.MAIL_TRANSLATE_PASSWORD_TITLE, okPassword.getMessage());
			} catch (MessagingException e) {
				rea.addFlashAttribute("message", message.MAIL_FAIL_SEND);
				return "redirect:/";
			}
			rea.addFlashAttribute("message", message.MAIL_SUCCESS_TRANSLATE_PASSWORD);
		} else {
			rea.addFlashAttribute("message", okPassword.getMessage());
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
	public String changePassword(HttpSession session, HttpServletRequest req, RedirectAttributes rea){
		log.info("execute UserController changePassword");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		@SuppressWarnings("unchecked")
		OkObjectCheck<String> passwordCheck = (OkObjectCheck<String>)req.getAttribute("password");
		@SuppressWarnings("unchecked")
		OkObjectCheck<String> changePasswordCheck = (OkObjectCheck<String>)req.getAttribute("changePassword");
		if(!passwordCheck.isBool()){
			rea.addFlashAttribute("message", passwordCheck.getMessage());
			return "redirect:/mypage";
		}
		if(!changePasswordCheck.isBool()){
			rea.addFlashAttribute("message", changePasswordCheck.getMessage());
			return "redirect:/mypage";
		}
		if(passwordCheck.getObject().equals(changePasswordCheck.getObject())){
			rea.addFlashAttribute("message", message.USER_PASSWORD_SAME_UPDATE_PASSWORD);
			return "redirect:/mypage";
		}
		OkCheck ok = userService.changePassword(((User)session.getAttribute("user")).getIdx(), passwordCheck.getObject(), changePasswordCheck.getObject());
		rea.addFlashAttribute("message", ok.getMessage());
		return "redirect:/mypage";
	}
	
	@RequestMapping(value="/leave")
	public String leave(HttpSession session, HttpServletResponse res, HttpServletRequest req, RedirectAttributes rea){
		log.info("execute UserController leave");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		@SuppressWarnings("unchecked")
		OkObjectCheck<String> passwordCheck = (OkObjectCheck<String>)req.getAttribute("password");
		if(!passwordCheck.isBool()){
			rea.addFlashAttribute("message", passwordCheck.getMessage());
			return "redirect:/mypage";
		}
		User user = (User)session.getAttribute("user");
		int idx = user.getIdx();
		OkObjectCheck<User> ouc = userService.confirmUserPassword(user.getId(), passwordCheck.getObject());
		if(!ouc.isBool()){
			rea.addFlashAttribute("message", ouc.getMessage());
			return "redirect:/";
		}
		if(ouc.getObject().getIdx() != idx){
			rea.addFlashAttribute("message", message.USER_WRONG_USER);
			return "redirect:/";
		}
		String loginId = "";
		Cookie[] cookies = req.getCookies();
		if(IsValid.isValidArrays(cookies)){
			for(int i = 0; i < cookies.length; i++){
				String key = cookies[i].getName();
				if(StringUtils.equals("psvlgnd", key)){
					try {
						loginId = URLDecoder.decode(cookies[i].getValue(), "UTF-8");
					} catch (UnsupportedEncodingException uee) {
						return "redirect:/";
					}
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
	public String updateView(Model model, HttpSession session, HttpServletRequest req, RedirectAttributes rea){
		log.info("execute UserController updateView");
		if(!common.sessionComparedToDB(session)){
			rea.addFlashAttribute("message", message.USER_NO_LOGIN);
			return "redirect:/";
		}
		@SuppressWarnings("unchecked")
		OkObjectCheck<String> passwordCheck = (OkObjectCheck<String>)req.getAttribute("password");
		if(!passwordCheck.isBool()){
			rea.addFlashAttribute("message", passwordCheck.getMessage());
			return "redirect:/mypage";
		}
		if(!userService.passwordCheck(((User)session.getAttribute("user")).getIdx(), passwordCheck.getObject())){
			rea.addFlashAttribute("message", message.USER_RE_PASSWORD);
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
		String id = updateUser.getId();
		if(StringUtils.isEmpty(id)){
			rea.addFlashAttribute("message", message.USER_FAIL_UPDATE);
			return "redirect:/";
		}
		String imgPath = "";
		try {
			imgPath = common.createImg(ufile, id, "user");
		} catch (IOException e) {
			rea.addFlashAttribute("message", message.FILE_FAIL_UPLOAD);
			return "redirect:/mypage";
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
		name = common.cleanXss(name);
		if(StringUtils.isEmpty(name)){
			rea.addFlashAttribute("message", message.USER_NO_NAME);
			return "redirect:/mypage";
		}
		User user = (User)session.getAttribute("user");
		user.setName(name);
		user.setEmail(email);
		user.setImg(imgPath);
		OkObjectCheck<User> ok = userService.update(user);
		if(ok.isBool()){
			session.setAttribute("user", ok.getObject());
			rea.addFlashAttribute("message", ok.getMessage());
		} else {
			rea.addFlashAttribute("message", ok.getMessage());
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
		OkObjectCheck<User> findUserForEmail = userService.findUserForEmail(email);
		if(!findUserForEmail.isBool()){
			rea.addFlashAttribute("message", findUserForEmail.getMessage());
			return "redirect:/";
		}
		if(StringUtils.equals(findUserForEmail.getObject().getAccess(), "Y")){
			session.setAttribute("loginYn", "Y");
			session.setAttribute("user", findUserForEmail.getObject());
			return "redirect:/board";
		}
		OkObjectCheck<User> accessEmail = userService.accessEmail(email);
		if(!accessEmail.isBool()){
			rea.addFlashAttribute("message", accessEmail.getMessage());
			return "redirect:/";
		}
		session.setAttribute("loginYn", "Y");
		session.setAttribute("user", accessEmail.getObject());
		rea.addFlashAttribute("message", accessEmail.getMessage());
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
