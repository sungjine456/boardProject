package kr.co.person.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public List<User> findUserAll() {
		return userRepository.findAll();
	}
}
