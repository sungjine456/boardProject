package kr.co.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.person.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
