package kr.co.person.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.person.common.Common;
import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;
import kr.co.person.service.UserService;

@Controller
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Value("${keyValue}")
	private String ENCRYPTION_KEY;
	@Autowired private UserService userService;
	@Autowired private Common common;
	
	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String joinView(){
		log.info("execute UserController addUserView");
		return "view/user/join";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(@ModelAttribute User user, @RequestParam(required=false) MultipartFile file, Model model, HttpSession session){
		log.info("execute UserController addUser");
		if(user == null){
			model.addAttribute("message", "회원가입에 실패하셨습니다.");
			return "view/user/join";
		}
		if(file == null){
			model.addAttribute("message", "회원가입에 실패하셨습니다.");
			return "view/user/join";
		}
		StringTokenizer st = new StringTokenizer(file.getOriginalFilename(), ".");
		String ext = "";
		if(st.countTokens() == 2){
			st.nextToken();
			ext = st.nextToken();
		}
		String fileName = "";
		String se = File.separator;
		log.info("execute UserController addUser ext : " + ext);
		if(StringUtils.equalsIgnoreCase(ext, "gif") || StringUtils.equalsIgnoreCase(ext, "jpg") || StringUtils.equalsIgnoreCase(ext, "jpeg") || StringUtils.equalsIgnoreCase(ext, "png")){
			Date date = new Date();
			fileName = user.getId() + "_"  + date.getTime() + "." + ext;
			String filePath = "D:"+se+"git"+se+"boardProject"+se+"boardProject"+se+"src"+se+"main"+se+"resources"+se+"static"+se+"img"+se+"user";
		    File dayFile = new File(filePath);
		    if(!dayFile.exists()){
		       dayFile.mkdirs();
		    }
		    try(FileOutputStream fos = new FileOutputStream(filePath + se + fileName)){
	            byte fileData[] = file.getBytes();
	            fos.write(fileData);
	        }catch(Exception e){
	            e.printStackTrace();
	        }
		}
		if(StringUtils.isEmpty(fileName)){
			fileName = "default.png";
		}
		log.info("execute UserController addUser fileName : " + fileName);
		
		user.setImg("img"+se+"user"+se+fileName);
		OkCheck ok = userService.join(user);
		model.addAttribute("message", ok.getMessage());
		if(ok.isBool()){
			session.setMaxInactiveInterval(24*60);
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
	public @ResponseBody Map<String, String> emailCheck(@RequestParam(required=false) String email){
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
	public String loginView(Model model, HttpSession session, HttpServletRequest req, RedirectAttributes rea){
		log.info("execute UserController loginView");
		if(rea.getFlashAttributes().get("message") != null){
			log.info("execute UserController getMessage");
			model.addAttribute("message", rea.getFlashAttributes().get("message"));
			return "view/user/login";
		}
		log.info("execute UserController no message");
		if(session.getAttribute("loginYn") != null && session.getAttribute("loginYn").equals("Y")){
			return "redirect:/board";
		}
		String id = "";
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
		if(user != null && userService.autoLoginCheck(user, req.getRemoteAddr())){
			session.setMaxInactiveInterval(24*60);
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
	public String login(@ModelAttribute User user, @RequestParam(required=false) String idSave, Model model, HttpSession session, HttpServletRequest req, HttpServletResponse res, RedirectAttributes rea){
		log.info("execute UserController login");
		String bool = (String)req.getAttribute("false");
		if(StringUtils.equals(bool, "false")){
			rea.addFlashAttribute("message", req.getAttribute("message"));
			return "redirect:/";
		}
		if(user == null){
			model.addAttribute("message", "로그인에 실패하셨습니다.");
			return "view/user/login";
		}
		String id = common.cleanXss(user.getId());
		user = userService.findUserForId(id);
		if(user != null){
			session.setMaxInactiveInterval(24*60);
			session.setAttribute("loginYn", "Y");
			session.setAttribute("idx", user.getIdx());
			session.setAttribute("id", user.getId());
			session.setAttribute("name", user.getName());
			session.setAttribute("email", user.getEmail());
			session.setAttribute("img", user.getImg());
			if(idSave != null && idSave.equals("check")){
				String ip = req.getRemoteAddr();
				if(!userService.autoLogin(user, ip)){
					model.addAttribute("message", "로그인에 실패하셨습니다.");
					return "view/user/login";
				}
				String enKeyId = common.cookieAesEncode(ENCRYPTION_KEY, id);
				if(StringUtils.isEmpty(enKeyId)){
					model.addAttribute("message", "로그인에 실패하셨습니다.");
					return "view/user/login";
				}
				Cookie cookie = new Cookie("saveId", enKeyId);
			    cookie.setMaxAge(60*60*24);
			    res.addCookie(cookie);
			}
			return "redirect:/board";
		} else {
			model.addAttribute("message", "로그인에 실패하셨습니다.");
			return "view/user/login";
		}
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(HttpSession session, HttpServletRequest req, HttpServletResponse res, RedirectAttributes rea){
		String url = req.getRequestURI();
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
	public String translatePassword(@RequestParam(required=false) String email, RedirectAttributes rea){
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
		log.info("execute UserController mypageView");
		if(StringUtils.isEmpty(password)){
			rea.addFlashAttribute("message", "페스워드를 입력해주세요");
			return "redirect:/update";
		}
		if(StringUtils.isEmpty(changePassword)){
			rea.addFlashAttribute("message", "수정할 페스워드를 입력해주세요");
			return "redirect:/update";
		}
		int idx = (int)session.getAttribute("idx");
		User user = userService.findUserForIdx(idx);
		model.addAttribute("id", user.getId());
		model.addAttribute("name", user.getName());
		model.addAttribute("email", user.getEmail());
		
		OkCheck ok = userService.changePassword(idx, password, changePassword);
		rea.addFlashAttribute("message", ok.getMessage());
		return "redirect:/mypage";
	}
	
	@RequestMapping(value="/leave")
	public String leave(@RequestParam(required=false) String password, Model model, HttpSession session, HttpServletRequest req, HttpServletResponse res, RedirectAttributes rea){
		log.info("execute UserController leave");
		if(StringUtils.isEmpty(password)){
			rea.addFlashAttribute("message", "password를 입력해주세요.");
			return "redirect:/mypage";
		}
		User user = userService.loginCheck((String)session.getAttribute("id"), password);
		if(user == null || user.getIdx() != (int)session.getAttribute("idx")){
			rea.addFlashAttribute("message", "패스워드를 다시입력해주세요.");
			return "redirect:/";
		}
		String ip = req.getRemoteAddr();
	    if(!userService.leave(user.getIdx(), ip)){
	    	rea.addFlashAttribute("message", "탈퇴에 실패하셨습니다.");
			return "redirect:/mypage";
	    }
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
	}
	
	@RequestMapping(value="/updateView", method=RequestMethod.POST)
	public String updateView(@RequestParam(required=false) String updatePassword, Model model, HttpSession session, RedirectAttributes rea){
		log.info("execute UserController mypageView");
		if(StringUtils.isEmpty(updatePassword)){
			rea.addFlashAttribute("message", "패스워드를 입력해주세요.");
			return "redirect:/mypage";
		}
		int idx = (int)session.getAttribute("idx");
		if(!userService.passwordCheck(idx, updatePassword)){
			rea.addFlashAttribute("message", "패스워드를 입력해주세요.");
			return "redirect:/mypage";
		}
		model.addAttribute("id", session.getAttribute("id"));
		model.addAttribute("name", session.getAttribute("name"));
		model.addAttribute("email", session.getAttribute("email"));
		model.addAttribute("message", rea.getFlashAttributes().get("message"));
		model.addAttribute("include", "/view/user/update.ftl");
		return "view/board/frame";
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(@RequestParam(required=false) MultipartFile ufile, @RequestParam(required=false) String name, @RequestParam(required=false) String email, Model model, HttpSession session){
		log.info("execute UserController update");
		if(ufile == null){
			model.addAttribute("message", "회원가입에 실패하셨습니다.");
			return "view/user/join";
		}
		StringTokenizer st = new StringTokenizer(ufile.getOriginalFilename(), ".");
		log.info("execute UserController update file orgFileName : " + ufile.getOriginalFilename());
		String ext = "";
		if(st.countTokens() == 2){
			st.nextToken();
			ext = st.nextToken();
		}
		String fileName = "";
		String id = (String)session.getAttribute("id");
		String se = File.separator;
		log.info("execute UserController update ext : " + ext);
		if(StringUtils.equalsIgnoreCase(ext, "gif") || StringUtils.equalsIgnoreCase(ext, "jpg") || StringUtils.equalsIgnoreCase(ext, "jpeg") || StringUtils.equalsIgnoreCase(ext, "png")){
			Date date = new Date();
			fileName = id + "_"  + date.getTime() + "." + ext;
			String filePath = "D:"+se+"git"+se+"boardProject"+se+"boardProject"+se+"src"+se+"main"+se+"resources"+se+"static"+se+"img"+se+"user";
		    File dayFile = new File(filePath);
		    if(!dayFile.exists()){
		       dayFile.mkdirs();
		    }
		    try(FileOutputStream fos = new FileOutputStream(filePath + se + fileName)){
	            byte fileData[] = ufile.getBytes();
	            fos.write(fileData);
	        }catch(Exception e){
	            e.printStackTrace();
	        }
		}
		if(StringUtils.isEmpty(name)){
			model.addAttribute("include", "/view/user/update.ftl");
			model.addAttribute("message", "회원정보를 수정에 실패 하셨습니다.");
		}
		if(StringUtils.isEmpty(email)){
			model.addAttribute("include", "/view/user/update.ftl");
			model.addAttribute("message", "회원정보를 수정에 실패 하셨습니다.");
		}
		name = common.cleanXss(name);
		email = common.cleanXss(email);
		int idx = (int)session.getAttribute("idx");
		if(idx == 0){
			model.addAttribute("include", "/view/user/update.ftl");
			model.addAttribute("message", "회원정보를 수정에 실패 하셨습니다.");
		}
		if(!ext.equals("")){
			if(userService.update(idx, name, email, fileName)){
				session.setAttribute("name", name);
				session.setAttribute("email", email);
				session.setAttribute("img", "img"+se+"user"+se+fileName);
				model.addAttribute("include", "/view/user/mypage.ftl");
				model.addAttribute("message", "회원정보를 수정 하셨습니다.");
			}
		} else {
			if(userService.update(idx, name, email)){
				session.setAttribute("name", name);
				session.setAttribute("email", email);
				model.addAttribute("include", "/view/user/mypage.ftl");
				model.addAttribute("message", "회원정보를 수정 하셨습니다.");
			}
		}
		return "view/board/frame";
	}
	
	@RequestMapping("/interceptorView")
	public String interceptorView(){
		return "common/interceptorPage";
	}
}
