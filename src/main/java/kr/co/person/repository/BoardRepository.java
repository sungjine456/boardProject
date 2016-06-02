package kr.co.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.person.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {

}
