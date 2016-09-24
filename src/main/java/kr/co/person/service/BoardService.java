package kr.co.person.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.co.person.domain.Board;
import kr.co.person.domain.BoardLike;
import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;

public interface BoardService {
	OkCheck write(String title, String content, int userIdx);
	Page<Board> findAll(Pageable pageable);
	Board findBoardForIdx(int idx);
	boolean update(int idx, String title, String content);
	boolean addHitCount(int boardIdx);
	int getBoardLikeCount(int boardIdx);
	BoardLike getBoardLike(int boardIdx, User user);
	boolean addBoardLike(int boardIdx, User user);
	boolean removeBoardLike(int boardIdx, User user);
}
