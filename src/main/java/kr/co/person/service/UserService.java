package kr.co.person.service;

import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;
import kr.co.person.pojo.OkUserCheck;

public interface UserService {
	OkCheck join(User user);
	OkCheck idCheck(String id);
	OkCheck emailCheck(String email);
	OkUserCheck confirmUserPassword(String id, String password);
	boolean leave(int idx, String loginId);
	OkCheck translatePassword(String email);
	OkUserCheck findUserForId(String id);
	OkUserCheck findUserForEmail(String email);
	OkCheck changePassword(int idx, String password, String changePassword);
	boolean autoLoginCheck(User user, String loginId);
	boolean autoLogin(User user, String loginId);
	boolean autoLogout(User user, String loginId);
	boolean update(User user);
	boolean passwordCheck(int idx, String password);
	OkUserCheck accessEmail(String email);
}
