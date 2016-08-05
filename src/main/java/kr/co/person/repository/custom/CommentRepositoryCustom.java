package kr.co.person.repository.custom;

import java.util.List;

import kr.co.person.domain.Comment;

public interface CommentRepositoryCustom {
	List<Comment> getCommentList(int boardIdx);
	List<Comment> getCommentList(int boardIdx, int circle);
	List<Comment> getCommentList(int boardIdx, int circle, int step);
	List<Comment> getCommentList(int boardIdx, int circle, int step, int depth);
}
