package kr.co.person.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.person.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	List<Comment> findByBoardIdx(int boardIdx);
	List<Comment> findByBoardIdxOrderByCircleDescStepAsc(int boardIdx);
	List<Comment> findByBoardIdxAndCircleOrderByStepDesc(int boardIdx, int circle);
	List<Comment> findByBoardIdxAndCircleAndStepGreaterThanAndDepthLessThanEqualOrderByStepAsc(int boardIdx, int circle, int step, int depth);
}
