package kr.co.person.service;

import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;

public interface UserService {
	OkCheck join(User user);
	OkCheck idCheck(String id);
	OkCheck emailCheck(String email);
	User loginCheck(String id, String password);
	boolean leave(int idx, String loginId);
	OkCheck translatePassword(String email);
	User findUserForIdx(int idx);
	User findUserForId(String id);
	OkCheck changePassword(int idx, String password, String changePassword);
	boolean autoLoginCheck(User user, String loginId);
	boolean autoLogin(User user, String loginId);
	boolean autoLogout(User user, String loginId);
	boolean update(int idx, String name, String email, String imgPath);
	boolean update(int idx, String name, String email);
	boolean passwordCheck(int idx, String password);
}
