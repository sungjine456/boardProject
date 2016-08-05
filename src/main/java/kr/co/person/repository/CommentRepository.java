package kr.co.person.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.person.domain.Comment;
import kr.co.person.repository.custom.CommentRepositoryCustom;

public interface CommentRepository extends JpaRepository<Comment, Integer>, CommentRepositoryCustom {
	List<Comment> findByBoardIdxAndCircleOrderByStepDesc(int boardIdx, int circle);
	List<Comment> findByBoardIdxAndCircleAndStepGreaterThan(int boardIdx, int circle, int step);
	List<Comment> findByBoardIdxAndCircleAndStepGreaterThanAndDepthLessThanEqualOrderByStepAsc(int boardIdx, int circle, int step, int depth);
}
