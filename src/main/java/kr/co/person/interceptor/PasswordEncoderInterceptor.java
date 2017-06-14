package kr.co.person.interceptor;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.person.common.Common;
import kr.co.person.common.exception.EmptyStringException;
import kr.co.person.pojo.OkObjectCheck;

@Component
public class PasswordEncoderInterceptor extends HandlerInterceptorAdapter {
	static final Logger log = LoggerFactory.getLogger(PasswordEncoderInterceptor.class);
	@Autowired private Common common;
    
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
    	log.info("PasswordEncoderInterceptor excute");
    	String url = req.getRequestURI();
    	if(url != null && (url.equals("/join") || url.equals("/") || url.equals("/leave") || url.equals("/changePassword") || url.equals("/updateView"))){
    		String[] parameters = req.getParameterMap().get("password");
    		if(parameters != null){
    			try {
    				req.setAttribute("password", new OkObjectCheck<String>(common.passwordEncryption(parameters[0]), "", true));
    			} catch(EmptyStringException e) {
    				req.setAttribute("password", new OkObjectCheck<String>(common.passwordEncryption(parameters[0]), "비밀번호를 다시 입력해주세요.", false));
    			} catch(NoSuchAlgorithmException e) {
    				req.setAttribute("password", new OkObjectCheck<String>(common.passwordEncryption(parameters[0]), "비밀번호를 다시 입력해주세요.", false));
    			}
    		} else {
    			req.setAttribute("password", new OkObjectCheck<String>("", "비밀번호를 입력해주세요.", false));
    		}
    	}
    	if(url != null && (url.equals("/changePassword"))){
    		String[] parameters = req.getParameterMap().get("changePassword");
    		if(parameters != null){
    			try {
    				req.setAttribute("changePassword", new OkObjectCheck<String>(common.passwordEncryption(parameters[0]), "", true));
    			} catch(EmptyStringException e) {
    				req.setAttribute("changePassword", new OkObjectCheck<String>(common.passwordEncryption(parameters[0]), "수정할 비밀번호를 다시 입력해주세요.", false));
    			} catch(NoSuchAlgorithmException e) {
    				req.setAttribute("changePassword", new OkObjectCheck<String>(common.passwordEncryption(parameters[0]), "수정할 비밀번호를 다시 입력해주세요.", false));
    			}
    		} else {
    			req.setAttribute("changePassword", new OkObjectCheck<String>("", "수정할 비밀번호를 입력해주세요.", false));
    		}
    	}
    	
    	return true;
    }
}
