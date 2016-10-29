package kr.co.person.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.co.person.domain.User;

public interface AdminService {
	public Page<User> findUserAll(Pageable pageable);
}
