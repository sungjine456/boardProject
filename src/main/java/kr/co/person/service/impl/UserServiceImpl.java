package kr.co.person.service.impl;

import java.io.File;

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
import kr.co.person.domain.AutoLogin;
import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;
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
		if(IsValid.isNotValidObjects(user)){
			return new OkCheck(message.USER_FAIL_JOIN, false);
		}
		user.setName(common.cleanXss(user.getName()));
		String id = common.cleanXss(user.getId());
		String password = user.getPassword();
		String email = user.getEmail();
		if(StringUtils.isEmpty(id) || StringUtils.isEmpty(password)){
			return new OkCheck(message.USER_WRONG_ID_OR_WRONG_PASSWORD, false);	
		}
		if(!common.isEmail(email)){
			return new OkCheck(message.USER_NO_EMAIL_FORMAT, false);
		}
		User findUser = userRepository.findById(id);
		if(IsValid.isValidObjects(findUser)){
			if(StringUtils.isNotEmpty(findUser.getId())){
				return new OkCheck(message.USER_ALREADY_JOIN, false);
			}
		}
		password = common.passwordEncryption(password);
		if(StringUtils.isEmpty(password)){
			return new OkCheck(message.USER_FAIL_JOIN, false);
		}
		user.setPassword(password);
		DateTime date = new DateTime();
		user.setRegDate(date);
		user.setUpDate(date);
		
		userRepository.save(user);
		return new OkCheck(message.USER_SUCCESS_JOIN, true);
	}
	
	@Override
	public boolean leave(int idx, String loginId){
		log.info("execute userServiceImpl leave");
		// user에 쓰레기값 넣기위한 암호화
		String garbage = common.passwordEncryption("Garbage");
		User user = userRepository.findOne(idx);
		if(StringUtils.isNotEmpty(loginId)){
			autoLogout(user, loginId);
		}
		if(IsValid.isNotValidObjects(user)){
			return false;
		}
		user.setEmail(garbage);
		user.setId(garbage);
		user.setName(garbage);
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
		return (IsValid.isNotValidObjects(user))
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
			String emailVal = findUserByEmail.getEmail();
			return (emailVal != null && common.isEmail(email))
					?new OkCheck(message.USER_ALREADY_JOIN_EMAIL, false)
					:new OkCheck(message.USER_NO_EMAIL_FORMAT, false);
		} else {
			return (common.isEmail(email))
					?new OkCheck(message.USER_AVAILABLE_EMAIL, true)
					:new OkCheck(message.USER_NO_EMAIL_FORMAT, false);
		}
	}

	@Override
	public User loginCheck(String id, String password) {
		log.info("execute UserServiceImpl loginCheck");
		if(StringUtils.isEmpty(id) || StringUtils.isEmpty(password)){
			return null;
		}
		log.info("id & password not null");
		password = common.passwordEncryption(password);
		if(StringUtils.isEmpty(password)){
			return null;
		}
		log.info("passwordEncryption function success");
		return userRepository.findByIdAndPassword(id, password);
	}

	@Override
	public OkCheck translatePassword(String email) {
		log.info("execute UserServiceImpl translatePassword");
		if(StringUtils.isEmpty(email)){
			return new OkCheck(message.USER_FAIL_TRANSlATE_PASSWORD, false);
		}
		User user = userRepository.findByEmail(email); 
		if(IsValid.isNotValidObjects(user)){
			return new OkCheck(message.USER_WRONG_EMAIL, false);
		}
		String random = "";
		for(int i = 0; i < 6; i++){
			random += ((int)(Math.random() * 10));
		}
		String password = common.passwordEncryption(random);
		if(StringUtils.isEmpty(password)){
			return new OkCheck(message.USER_FAIL_TRANSLATE_PASSWORD, false);
		}
		log.info("passwordEncryption function success");
		user.setPassword(password);
		
		return new OkCheck("비밀번호가 " + random + "로 수정되었습니다.", true);
	}

	@Override
	public User findUserForIdx(int idx) {
		log.info("execute UserServiceImpl findUserForIdx");
		return userRepository.findOne(idx);
	}
	
	@Override
	public User findUserForId(String id) {
		log.info("execute UserServiceImpl findUserForId");
		return userRepository.findById(id);
	}

	@Override
	public OkCheck changePassword(int idx, String password, String changePassword) {
		log.info("execute UserServiceImpl changePassword");
		if(IsValid.isNotValidInts(idx)){
			return new OkCheck(message.USER_NO_LOGIN, false);
		}
		if(StringUtils.isEmpty(password)){
			return new OkCheck(message.USER_NO_PASSWORD, false);
		}
		if(StringUtils.isEmpty(changePassword)){
			return new OkCheck(message.USER_NO_UPDATE_PASSWORD, false);
		}
		User user = userRepository.findOne(idx);
		password = common.passwordEncryption(password);
		if(StringUtils.isEmpty(password)){
			return new OkCheck(message.USER_NO_PASSWORD, false);
		} else if(!password.equals(user.getPassword())){
			return new OkCheck(message.USER_RE_PASSWORD, false);
		}
		changePassword = common.passwordEncryption(changePassword);
		if(StringUtils.isEmpty(changePassword)){
			return new OkCheck(message.USER_RE_UPDATE_PASSWORD, false);
		}
		user.setPassword(changePassword);
		
		return new OkCheck(message.USER_SUCCESS_TRANSlATE_PASSWORD, true);
	}
	
	@Override
	public boolean autoLoginCheck(User user, String loginId){
		log.info("execute UserServiceImpl autoLoginCheck");
		if(IsValid.isNotValidObjects(user) || IsValid.isNotValidObjects(user.getIdx()) || StringUtils.isEmpty(loginId)){
			return false;
		}
		user = userRepository.findOne(user.getIdx());
		if(IsValid.isNotValidObjects(user)){
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
		if(IsValid.isNotValidObjects(user) || IsValid.isNotValidObjects(user.getIdx()) || StringUtils.isEmpty(loginId)){
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
		if(IsValid.isNotValidObjects(user) || StringUtils.isEmpty(loginId)){
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
		if(IsValid.isNotValidInts(idx) || StringUtils.isEmpty(name) || !common.isEmail(email)){
			return false;
		}
		User user = userRepository.findOne(idx);
		if(IsValid.isNotValidObjects(user)){
			return false;
		}
		if(StringUtils.isEmpty(imgPath)){
			imgPath = "C:"+se+"boardProject"+se+"img"+se+"user"+se+"default.png";
		} 
		user.setName(name);
		user.setEmail(email);
		user.setImg(imgPath);
		user.setUpDate(new DateTime());
		user = userRepository.save(user);
		if(user == null){
			return false;
		}
		return true;
	}
	
	@Override
	public boolean update(int idx, String name, String email) {
		log.info("execute UserService update 3param");
		if(IsValid.isNotValidInts(idx) || StringUtils.isEmpty(name) || !common.isEmail(email)){
			return false;
		}
		User user = userRepository.findOne(idx);
		if(IsValid.isNotValidObjects(user)){
			return false;
		}
		user.setName(name);
		user.setEmail(email);
		user.setUpDate(new DateTime());
		userRepository.save(user);
		return true;
	}

	@Override
	public boolean passwordCheck(int idx, String password) {
		log.info("execute UserServiceImpl passwordCheck");
		User user = userRepository.findOne(idx);
		if(IsValid.isNotValidObjects(user) || !StringUtils.equals(common.passwordEncryption(password), user.getPassword())){
			return false;
		}
		return true;
	}
}
