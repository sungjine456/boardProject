package kr.co.person.service;

import kr.co.person.domain.OkCheck;
import kr.co.person.domain.User;

public interface UserService {
	OkCheck join(User user);
	OkCheck idCheck(String id);
	OkCheck emailCheck(String email);
	User loginCheck(String id, String password);
	boolean leave(User user);
	String translatePassword(String email);
}
