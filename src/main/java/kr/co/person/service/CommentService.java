package kr.co.person.service;

import java.util.List;

import kr.co.person.domain.Comment;

public interface CommentService {
	List<Comment> findAllCommentByBoard(int boardIdx);
}
