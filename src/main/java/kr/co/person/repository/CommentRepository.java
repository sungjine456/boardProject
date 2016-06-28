package kr.co.person.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.person.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	List<Comment> findAllByBoardIdx(int boardIdx);
}
