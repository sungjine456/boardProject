package kr.co.person.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.person.domain.Board;
import kr.co.person.domain.Comment;
import kr.co.person.domain.User;
import kr.co.person.repository.BoardRepository;
import kr.co.person.repository.CommentRepository;
import kr.co.person.repository.UserRepository;
import kr.co.person.service.CommentService;

@Service
@Transactional
public class CommentServiceImple implements CommentService {
	private static final Logger log = LoggerFactory.getLogger(CommentServiceImple.class);

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private UserRepository userRepository;
	
	public List<Comment> findAllCommentByBoard(int boardIdx){
		if(boardIdx == 0){
			return new ArrayList<Comment>();
		}
		return commentRepository.findAllByBoardIdx(boardIdx);
	}

	@Override
	public boolean save(String commentLine, int userIdx, int boardIdx) {
		Date date = new Date();
		User writer = userRepository.findOne(userIdx);
		Board board = boardRepository.findOne(boardIdx);
		Comment comment = new Comment(commentLine, writer, board, date, date);
		comment = commentRepository.save(comment);
		return false;
	}
}
