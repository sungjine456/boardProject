package kr.co.person.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.person.common.IsValid;
import kr.co.person.domain.Board;
import kr.co.person.domain.User;
import kr.co.person.repository.BoardRepository;
import kr.co.person.repository.UserRepository;
import kr.co.person.service.AdminService;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

	@Autowired UserRepository userRepository;
	@Autowired BoardRepository boardRepository;
	
	@Override
	public Page<User> findUserAll(Pageable pageable) {
		Page<User> findUser = userRepository.findAll(pageable);
		if(IsValid.isNotValidObjects(findUser)){
			return null;
		}
		return findUser;
	}
	
	@Override
	public Page<Board> findBoardAll(Pageable pageable){
		Page<Board> findBoard = boardRepository.findAll(pageable);
		if(IsValid.isNotValidObjects(findBoard)){
			return null;
		}
		return findBoard;
	}
}
