package kr.co.person.service.impl;

import java.time.LocalDateTime;
import java.util.List;

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
import kr.co.person.pojo.OkObjectCheck;
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
	public boolean write(String title, String content, int userIdx) {
		log.info("execute BoardServiceImpl write");
		LocalDateTime date = LocalDateTime.now();
		User user = userRepository.findOne(userIdx);
		if(IsValid.isNotValidUser(user)){
			return false;
		}
		Board board = new Board(title, content, user, date, date);
		boardRepository.save(board);
		
		return true;
	}

	@Override
	public Page<Board> findAll(Pageable pageable) {
		log.info("execute BoardServiceImpl findAll");
		Page<Board> findBoard = boardRepository.findAll(pageable);
		if(IsValid.isNotValidObjects(findBoard)){
			return null;
		}
		return findBoard;
	}

	@Override
	public OkObjectCheck<Board> findBoardForIdx(int idx) {
		log.info("execute BoardServiceImpl findBoardForIdx");
		Board board = boardRepository.findOne(idx);
		if(IsValid.isNotValidBoard(board)){
			return new OkObjectCheck<Board>(new Board(), message.BOARD_NO_BOARD, false);
		}
		return new OkObjectCheck<Board>(board, "", true);
	}
	
	@Override
	public boolean isNotBoardForIdx(int idx) {
		log.info("execute BoardServiceImpl findBoardForIdx");
		Board findBoard = boardRepository.findOne(idx);
		if(IsValid.isNotValidBoard(findBoard)){
			return true;
		}
		return false;
	}

	@Override
	public boolean update(int idx, String title, String content) {
		log.info("execute BoardServiceImpl update");
		Board board = boardRepository.findOne(idx);
		if(IsValid.isNotValidObjects(board)){
			return false;
		}
		board.setContent(content);
		board.setTitle(title);
		board.setUpdateDate(LocalDateTime.now());
		return true;
	}

	@Override
	public boolean addHitCount(int boardIdx) {
		log.info("execute BoardServiceImple addHitCount");
		Board board = boardRepository.findOne(boardIdx);
		if(IsValid.isNotValidObjects(board)){
			return false;
		}
		board.setHitCount(board.getHitCount() + 1);
		return true;
	}

	@Override
	public int getBoardLikeCount(int boardIdx) {
		log.info("execute BoardServiceImple getBoardLikeCount");
		Board board = boardRepository.findOne(boardIdx);
		if(IsValid.isNotValidBoard(board)){
			return -1;
		}
		List<BoardLike> boardLikes = boardLikeRepository.findByBoardIdx(boardIdx);
		if(IsValid.isNotValidObjects(boardLikes)){
			return -1;
		}
		return boardLikes.size();
	}

	@Override
	public BoardLike getBoardLike(int boardIdx, int userIdx) {
		log.info("execute BoardServiceImple getBoardLike");
		BoardLike like = boardLikeRepository.findByBoardIdxAndUserIdx(boardIdx, userIdx);
		if(IsValid.isNotValidObjects(like)){
			return null;
		}
		return like;
	}

	@Override
	public boolean addBoardLike(int boardIdx, int userIdx) {
		log.info("execute BoardServiceImple addBoardLike");
		Board board = boardRepository.findOne(boardIdx);
		User user = userRepository.findOne(userIdx);
		if(IsValid.isNotValidBoard(board) || IsValid.isNotValidUser(user)){
			return false;
		}
		BoardLike like = boardLikeRepository.findByBoardIdxAndUserIdx(boardIdx, userIdx);
		if(IsValid.isValidObjects(like)){
			return false;
		}
		boardLikeRepository.save(new BoardLike(board, user));
		return true;
	}

	@Override
	public boolean removeBoardLike(int boardIdx, int userIdx) {
		log.info("execute BoardServiceImple removeBoardLike");
		BoardLike like = boardLikeRepository.findByBoardIdxAndUserIdx(boardIdx, userIdx);
		if(IsValid.isNotValidObjects(like)){
			return false;
		}
		boardLikeRepository.delete(like);
		return true;
	}
}
