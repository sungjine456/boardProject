package kr.co.person.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.person.common.IsValid;
import kr.co.person.domain.Board;
import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;
import kr.co.person.repository.BoardRepository;
import kr.co.person.repository.UserRepository;
import kr.co.person.service.BoardService;

@Service
@Transactional
public class BoardServiceImple implements BoardService {
	private static final Logger log = LoggerFactory.getLogger(BoardServiceImple.class);

	@Autowired private BoardRepository boardRepository;
	@Autowired private UserRepository userRepository;
	
	@Override
	public OkCheck write(String title, String content, int userIdx) {
		log.info("execute BoardService save");
		if(StringUtils.isEmpty(title)){
			return new OkCheck("제목을 입력해주세요.", false);
		}
		if(StringUtils.isEmpty(content)){
			return new OkCheck("내용을 입력해주세요.", false);
		}
		if(IsValid.isNotValidInts(userIdx)){
			return new OkCheck("유효한 회원이 아닙니다.", false);
		}
		DateTime date = new DateTime();
		User user = userRepository.findOne(userIdx);
		if(IsValid.isNotValidObjects(user)){
			return new OkCheck("유효한 회원이 아닙니다.", false);
		}
		Board board = new Board(title, content, user, date, date);
		boardRepository.save(board);
		
		return new OkCheck("글이 등록 되었습니다.", true);
	}

	@Override
	public Page<Board> findAll(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}

	@Override
	public Board findBoardForIdx(int idx) {
		return boardRepository.findOne(idx);
	}

	@Override
	public boolean update(int idx, String title, String content) {
		if(IsValid.isNotValidInts(idx)){
			return false;
		}
		if(StringUtils.isEmpty(title)){
			return false;
		}
		if(StringUtils.isEmpty(content)){
			return false;
		}
		Board board = boardRepository.findOne(idx);
		if(IsValid.isNotValidObjects(board)){
			return false;
		}
		board.setContent(content);
		board.setTitle(title);
		board.setUpDate(new DateTime());
		boardRepository.save(board);
		return true;
	}
}
