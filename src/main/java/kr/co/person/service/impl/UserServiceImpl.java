package kr.co.person.service.impl;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.person.common.Common;
import kr.co.person.common.IsValid;
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
	
	@Autowired 
	private UserRepository userRepository;
	@Autowired
	private AutoLoginRepository autoLoginRepository;
	@Autowired 
	private Common common;
	
	@Override
	public OkCheck join(User user){
		log.info("execute UserService create");
		if(IsValid.isNotValid(user)){
			return new OkCheck("회원가입에 실패하셨습니다.", false);
		}
		user.setId(common.cleanXss(user.getId()));
		user.setName(common.cleanXss(user.getName()));
		String id = user.getId();
		String password = user.getPassword();
		String email = user.getEmail();
		if(!common.isEmail(email)){
			return new OkCheck("회원가입에 실패하셨습니다.", false);
		}
		User findUser = userRepository.findById(id);
		if(IsValid.isValid(findUser)){
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
		DateTime date = new DateTime();
		user.setRegDate(date);
		user.setUpDate(date);
		
		log.info("create User success");
		userRepository.save(user);
		return new OkCheck("회원가입에 성공하셨습니다.", true);
	}
	
	@Override
	public boolean leave(int idx, String ip){
		log.info("userService leave");
		// user에 쓰레기값 넣기위한 암호화
		String garbage = common.passwordEncryption("Garbage");
		User user = userRepository.findOne(idx);
		if(IsValid.isNotValid(user)){
			return false;
		}
		if(!autoLogout(user, ip)){
			return false;
		}
		log.info("userService leave success before");
		user.setEmail(garbage);
		user.setId(garbage);
		user.setName(garbage);
		user.setPassword(garbage);
		userRepository.save(user);
		log.info("userService leave success");
		return true;
	}

	@Override
	public OkCheck idCheck(String id) {
		if(StringUtils.isEmpty(id)){
			return new OkCheck("아이디를 입력해주세요.", false);
		}
		User user = userRepository.findById(id);
		if(IsValid.isNotValid(user)){
			return new OkCheck("가입 가능한 아이디입니다.", true);
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
		if(IsValid.isValid(findUserByEmail)){
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
		if(StringUtils.isEmpty(password)){
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
		if(IsValid.isNotValid(user)){
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
	public User findUserForId(String id) {
		return userRepository.findById(id);
	}

	@Override
	public OkCheck changePassword(int idx, String password, String changePassword) {
		log.info("execute UserService changePassword");
		if(IsValid.isNotValid(idx)){
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
	
	@Override
	public boolean autoLoginCheck(User user, String ip){
		if(IsValid.isNotValid(user) || IsValid.isNotValid(user.getIdx())){
			return false;
		}
		user = userRepository.findOne(user.getIdx());
		log.info("userService autoLoginCheck user :  " + user);
		if(IsValid.isNotValid(user) || StringUtils.isEmpty(ip)){
			return false;
		}
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndRegIp(user.getIdx(), ip);
		log.info("userService autoLoginCheck autoLogin :  " + autoLogin);
		if(IsValid.isNotValid(autoLogin)){
			return false;
		}
		log.info("userService autoLoginCheck autoLoginCheck :  " + autoLogin.getLoginCheck());
		if(!autoLogin.getLoginCheck().equals("O")){
			return false;
		}
		Date date = new Date();
		if(date.getTime() - autoLogin.getRegDate().getTime() > 24 * 60 * 60 * 1000){
			return false;
		}
		return true;
	};
	
	@Override
	public boolean autoLogin(User user, String ip){
		if(IsValid.isNotValid(user) || IsValid.isNotValid(user.getIdx()) || StringUtils.isEmpty(ip)){
			return false;
		}
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndRegIp(user.getIdx(), ip);
		if(IsValid.isNotValid(autoLogin)){
			autoLoginRepository.save(new AutoLogin(user, "O", ip, new Date()));
		} else {
			autoLogin.setLoginCheck("O");
			autoLoginRepository.save(autoLogin);
		}
		return true;
	}

	@Override
	public boolean autoLogout(User user, String ip) {
		log.info("execute userService autoLogout");
		if(IsValid.isNotValid(user) || IsValid.isNotValid(user.getIdx()) || StringUtils.isEmpty(ip)){
			return false;
		}
		AutoLogin autoLogin = autoLoginRepository.findByUserIdxAndRegIp(user.getIdx(), ip);
		if(IsValid.isValid(autoLogin)){
			autoLogin.setLoginCheck("X");
			autoLoginRepository.save(autoLogin);
		}
		return true;
	}

	@Override
	public boolean update(int idx, String name, String email, String fileName) {
		String se = File.separator;
		if(IsValid.isNotValid(idx)){
			return false;
		}
		if(StringUtils.isEmpty(name)){
			return false;
		}
		if(StringUtils.isEmpty(email) && !common.isEmail(email)){
			return false;
		}
		log.info("execute UserService update fileName : " + fileName);
		User user = userRepository.findOne(idx);
		if(IsValid.isNotValid(user)){
			return false;
		}
		if(StringUtils.isEmpty(fileName)){
			fileName = "default.png";
		} 
		user.setName(name);
		user.setEmail(email);
		user.setImg("img"+se+"user"+se+fileName);
		user.setUpDate(new DateTime());
		user = userRepository.save(user);
		if(user == null){
			return false;
		}
		return true;
	}
	
	@Override
	public boolean update(int idx, String name, String email) {
		log.info("execute UserService update");
		if(IsValid.isNotValid(idx)){
			return false;
		}
		if(StringUtils.isEmpty(name)){
			return false;
		}
		if(StringUtils.isEmpty(email) && !common.isEmail(email)){
			return false;
		}
		User user = userRepository.findOne(idx);
		if(IsValid.isNotValid(user)){
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
		User user = userRepository.findOne(idx);
		if(IsValid.isNotValid(user)){
			return false;
		}
		if(!StringUtils.equals(common.passwordEncryption(password), user.getPassword())){
			return false;
		}
		return true;
	}
}
