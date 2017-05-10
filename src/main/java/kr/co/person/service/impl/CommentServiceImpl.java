package kr.co.person.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.person.common.IsValid;
import kr.co.person.domain.Board;
import kr.co.person.domain.Comment;
import kr.co.person.domain.User;
import kr.co.person.repository.BoardRepository;
import kr.co.person.repository.CommentRepository;
import kr.co.person.repository.UserRepository;
import kr.co.person.service.CommentService;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
	private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

	@Autowired private CommentRepository commentRepository;
	@Autowired private BoardRepository boardRepository;
	@Autowired private UserRepository userRepository;
	
	public Page<Comment> findAllCommentByBoard(int boardIdx, Pageable pageable){
		log.info("execute CommentServiceImple findAllCommentByBoard");
		return commentRepository.findByBoardIdx(boardIdx, pageable);
	}

	@Override
	public boolean write(String commentSentence, int userIdx, int boardIdx) {
		log.info("execute CommentServiceImple write");
		User writer = userRepository.findOne(userIdx);
		Board board = boardRepository.findOne(boardIdx);
		if(IsValid.isNotValidUser(writer) || IsValid.isNotValidBoard(board)){
			return false;
		}
		LocalDateTime date = LocalDateTime.now();
		commentRepository.saveComment(new Comment(commentSentence, 0, 0, 0, writer, board, date, date));
		return true;
	}

	@Override
	public boolean update(int idx, String commentSentence) {
		log.info("execute CommentServiceImple update");
		Comment comment = commentRepository.findOne(idx);
		if(IsValid.isNotValidObjects(comment)){
			return false;
		}
		comment.setComment(commentSentence);
		comment.setUpdateDate(LocalDateTime.now());
		return true;
	}

	@Override
	public boolean replyWrite(int idx, String commentSentence, int userIdx, int boardIdx) {
		log.info("execute CommentServiceImple replyWrite");
		Comment comment = commentRepository.findOne(idx);
		User writer = userRepository.findOne(userIdx);
		Board board = boardRepository.findOne(boardIdx);
		if(IsValid.isNotValidUser(writer) || IsValid.isNotValidBoard(board) || IsValid.isNotValidObjects(comment)){
			return false;
		}
		int circle = comment.getCircle();
		LocalDateTime date = LocalDateTime.now();
		int step = comment.getStep();
		
		commentRepository.increaseCommentIdx(board, circle, step);
		commentRepository.save(new Comment(commentSentence, circle, step + 1, comment.getDepth()+1, writer, board, date, date));
		return true;
	}
}
