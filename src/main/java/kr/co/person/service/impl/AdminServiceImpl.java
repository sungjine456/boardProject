package kr.co.person.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.person.domain.User;
import kr.co.person.repository.UserRepository;
import kr.co.person.service.AdminService;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

	@Autowired UserRepository userRepository;
	
	@Override
	public Page<User> findUserAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}
}
