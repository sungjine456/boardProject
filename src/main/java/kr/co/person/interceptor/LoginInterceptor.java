package kr.co.person.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
	static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
    	log.info("LoginInterceptor excute");
    	String url = req.getRequestURI();
    	if(url != null && (url.equals("/test") || url.equals("/") || url.matches("/join[/a-zA-Z]{0,15}") || url.equals("/translatePassword") || url.equals("/interceptorView") || url.matches("/error[0-9]{0,3}")) || url.matches("/emailAccess[a-zA-Z]{0,5}")){
    		log.info("LoginInterceptor no login required excute");
    		return true;
    	}
    	HttpSession session = req.getSession();
		if(session.getAttribute("loginYn") != null && session.getAttribute("loginYn").equals("Y")){
			log.info("LoginInterceptor login required excute");
			return true;
		}
		res.sendRedirect("/interceptorView");
		return false;
    }
}
