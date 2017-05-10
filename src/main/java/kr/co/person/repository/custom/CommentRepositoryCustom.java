package kr.co.person.repository.custom;

import java.util.List;

import kr.co.person.domain.Board;
import kr.co.person.domain.Comment;

public interface CommentRepositoryCustom {
	void saveComment(Comment comment);
	void increaseCommentIdx(Board board, int circle, int step);
	List<Comment> getCommentList(Board board, int circle, int step, int depth);
}
