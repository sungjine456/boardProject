package kr.co.person.service;

import kr.co.person.domain.User;

public interface UserService {
	boolean create(User user);
	boolean idCheck(String id);
	String emailCheck(String email);
	boolean loginCheck(String id, String password);
}
