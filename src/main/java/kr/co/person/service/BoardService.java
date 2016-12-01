package kr.co.person.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.co.person.domain.Board;
import kr.co.person.domain.BoardLike;
import kr.co.person.domain.User;
import kr.co.person.pojo.OkObjectCheck;

public interface BoardService {
	boolean write(String title, String content, int userIdx);
	Page<Board> findAll(Pageable pageable);
	OkObjectCheck<Board> findBoardForIdx(int idx);
	boolean isNotBoardForIdx(int boardIdx);
	boolean update(int idx, String title, String content);
	boolean addHitCount(int boardIdx);
	int getBoardLikeCount(int boardIdx);
	BoardLike getBoardLike(int boardIdx, int userIdx);
	boolean addBoardLike(int boardIdx, User user);
	boolean removeBoardLike(int boardIdx, int userIdx);
}
