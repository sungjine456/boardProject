package kr.co.person.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.co.person.domain.Comment;

public interface CommentService {
	Page<Comment> findAllCommentByBoard(int boardIdx, Pageable pageable);
	boolean write(String comment, int userIdx, int boardIdx);
	boolean update(int idx, String comment);
	boolean replyWrite(int idx, String comment, int userIdx, int boardIdx);
}
