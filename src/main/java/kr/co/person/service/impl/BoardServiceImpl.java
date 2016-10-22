package kr.co.person.service.impl;

import java.util.List;

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
import kr.co.person.common.Message;
import kr.co.person.domain.Board;
import kr.co.person.domain.BoardLike;
import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;
import kr.co.person.repository.BoardLikeRepository;
import kr.co.person.repository.BoardRepository;
import kr.co.person.repository.UserRepository;
import kr.co.person.service.BoardService;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {
	private static final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class);

	@Autowired private BoardRepository boardRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private BoardLikeRepository boardLikeRepository;
	@Autowired private Message message;
	
	@Override
	public OkCheck write(String title, String content, int userIdx) {
		log.info("execute BoardServiceImpl write");
		if(StringUtils.isEmpty(title)){
			return new OkCheck(message.BOARD_NO_TITLE, false);
		}
		if(StringUtils.isEmpty(content)){
			return new OkCheck(message.BOARD_NO_CONTENT, false);
		}
		if(IsValid.isNotValidInts(userIdx)){
			return new OkCheck(message.USER_WRONG_USER, false);
		}
		DateTime date = new DateTime();
		User user = userRepository.findOne(userIdx);
		if(IsValid.isNotValidObjects(user)){
			return new OkCheck(message.USER_WRONG_USER, false);
		}
		Board board = new Board(title, content, user, date, date);
		boardRepository.save(board);
		
		return new OkCheck(message.BOARD_SUCCESS_WRITE, true);
	}

	@Override
	public Page<Board> findAll(Pageable pageable) {
		log.info("execute BoardServiceImpl findAll");
		if(IsValid.isNotValidObjects(pageable)){
			return null;
		}
		return boardRepository.findAll(pageable);
	}

	@Override
	public Board findBoardForIdx(int idx) {
		log.info("execute BoardServiceImpl findBoardForIdx");
		if(IsValid.isNotValidInts(idx)){
			return new Board();
		}
		return boardRepository.findOne(idx);
	}

	@Override
	public boolean update(int idx, String title, String content) {
		log.info("execute BoardServiceImpl update");
		if(IsValid.isNotValidInts(idx) || StringUtils.isEmpty(title) || StringUtils.isEmpty(content)){
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

	@Override
	public boolean addHitCount(int boardIdx) {
		log.info("execute BoardServiceImple addHitCount");
		if(IsValid.isNotValidInts(boardIdx)){
			return false;
		}
		Board board = boardRepository.findOne(boardIdx);
		if(IsValid.isNotValidObjects(board)){
			return false;
		}
		board.setHitCount(board.getHitCount() + 1);
		boardRepository.save(board);
		return true;
	}

	@Override
	public int getBoardLikeCount(int boardIdx) {
		log.info("execute BoardServiceImple getBoardLikeCount");
		if(IsValid.isNotValidInts(boardIdx)){
			return -1;
		}
		Board board = boardRepository.findOne(boardIdx);
		List<BoardLike> boardLikes = boardLikeRepository.findByBoardIdx(boardIdx);
		if(IsValid.isNotValidObjects(board, boardLikes)){
			return -1;
		}
		return boardLikes.size();
	}

	@Override
	public BoardLike getBoardLike(int boardIdx, User user) {
		log.info("execute BoardServiceImple getBoardLike");
		if(IsValid.isNotValidObjects(user) || IsValid.isNotValidInts(boardIdx, user.getIdx())){
			return null;
		}
		BoardLike like = boardLikeRepository.findByBoardIdxAndUserIdx(boardIdx, user.getIdx());
		if(IsValid.isNotValidObjects(like)){
			return null;
		}
		return like;
	}

	@Override
	public boolean addBoardLike(int boardIdx, User user) {
		log.info("execute BoardServiceImple addBoardLike");
		if(IsValid.isNotValidObjects(user) || IsValid.isNotValidInts(boardIdx, user.getIdx())){
			return false;
		}
		BoardLike like = boardLikeRepository.findByBoardIdxAndUserIdx(boardIdx, user.getIdx());
		if(IsValid.isValidObjects(like)){
			return false;
		}
		Board board = boardRepository.findOne(boardIdx);
		User userCheck = userRepository.findOne(user.getIdx());
		if(IsValid.isNotValidObjects(board, userCheck)){
			return false;
		}
		like = new BoardLike(board, user);
		boardLikeRepository.save(like);
		return true;
	}

	@Override
	public boolean removeBoardLike(int boardIdx, User user) {
		log.info("execute BoardServiceImple removeBoardLike");
		if(IsValid.isNotValidObjects(user) || IsValid.isNotValidInts(boardIdx, user.getIdx())){
			return false;
		}
		BoardLike like = boardLikeRepository.findByBoardIdxAndUserIdx(boardIdx, user.getIdx());
		if(IsValid.isNotValidObjects(like)){
			return false;
		}
		boardLikeRepository.delete(like);
		return true;
	}
}
