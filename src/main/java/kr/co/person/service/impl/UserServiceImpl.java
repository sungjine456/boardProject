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
	public boolean create(User user){
		log.info("execute UserService create");
		if(user == null){
			return false;
		}
		String password = user.getPassword();
		String email = user.getEmail();
		if(!common.isEmail(email)){
			return false;
		}
		log.info("isEmail function success");
		password = common.passwordEncryption(password);
		if(password == null){
			return false;
		}
		log.info("passwordEncryption function success");
		user.setPassword(password);
		
		User u = userRepository.save(user);
		log.info("create User success");
		if(u == null){
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
	public boolean loginCheck(String id, String password) {
		log.info("execute UserService loginCheck");
		if(id == null || id.equals("") || password == null || password.equals("")){
			return false;
		}
		log.info("id & password not null");
		password = common.passwordEncryption(password);
		if(password == null){
			return false;
		}
		log.info("passwordEncryption function success");
		String login = userRepository.loginCheck(id, password);
		log.info("function loginCheck success");
		if(login == null){
			return false;
		}
		return true;
	}
}
