package kr.co.person.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.person.BoardProjectApplication;
import kr.co.person.common.Common;
import kr.co.person.domain.User;
import kr.co.person.repository.UserRepository;
import kr.co.person.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	static final Logger log = LoggerFactory.getLogger(BoardProjectApplication.class);
	
	@Autowired UserRepository userRepository;
	@Autowired Common common;
	
	@Override
	public String join(User user){
		log.info("execute UserService create");
		if(user == null){
			return "회원가입에 실패하셨습니다.";
		}
		String id = user.getId();
		String password = user.getPassword();
		String email = user.getEmail();
		
		String joinCheck = userRepository.userIdCheck(id);
		if(joinCheck != null){
			return "이미 가입되어있는 회원입니다.";
		}
		if(!common.isEmail(email)){
			return "회원가입에 실패하셨습니다.";
		}
		log.info("isEmail function success");
		password = common.passwordEncryption(password);
		if(password == null){
			return "회원가입에 실패하셨습니다.";
		}
		log.info("passwordEncryption function success");
		user.setPassword(password);
		
		User saveUser = userRepository.save(user);
		log.info("create User success");
		if(saveUser == null){
			return "회원가입에 실패하셨습니다.";
		}
		return "회원가입에 성공하셨습니다.";
	}
	
	@Override
	public boolean leave(User user){
		try{
			userRepository.delete(user);
		} catch (Exception e){
			log.error("UserService leave function : " + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean idCheck(String id) {
		if(id.equals("") || id == null){
			return false;
		}
		String idVal = userRepository.userIdCheck(id);
		if(idVal == null){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String emailCheck(String email) {
		if(email.equals("") || email == null){
			return "올바른 형식의 메일을 입력해주세요.";
		}
		String emailVal = userRepository.userIdCheck(email);
		if(emailVal == null && common.isEmail(email)){
			return "가입 가능한 이메일입니다";
		} else if(emailVal != null){
			return "이미 가입되어 있는 이메일입니다.";
		} else {
			return "올바른 형식의 메일을 입력해주세요.";
		}
	}

	@Override
	public User loginCheck(String id, String password) {
		log.info("execute UserService loginCheck");
		if(id == null || id.equals("") || password == null || password.equals("")){
			return null;
		}
		log.info("id & password not null");
		password = common.passwordEncryption(password);
		if(password == null){
			return null;
		}
		log.info("passwordEncryption function success");
		return userRepository.loginCheck(id, password);
	}

	@Override
	public String translatePassword(String email) {
		log.info("execute UserService findPassword");
		if(email == null && !common.isEmail(email)){
			return null;
		}
		User user = userRepository.passwordCheck(email); 
		if(user == null){
			return null;
		}
		String random = "";
		for(int i = 0; i < 6; i++){
			random += ((int)(Math.random() * 10));
		}
		String password = common.passwordEncryption(random);
		if(password == null){
			return null;
		}
		log.info("passwordEncryption function success");
		user.setPassword(password);
		userRepository.save(user);
		
		return random;
	}
}
