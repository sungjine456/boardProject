package kr.co.person.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
		log.info("CommentServiceImple findAllCommentByBoard execute");
		if(boardIdx == 0){
			return new ArrayList<Comment>();
		}
		return commentRepository.findAllByBoardIdx(boardIdx);
	}

	@Override
	public boolean write(String commentSentence, int userIdx, int boardIdx) {
		log.info("CommentServiceImple write execute");
		if(userIdx == 0 || boardIdx == 0){
			return false;
		}
		if(StringUtils.isEmpty(commentSentence)){
			return false;
		}
		Date date = new Date();
		User writer = userRepository.findOne(userIdx);
		Board board = boardRepository.findOne(boardIdx);
		if(writer == null || board == null){
			return false;
		}
		Comment saveComment = new Comment(commentSentence, 0, 0, 0, writer, board, date, date);
		commentRepository.save(saveComment);
		return true;
	}

	@Override
	public boolean update(int idx, String commentSentence) {
		log.info("CommentServiceImple update execute");
		if(idx == 0){
			return false;
		}
		Comment comment = commentRepository.findOne(idx);
		if(comment == null){
			return false;
		}
		if(StringUtils.isEmpty(commentSentence)){
			return false;
		}
		comment.setComment(commentSentence);
		comment.setUpDate(new Date());
		comment = commentRepository.save(comment);
		return true;
	}

	@Override
	public boolean replyWrite(int idx, String commentSentence, int userIdx, int boardIdx) {
		log.info("CommentServiceImple replyWrite execute");
		if(idx == 0){
			return false;
		}
		Comment comment = commentRepository.findOne(idx);
		if(comment == null){
			return false;
		}
		if(userIdx == 0 || boardIdx == 0){
			return false;
		}
		if(StringUtils.isEmpty(commentSentence)){
			return false;
		}
		int group = 0;
		Date date = new Date();
		User writer = userRepository.findOne(userIdx);
		Board board = boardRepository.findOne(boardIdx);
		if(comment.getCircle() == 0){
			group = comment.getIdx();
		} else {
			group = comment.getCircle();
		}
		if(writer == null || board == null){
			return false;
		}
		Comment reply = new Comment(commentSentence, group, comment.getLevel()+1, comment.getDepth()+1, writer, board, date, date);
		commentRepository.save(reply);
		return true;
	}
}
