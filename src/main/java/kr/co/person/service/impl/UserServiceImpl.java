package kr.co.person.service.impl;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.person.common.Common;
import kr.co.person.common.IsValid;
import kr.co.person.common.Message;
import kr.co.person.common.exception.EmptyStringException;
import kr.co.person.domain.AutoLogin;
import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;
import kr.co.person.pojo.OkUserCheck;
import kr.co.person.repository.AutoLoginRepository;
import kr.co.person.repository.UserRepository;
import kr.co.person.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired private UserRepository userRepository;
	@Autowired private AutoLoginRepository autoLoginRepository;
	@Autowired private Common common;
	@Autowired private Message message;
	
	@Override
	public OkCheck join(User user){
		log.info("execute UserServiceImpl join");
		if(IsValid.isNotValidUser(user)){
			return new OkCheck(message.USER_FAIL_JOIN, false);
		}
		String id = "";
		String password = "";
		String name = "";
		try {
			id = common.cleanXss(user.getId());
			password = common.passwordEncryption(user.getPassword());
			name = common.cleanXss(user.getName());
		} catch(EmptyStringException e){
			log.info("execute UserServiceImpl join : " + e.getMessage());
		} catch(NoSuchAlgorithmException e) {
			return new OkCheck(message.USER_RE_PASSWORD, false);
		}
		String email = user.getEmail();
		OkCheck emailCheck = common.isEmail(email);
		if(StringUtils.isEmpty(id) || StringUtils.isEmpty(password)){
			return new OkCheck(message.USER_WRONG_ID_OR_WRONG_PASSWORD, false);	
		}
		if(StringUtils.isEmpty(name)){
			return new OkCheck(message.USER_NO_NAME, false);
		}
		if(!emailCheck.isBool()){
			return new OkCheck(emailCheck.getMessage(), false);
		}
		OkCheck joinCheckId = idCheck(id);
		OkCheck joinCheckEmail = emailCheck(email);
		if(!joinCheckId.isBool()){
			return joinCheckId;
		}
		if(!joinCheckEmail.isBool()){
			return joinCheckEmail;
		}
		user.setId(id);
		user.setName(name);
		user.setPassword(password);
		DateTime date = new DateTime();
		user.setRegDate(date);
		user.setUpdateDate(date);
		
		userRepository.save(user);
		return new OkCheck(message.USER_SUCCESS_JOIN, true);
	}
	
	@Override
	public boolean leave(int idx, String loginId){
		log.info("execute userServiceImpl leave");
		String garbage = "b94c56f6f1cf92d48e021c573b77fa253eca91e579e308473c0536716c8e7bd6personProject";
		User user = userRepository.findOne(idx);
		if(IsValid.isNotValidUser(user)){
			return false;
		}
		if(StringUtils.isNotEmpty(loginId)){
			if(autoLoginCheck(user, loginId)){
				if(!autoLogout(user, loginId)){
					return false;
				}
			} else {
				return false;
			}
		}
		user.setEmail(garbage);
		user.setId(garbage);
		user.setPassword(garbage);
		userRepository.save(user);
		return true;
	}

	@Override
	public OkCheck idCheck(String id) {
		log.info("execute userServiceImpl idCheck");
		if(StringUtils.isEmpty(id)){
			return new OkCheck(message.USER_NO_ID, false);
		}
		User user = userRepository.findById(id);
		return (IsValid.isNotValidUser(user))
				?new OkCheck(message.USER_AVAILABLE_ID, true)
				:new OkCheck(message.USER_ALREADY_JOIN_ID, false);
	}

	@Override
	public OkCheck emailCheck(String email) {
		log.info("execute userServiceImpl emailCheck");
		if(StringUtils.isEmpty(email)){
			return new OkCheck(message.USER_NO_EMAIL, false);
		}
		User findUserByEmail = userRepository.findByEmail(email);
		if(IsValid.isValidObjects(findUserByEmail)){
			return (common.isEmail(findUserByEmail.getEmail()).isBool())
					?new OkCheck(message.USER_ALREADY_JOIN_EMAIL, false)
					:new OkCheck(message.USER_NO_EMAIL_FORMAT, false);
		} else {
			return (common.isEmail(email).isBool())
					?new OkCheck(message.USER_AVAILABLE_EMAIL, true)
					:new OkCheck(message.USER_NO_EMAIL_FORMAT, false);
		}
	}

	@Override
	public OkUserCheck joinCheck(String id, String password) {
		log.info("execute UserServiceImpl joinCheck");
		if(StringUtils.isEmpty(id)){
			return new OkUserCheck(new User(), message.USER_NO_ID, false);
		}
		try {
			password = common.passwordEncryption(password);
		} catch(EmptyStringException e){
			return new OkUserCheck(new User(), message.USER_NO_PASSWORD, false);
		} catch (NoSuchAlgorithmException e) {
			return new OkUserCheck(new User(), message.USER_RE_PASSWORD, false);
		}
		if(StringUtils.isEmpty(password)){
			return new OkUserCheck(new User(), message.USER_RE_PASSWORD, false);
		}
		User user = userRepository.findByIdAndPassword(id, password);
		return new OkUserCheck(user, "", true);
	}

	@Override
	public OkCheck translatePassword(String email) {
		log.info("execute UserServiceImpl translatePassword");
		OkCheck emailCheck = common.isEmail(email);
		if(!emailCheck.isBool()){
			return new OkCheck(emailCheck.getMessage(), false);
		}
		User user = userRepository.findByEmail(email); 
		if(IsValid.isNotValidUser(user)){
			return new OkCheck(message.USER_WRONG_EMAIL, false);
		}
		String random = "";
		for(int i = 0; i < 6; i++){
			random += ((int)(Math.random() * 10));
		}
		String password = "";
		try {
			password = common.passwordEncryption(random);
		} catch (EmptyStringException e) {
			return new OkCheck(message.USER_FAIL_TRANSLATE_PASSWORD, false);
		} catch (NoSuchAlgorithmException e) {
			return new OkCheck(message.USER_FAIL_TRANSLATE_PASSWORD, false);
		}
		if(StringUtils.isEmpty(password)){
			return new OkCheck(message.USER_FAIL_TRANSLATE_PASSWORD, false);
		}
		user.setPassword(password);
		
		return new OkCheck("비밀번호가 " + random + "로 수정되었습니다.", true);
	}
	
	@Override
	public User findUserForId(String id) {
		log.info("execute UserServiceImpl findUserForId");
		return userRepository.findById(id);
	}
	
	@Override
	public User findUserForEmail(String email) {
		log.info("execute UserServiceImpl findUserForEmail");
		return userRepository.findByEmail(email);
	}

	@Override
	public OkCheck changePassword(int idx, String password, String changePassword) {
		log.info("execute UserServiceImpl changePassword");
		if(IsValid.isNotValidInts(idx)){
			return new OkCheck(message.USER_NO_LOGIN, false);
		}
		User user = userRepository.findOne(idx);
		if(IsValid.isNotValidUser(user)){
			return new OkCheck(message.USER_WRONG_USER, false);
		}
		try {
			password = common.passwordEncryption(password);
			changePassword = common.passwordEncryption(changePassword);
		} catch (EmptyStringException e) {
			return new OkCheck(message.USER_RE_PASSWORD, false);
		} catch (NoSuchAlgorithmException e) {
			return new OkCheck(message.USER_RE_PASSWORD, false);
		}
		if(StringUtils.isEmpty(password)){
			return new OkCheck(message.USER_NO_PASSWORD, false);
		} else if(!password.equals(user.getPassword())){
			return new OkCheck(message.USER_RE_PASSWORD, false);
		}
		if(StringUtils.isEmpty(changePassword)){
			return new OkCheck(message.USER_RE_UPDATE_PASSWORD, false);
		}
		user.setPassword(changePassword);
		
		return new OkCheck(message.USER_SUCCESS_TRANSlATE_PASSWORD, true);
	}
	
	@Override
	public boolean autoLoginCheck(User user, String loginId){
		log.info("execute UserServiceImpl autoLoginCheck");
		if(IsValid.isNotValidUser(user) || IsValid.isNotValidObjects(user.getIdx()) || StringUtils.isEmpty(loginId)){
			return false;
		}
		user = userRepository.findOne(user.getIdx());
		if(IsValid.isNotValidUser(user)){
			return false;
		}
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndLoginId(user.getIdx(), loginId);
		if(IsValid.isNotValidObjects(autoLogin)){
			return false;
		}
		return true;
	};
	
	@Override
	public boolean autoLogin(User user, String loginId){
		log.info("execute UserServiceImpl autoLogin");
		if(IsValid.isNotValidUser(user) || IsValid.isNotValidObjects(user.getIdx()) || StringUtils.isEmpty(loginId)){
			return false;
		}
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndLoginId(user.getIdx(), loginId);
		if(IsValid.isNotValidObjects(autoLogin)){
			autoLoginRepository.save(new AutoLogin(loginId, new DateTime(), user));
		}
		return true;
	}

	@Override
	public boolean autoLogout(User user, String loginId) {
		log.info("execute userServiceImpl autoLogout");
		if(IsValid.isNotValidUser(user) || StringUtils.isEmpty(loginId)){
			return false;
		}
		int idx = user.getIdx();
		if(IsValid.isNotValidInts(idx)){
			return false;
		}
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndLoginId(idx, loginId);
		if(IsValid.isValidObjects(autoLogin)){
			autoLoginRepository.delete(autoLogin);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean update(int idx, String name, String email, String imgPath) {
		log.info("execute UserServiceImpl update 4param");
		String se = File.separator;
		if(IsValid.isNotValidInts(idx) || StringUtils.isEmpty(name) || !common.isEmail(email).isBool()){
			return false;
		}
		User user = userRepository.findOne(idx);
		if(IsValid.isNotValidUser(user)){
			return false;
		}
		if(StringUtils.isEmpty(imgPath)){
			imgPath = "img"+se+"user"+se+"default.png";
		} 
		user.setName(name);
		user.setEmail(email);
		user.setImg(imgPath);
		user.setUpdateDate(new DateTime());
		user = userRepository.save(user);
		if(user == null){
			return false;
		}
		return true;
	}
	
	@Override
	public boolean update(int idx, String name, String email) {
		log.info("execute UserService update 3param");
		if(IsValid.isNotValidInts(idx) || StringUtils.isEmpty(name) || !common.isEmail(email).isBool()){
			return false;
		}
		User user = userRepository.findOne(idx);
		if(IsValid.isNotValidUser(user)){
			return false;
		}
		user.setName(name);
		user.setEmail(email);
		user.setUpdateDate(new DateTime());
		userRepository.save(user);
		return true;
	}

	@Override
	public boolean passwordCheck(int idx, String password) {
		log.info("execute UserServiceImpl passwordCheck");
		User user = userRepository.findOne(idx);
		try {
			password = common.passwordEncryption(password);
		} catch(EmptyStringException e){
			return false;
		} catch (NoSuchAlgorithmException e) {
			return false;
		}
		if(IsValid.isNotValidUser(user) || !StringUtils.equals(password, user.getPassword())){
			return false;
		}
		return true;
	}

	@Override
	public User accessEmail(String email) {
		log.info("execute UserServiceImpl accessEmail");
		User user = userRepository.findByEmail(email);
		if(IsValid.isNotValidUser(user)){
			return new User();
		}
		user.setAccess("Y");
		return user;
	}
}
