package kr.co.person.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.person.domain.Board;
import kr.co.person.domain.OkCheck;
import kr.co.person.repository.BoardRepository;
import kr.co.person.service.BoardService;

@Service
public class BoardServiceImple implements BoardService {

	@Autowired
	private BoardRepository boardRepository;
	
	@Override
	public OkCheck save(String title, String content, int userIdx) {
		if(title == null || title.equals("")){
			return new OkCheck("제목을 입력해주세요", false);
		}
		if(content == null || content.equals("")){
			return new OkCheck("내용을 입력해주세요", false);
		}
		Date date = new Date();
		Board board = new Board(title, content, userIdx, date, date);
		boardRepository.save(board);
		
		return null;
	}

}
