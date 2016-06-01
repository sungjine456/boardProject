package kr.co.person.service;

import kr.co.person.domain.OkCheck;
import kr.co.person.domain.User;

public interface UserService {
	OkCheck join(User user);
	OkCheck idCheck(String id);
	OkCheck emailCheck(String email);
	User loginCheck(String id, String password);
	boolean leave(int idx);
	String translatePassword(String email);
	User findUserForIdx(int idx);
	OkCheck changePassword(int idx, String password, String changePassword);
}
