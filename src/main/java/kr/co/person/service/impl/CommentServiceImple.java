package kr.co.person.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.person.domain.Comment;
import kr.co.person.repository.CommentRepository;
import kr.co.person.service.CommentService;

@Service
@Transactional
public class CommentServiceImple implements CommentService {
	@Autowired
	private CommentRepository commentRepository;
	
	public List<Comment> findAllCommentByBoard(int boardIdx){
		if(boardIdx == 0){
			return new ArrayList<Comment>();
		}
		return commentRepository.findAllByBoardIdx(boardIdx);
	}
}
