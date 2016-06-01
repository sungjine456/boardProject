package kr.co.person.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.person.BoardProjectApplication;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	static final Logger log = LoggerFactory.getLogger(BoardProjectApplication.class);
    
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
    	String url = req.getRequestURI();
    	if(url.equals("/") || url.equals("/join") || url.equals("/idCheck") || url.equals("/emailCheck") || url.equals("/translatePassword")){
    		return true;
    	}
    	HttpSession session = req.getSession();
		if(session.getAttribute("loginYn") == null || session.getAttribute("loginYn").equals("N")){
			log.info("execute UserController no login");
			session.setAttribute("message", "로그인 후 이용해주세요.");
			res.sendRedirect("/");
			return false;
		}
		return true;
    }
}
