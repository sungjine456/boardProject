package kr.co.person.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.person.common.Common;
import kr.co.person.domain.OkCheck;
import kr.co.person.domain.User;
import kr.co.person.repository.UserRepository;
import kr.co.person.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired 
	private UserRepository userRepository;
	@Autowired 
	private Common common;
	
	@Override
	public OkCheck join(User user){
		log.info("execute UserService create");
		if(user == null){
			return new OkCheck("회원가입에 실패하셨습니다.", false);
		}
		String id = user.getId();
		String password = user.getPassword();
		String email = user.getEmail();
		if(!common.isEmail(email)){
			return new OkCheck("회원가입에 실패하셨습니다.", false);
		}
		User findUser = userRepository.findById(id);
		if(findUser != null){
			if(StringUtils.isNotEmpty(findUser.getId())){
				return new OkCheck("이미 가입되어있는 회원입니다.", false);
			}
		}
		log.info("isEmail function success");
		password = common.passwordEncryption(password);
		if(StringUtils.isEmpty(password)){
			return new OkCheck("회원가입에 실패하셨습니다.", false);
		}
		log.info("passwordEncryption function success");
		user.setPassword(password);
		Date date = new Date();
		user.setRegDate(date);
		user.setUpDate(date);
		
		User saveUser = userRepository.save(user);
		log.info("create User success");
		if(saveUser == null){
			return new OkCheck("회원가입에 실패하셨습니다.", false);
		}
		return new OkCheck("회원가입에 성공하셨습니다.", true);
	}
	
	@Override
	public boolean leave(int idx){
		try{
			userRepository.delete(idx);
		} catch (Exception e){
			log.error("UserService leave function : " + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public OkCheck idCheck(String id) {
		if(StringUtils.isEmpty(id)){
			return new OkCheck("아이디를 입력해주세요.", false);
		}
		String idVal = userRepository.findById(id).getId();
		if(StringUtils.isEmpty(idVal)){
			return new OkCheck("가입 가능한 아이디입니다", true);
		} else {
			return new OkCheck("이미 가입되어 있는 아이디입니다.", false);
		}
	}

	@Override
	public OkCheck emailCheck(String email) {
		if(StringUtils.isEmpty(email)){
			return new OkCheck("메일을 입력해주세요.", false);
		}
		User findUserByEmail = userRepository.findByEmail(email);
		if(findUserByEmail != null){
			String emailVal = findUserByEmail.getEmail();
			if(emailVal != null && common.isEmail(email)){
				return new OkCheck("이미 가입되어 있는 이메일입니다.", false);
			} else {
				return new OkCheck("올바른 형식의 메일을 입력해주세요.", false);
			}
		} else {
			if(common.isEmail(email)){
				return new OkCheck("가입 가능한 이메일입니다.", true);
			} else {
				return new OkCheck("올바른 형식의 메일을 입력해주세요.", false);
			}
		}
	}

	@Override
	public User loginCheck(String id, String password) {
		log.info("execute UserService loginCheck");
		if(StringUtils.isEmpty(id) || StringUtils.isEmpty(password)){
			return null;
		}
		log.info("id & password not null");
		password = common.passwordEncryption(password);
		if(password == null){
			return null;
		}
		log.info("passwordEncryption function success");
		return userRepository.findByIdAndPassword(id, password);
	}

	@Override
	public OkCheck translatePassword(String email) {
		log.info("execute UserService findPassword");
		if(StringUtils.isEmpty(email)){
			return new OkCheck("비밀번호 수정을 실패했습니다.", false);
		}
		User user = userRepository.findByEmail(email); 
		if(user == null){
			return new OkCheck("비밀번호 수정을 실패했습니다.", false);
		}
		String random = "";
		for(int i = 0; i < 6; i++){
			random += ((int)(Math.random() * 10));
		}
		String password = common.passwordEncryption(random);
		if(StringUtils.isEmpty(password)){
			return new OkCheck("비밀번호 수정을 실패했습니다.", false);
		}
		log.info("passwordEncryption function success");
		user.setPassword(password);
		
		return new OkCheck("비밀번호가 " + random + "로 수정되었습니다.", true);
	}

	@Override
	public User findUserForIdx(int idx) {
		return userRepository.findOne(idx);
	}

	@Override
	public OkCheck changePassword(int idx, String password, String changePassword) {
		log.info("execute UserService changePassword");
		if(idx == 0){
			return new OkCheck("로그인 후 이용해주세요.", false);
		}
		if(StringUtils.isEmpty(password)){
			return new OkCheck("비밀번호를 입력해 주세요", false);
		}
		if(StringUtils.isEmpty(changePassword)){
			return new OkCheck("비밀번호 수정을 입력해 주세요", false);
		}
		User user = userRepository.findOne(idx);
		password = common.passwordEncryption(password);
		if(StringUtils.isEmpty(password)){
			return new OkCheck("비밀번호를 다시 입력해 주세요", false);
		} else if(!password.equals(user.getPassword())){
			return new OkCheck("아이디의 비밀번호가 맞지 않습니다.", false);
		}
		changePassword = common.passwordEncryption(changePassword);
		if(StringUtils.isEmpty(changePassword)){
			return new OkCheck("비밀번호 수정을 다시 입력해 주세요", false);
		}
		log.info("passwordEncryption function success");
		user.setPassword(changePassword);
		
		return new OkCheck("비밀번호 수정이 완료되었습니다.", true);
	}
}
