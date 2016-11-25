package kr.co.person.interceptor;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.person.common.Common;
import kr.co.person.common.exception.EmptyStringException;

public class PasswordEncoderInterceptor extends HandlerInterceptorAdapter {
	static final Logger log = LoggerFactory.getLogger(PasswordEncoderInterceptor.class);
	private Common common = new Common();
    
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
    	log.info("PasswordEncoderInterceptor excute");
    	String url = req.getRequestURI();
    	if(url != null && (url.equals("/join") || url.equals("/") || url.equals("/leave") || url.equals("/changePassword") || url.equals("/updateView"))){
    		String[] parameters = req.getParameterMap().get("password");
    		if(parameters != null){
    			try {
    				req.setAttribute("password", common.passwordEncryption(parameters[0]));
    			} catch(EmptyStringException e) {
    				req.setAttribute("message", "비밀번호를 다시 입력해주세요.");
    			} catch(NoSuchAlgorithmException e) {
    				req.setAttribute("message", "비밀번호를 다시 입력해주세요.");
    			}
    		}
    	}
    	if(url != null && (url.equals("/changePassword"))){
    		String[] parameters = req.getParameterMap().get("changePassword");
    		if(parameters != null){
    			try {
    				req.setAttribute("changePassword", common.passwordEncryption(parameters[0]));
    			} catch(EmptyStringException e) {
    				req.setAttribute("message", "비밀번호를 다시 입력해주세요.");
    			} catch(NoSuchAlgorithmException e) {
    				req.setAttribute("message", "비밀번호를 다시 입력해주세요.");
    			}
    		}
    	}
    	
    	return true;
    }
}
