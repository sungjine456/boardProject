package kr.co.person.service;

import java.util.List;

import kr.co.person.domain.Comment;

public interface CommentService {
	List<Comment> findAllCommentByBoard(int boardIdx);
	boolean write(String comment, int userIdx, int boardIdx);
	boolean update(int idx, String comment);
	boolean replyWrite(int idx, String comment, int userIdx, int boardIdx);
}
