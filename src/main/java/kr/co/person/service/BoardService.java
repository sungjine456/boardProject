package kr.co.person.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.co.person.domain.Board;
import kr.co.person.domain.BoardLike;
import kr.co.person.pojo.OkCheck;

public interface BoardService {
	OkCheck write(String title, String content, int userIdx);
	Page<Board> findAll(Pageable pageable);
	Board findBoardForIdx(int idx);
	boolean update(int idx, String title, String content);
	boolean addHitCount(int boardIdx);
	int getBoardLikeCount(int boardIdx);
	BoardLike getBoardLike(int boardIdx, int userIdx);
	boolean addBoardLike(int boardIdx, int userIdx);
	boolean removeBoardLike(int boardIdx, int userIdx);
}
