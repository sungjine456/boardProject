package kr.co.person.service;

import java.util.List;

import kr.co.person.domain.Board;
import kr.co.person.domain.OkCheck;

public interface BoardService {
	OkCheck write(String title, String content, int userIdx);
	List<Board> findAll();
	Board findOne(int idx);
	boolean update(int idx, String title, String content);
}
