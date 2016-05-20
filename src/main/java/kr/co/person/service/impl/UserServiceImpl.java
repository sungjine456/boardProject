package kr.co.person.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.person.domain.User;
import kr.co.person.repository.UserRepository;
import kr.co.person.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired UserRepository userRepository;
	
	@Override
	public void create(User user){
		userRepository.save(user);
	}
}
