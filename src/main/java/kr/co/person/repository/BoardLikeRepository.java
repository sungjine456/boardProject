package kr.co.person.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.person.domain.BoardLike;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Integer> {
	BoardLike findByBoardIdxAndUserIdx(int boardIdx, int userIdx);
	List<BoardLike> findByBoardIdx(int boardIdx);
}
