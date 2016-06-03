package kr.co.person.service;

import kr.co.person.domain.OkCheck;

public interface BoardService {
	OkCheck save(String title, String content, int userIdx);
}
