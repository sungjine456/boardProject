package kr.co.person.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.person.domain.Comment;
import kr.co.person.repository.custom.CommentRepositoryCustom;

public interface CommentRepository extends JpaRepository<Comment, Integer>, CommentRepositoryCustom {
	Page<Comment> findByBoardIdx(int boardIdx, Pageable pageable);
}
