package kr.co.person.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CommentServiceImple implements CommentService {
	private static final Logger log = LoggerFactory.getLogger(CommentServiceImple.class);

	@Autowired private CommentRepository commentRepository;
	@Autowired private BoardRepository boardRepository;
	@Autowired private UserRepository userRepository;
	
	public List<Comment> findAllCommentByBoard(int boardIdx){
		log.info("CommentServiceImple findAllCommentByBoard execute");
		if(IsValid.isNotValidInts(boardIdx)){
			return new ArrayList<Comment>();
		}
		List<Comment> commentList0 = commentRepository.findAllByBoardIdx(boardIdx);
		List<Comment> commentList1 = new ArrayList<Comment>();
		List<Comment> commentList2 = new ArrayList<Comment>();
		List<Comment> commentList3 = new ArrayList<Comment>();
		int size0 = commentList0.size();
		for(int i = 0; i < size0; i++){
			if(commentList0.get(i).getCircle() == 0){
				commentList1.add(commentList0.get(i));
			} else {
				commentList2.add(commentList0.get(i));
				
			}
		}
		int size1 = commentList1.size();
		int size2 = commentList2.size();
		for(int i = 0; i < size1; i++){
			commentList3.add(commentList1.get(i));
			for(int j = 0; j < size2; j++){
				if(commentList1.get(i).getIdx() == commentList2.get(j).getCircle()){
					commentList3.add(commentList2.get(j));
				}
			}
		}
		return commentList3;
	}

	@Override
	public boolean write(String commentSentence, int userIdx, int boardIdx) {
		log.info("CommentServiceImple write execute");
		if(IsValid.isNotValidInts(userIdx, boardIdx)){
			return false;
		}
		if(StringUtils.isEmpty(commentSentence)){
			return false;
		}
		User writer = userRepository.findOne(userIdx);
		Board board = boardRepository.findOne(boardIdx);
		if(IsValid.isNotValidObjects(writer, board)){
			return false;
		}
		DateTime date = new DateTime();
		commentRepository.save(new Comment(commentSentence, 0, 0, 0, 0, writer, board, date, date));
		return true;
	}

	@Override
	public boolean update(int idx, String commentSentence) {
		log.info("CommentServiceImple update execute");
		if(IsValid.isNotValidInts(idx)){
			return false;
		}
		Comment comment = commentRepository.findOne(idx);
		if(IsValid.isNotValidObjects(comment)){
			return false;
		}
		if(StringUtils.isEmpty(commentSentence)){
			return false;
		}
		comment.setComment(commentSentence);
		comment.setUpDate(new DateTime());
		commentRepository.save(comment);
		return true;
	}

	@Override
	public boolean replyWrite(int idx, String commentSentence, int userIdx, int boardIdx) {
		log.info("CommentServiceImple replyWrite execute");
		if(IsValid.isNotValidInts(idx, userIdx, boardIdx)){
			return false;
		}
		if(StringUtils.isEmpty(commentSentence)){
			return false;
		}
		Comment comment = commentRepository.findOne(idx);
		User writer = userRepository.findOne(userIdx);
		Board board = boardRepository.findOne(boardIdx);
		if(IsValid.isNotValidObjects(comment, writer, board)){
			return false;
		}
		int group = (IsValid.isNotValidInts(comment.getCircle()))?comment.getIdx():comment.getCircle();
		DateTime date = new DateTime();
		commentRepository.save(new Comment(commentSentence, idx, group, comment.getLevel()+1, comment.getDepth()+1, writer, board, date, date));
		return true;
	}
}
