package kr.co.person.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.person.common.IsValid;
import kr.co.person.domain.User;
import kr.co.person.service.UserService;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired private UserService userService;
	static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
    	log.info("interceptor excute");
    	String url = req.getRequestURI();
    	if(url.equals("/testest")){
    		return true;
    	}
    	if(url != null && (url.equals("/") || url.matches("/join[/a-zA-Z]{0,15}") || url.equals("/translatePassword") || url.equals("/interceptorView") || url.matches("/error[0-9]{0,3}"))){
    		log.info("interceptor no login required excute");
    		return true;
    	}
    	HttpSession session = req.getSession();
		if(session.getAttribute("loginYn") == null || session.getAttribute("loginYn").equals("N")){
			log.info("interceptor login required excute");
			res.sendRedirect("/interceptorView");
			return false;
		}
		int idx = (int)session.getAttribute("idx");
		String id = (String)session.getAttribute("id");
		String name = (String)session.getAttribute("name");
		String email = (String)session.getAttribute("email");
		User user = userService.findUserForIdx(idx);
		if(IsValid.isNotValidObjects(user) || !StringUtils.equals(id, user.getId())
				|| !StringUtils.equals(name, user.getName()) || !StringUtils.equals(email, user.getEmail())){
			log.info("interceptor login no metch user excute");
			res.sendRedirect("/interceptorView");
			return false;
		}
		return true;
    }
}
