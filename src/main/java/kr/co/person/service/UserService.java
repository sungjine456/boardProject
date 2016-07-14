package kr.co.person.service;

import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;

public interface UserService {
	OkCheck join(User user);
	OkCheck idCheck(String id);
	OkCheck emailCheck(String email);
	User loginCheck(String id, String password);
	boolean leave(int idx, String ip);
	OkCheck translatePassword(String email);
	User findUserForIdx(int idx);
	User findUserForId(String id);
	OkCheck changePassword(int idx, String password, String changePassword);
	boolean autoLoginCheck(User user, String ip);
	boolean autoLogin(User user, String ip);
	boolean autoLogout(User user, String ip);
	boolean update(int idx, String name, String email, String fileName);
	boolean update(int idx, String name, String email);
	boolean passwordCheck(int idx, String password);
}
