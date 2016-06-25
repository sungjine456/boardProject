package kr.co.person.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
    	log.info("interceptor excute");
    	String url = req.getRequestURI();
    	log.info("interceptor url : " + url);
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
		return true;
    }
}
