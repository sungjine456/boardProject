package kr.co.person.service;

import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;
import kr.co.person.pojo.OkObjectCheck;

public interface UserService {
	OkCheck join(User user);
	OkCheck idCheck(String id);
	OkCheck emailCheck(String email);
	OkObjectCheck<User> confirmUserPassword(String id, String password);
	boolean leave(int idx, String loginId);
	OkCheck translatePassword(String email);
	OkObjectCheck<User> findUserForId(String id);
	OkObjectCheck<User> findUserForEmail(String email);
	OkCheck changePassword(int idx, String password, String changePassword);
	boolean autoLoginCheck(User user, String loginId);
	boolean autoLogin(User user, String loginId);
	boolean autoLogout(User user, String loginId);
	boolean update(User user);
	boolean passwordCheck(int idx, String password);
	OkObjectCheck<User> accessEmail(String email);
}
