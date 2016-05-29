package kr.co.person.service;

import kr.co.person.domain.User;

public interface UserService {
	String join(User user);
	boolean idCheck(String id);
	String emailCheck(String email);
	User loginCheck(String id, String password);
	boolean leave(User user);
	String translatePassword(String email);
}
