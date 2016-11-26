package kr.co.person.service.impl;

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
import kr.co.person.pojo.OkObjectCheck;
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
		DateTime date = new DateTime();
		user.setRegDate(date);
		user.setUpdateDate(date);
		
		User updateUser = userRepository.save(user);
		if(updateUser == null){
			return new OkCheck(message.USER_FAIL_JOIN, true);
		} else {
			return new OkCheck(message.USER_SUCCESS_JOIN, true);
		}
	}
	
	@Override
	public boolean leave(int idx, String loginId){
		log.info("execute userServiceImpl leave");
		String garbage = "b94c56f6f1cf92d48e021c573b77fa253eca91e579e308473c0536716c8e7bd6personProject";
		User user = userRepository.findOne(idx);
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
		user.setName(garbage);
		user.setImg(garbage);
		user.setAccess("N");
		return true;
	}

	@Override
	public OkCheck idCheck(String id) {
		log.info("execute userServiceImpl idCheck");
		User user = userRepository.findById(id);
		return (IsValid.isNotValidUser(user))
				?new OkCheck(message.USER_AVAILABLE_ID, true)
				:new OkCheck(message.USER_ALREADY_JOIN_ID, false);
	}

	@Override
	public OkCheck emailCheck(String email) {
		log.info("execute userServiceImpl emailCheck");
		User findUserByEmail = userRepository.findByEmail(email);
		if(IsValid.isValidUser(findUserByEmail)){
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
	public OkObjectCheck<User> confirmUserPassword(String id, String password) {
		log.info("execute UserServiceImpl confirmUserPassword");
		User user = userRepository.findByIdAndPassword(id, password);
		if(IsValid.isNotValidUser(user)){
			return new OkObjectCheck<User>(user, message.USER_WRONG_USER, true);
		}
		return new OkObjectCheck<User>(user, "", true);
	}

	@Override
	public OkCheck translatePassword(String email) {
		log.info("execute UserServiceImpl translatePassword");
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
		user.setPassword(password);
		
		return new OkCheck("비밀번호가 " + random + "로 수정되었습니다.", true);
	}
	
	@Override
	public OkObjectCheck<User> findUserForId(String id) {
		log.info("execute UserServiceImpl findUserForId");
		User user = userRepository.findById(id);
		if(IsValid.isValidUser(user)){
			return new OkObjectCheck<User>(user, "", true);
		} else {
			return new OkObjectCheck<User>(new User(), message.USER_WRONG_ID, false);
		}
	}
	
	@Override
	public OkObjectCheck<User> findUserForEmail(String email) {
		log.info("execute UserServiceImpl findUserForEmail");
		User user = userRepository.findByEmail(email);
		if(IsValid.isValidUser(user)){
			return new OkObjectCheck<User>(user, "", true);
		} else {
			return new OkObjectCheck<User>(new User(), message.USER_WRONG_EMAIL, false);
		}
	}

	@Override
	public OkCheck changePassword(int idx, String password, String changePassword) {
		log.info("execute UserServiceImpl changePassword");
		User user = userRepository.findOne(idx);
		if(IsValid.isNotValidUser(user)){
			return new OkCheck(message.USER_WRONG_USER, false);
		}
		if(!password.equals(user.getPassword())){
			return new OkCheck(message.USER_RE_PASSWORD, false);
		}
		user.setPassword(changePassword);
		
		return new OkCheck(message.USER_SUCCESS_TRANSlATE_PASSWORD, true);
	}
	
	@Override
	public boolean autoLoginCheck(User user, String loginId){
		log.info("execute UserServiceImpl autoLoginCheck");
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndLoginId(user.getIdx(), loginId);
		if(IsValid.isNotValidObjects(autoLogin)){
			return false;
		}
		return true;
	};
	
	@Override
	public boolean autoLogin(User user, String loginId){
		log.info("execute UserServiceImpl autoLogin");
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndLoginId(user.getIdx(), loginId);
		if(IsValid.isNotValidObjects(autoLogin)){
			autoLoginRepository.save(new AutoLogin(loginId, new DateTime(), user));
		}
		return true;
	}

	@Override
	public boolean autoLogout(User user, String loginId) {
		log.info("execute userServiceImpl autoLogout");
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndLoginId(user.getIdx(), loginId);
		if(IsValid.isValidObjects(autoLogin)){
			autoLoginRepository.delete(autoLogin);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public OkObjectCheck<User> update(User user) {
		log.info("execute UserServiceImpl update");
		User findUser = userRepository.findOne(user.getIdx());
		findUser.setName(user.getName());
		findUser.setEmail(user.getEmail());
		findUser.setImg(user.getImg());
		findUser.setUpdateDate(new DateTime());
		return new OkObjectCheck<User>(findUser, message.USER_SUCCESS_UPDATE, true);
	}

	@Override
	public boolean passwordCheck(int idx, String password) {
		log.info("execute UserServiceImpl passwordCheck");
		User user = userRepository.findOne(idx);
		if(IsValid.isNotValidUser(user) || !StringUtils.equals(password, user.getPassword())){
			return false;
		}
		return true;
	}

	@Override
	public OkObjectCheck<User> accessEmail(String email) {
		log.info("execute UserServiceImpl accessEmail");
		User user = userRepository.findByEmail(email);
		if(IsValid.isNotValidUser(user)){
			return new OkObjectCheck<User>(new User(), message.ACCESS_FAIL_ACCESS, false);
		}
		user.setAccess("Y");
		return new OkObjectCheck<User>(user, message.ACCESS_THANK_YOU_FOR_AGREE, true);
	}
}
