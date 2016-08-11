package kr.co.person.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
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
public class CommentServiceImple implements CommentService {
	private static final Logger log = LoggerFactory.getLogger(CommentServiceImple.class);

	@Autowired private CommentRepository commentRepository;
	@Autowired private BoardRepository boardRepository;
	@Autowired private UserRepository userRepository;
	
	public Page<Comment> findAllCommentByBoard(int boardIdx, Pageable pageable){
		log.info("execute CommentServiceImple findAllCommentByBoard");
		if(IsValid.isNotValidObjects(pageable)){
			return null;
		}
		if(IsValid.isNotValidInts(boardIdx)){
			return null;
		}
		return commentRepository.findByBoardIdx(boardIdx, pageable);
	}

	@Override
	public boolean write(String commentSentence, int userIdx, int boardIdx) {
		log.info("execute CommentServiceImple write");
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
		commentRepository.saveComment(new Comment(commentSentence, 0, 0, 0, writer, board, date, date));
		return true;
	}

	@Override
	public boolean update(int idx, String commentSentence) {
		log.info("execute CommentServiceImple update");
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
		log.info("execute CommentServiceImple replyWrite");
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
		int circle = (IsValid.isNotValidInts(comment.getCircle()))?comment.getIdx():comment.getCircle();
		DateTime date = new DateTime();
		List<Comment> comments = commentRepository.getCommentList(boardIdx, circle, comment.getStep(), comment.getDepth());
		if(IsValid.isNotValidObjects(comments)){
			return false;
		}
		int step = comment.getStep();
		if(comments.size() == 0){
			List<Comment> maxComments = commentRepository.getCommentList(boardIdx, circle);
			if(IsValid.isNotValidObjects(maxComments)){
				return false;
			}
			int maxSize = maxComments.size();
			if(maxSize != 0){
				commentRepository.updateComment(boardIdx, circle, step);
				commentRepository.save(new Comment(commentSentence, circle, step + 1, comment.getDepth()+1, writer, board, date, date));
			} else {
				commentRepository.save(new Comment(commentSentence, circle, 1, comment.getDepth() + 1, writer, board, date, date));
			}
		} else {
			commentRepository.save(new Comment(commentSentence, circle, step + 1, comment.getDepth() + 1, writer, board, date, date));
			commentRepository.updateComment(boardIdx, circle, step);
		}
		return true;
	}
}
