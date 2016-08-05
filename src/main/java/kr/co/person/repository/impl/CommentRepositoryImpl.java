package kr.co.person.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import kr.co.person.domain.Board;
import kr.co.person.domain.Comment;
import kr.co.person.repository.custom.CommentRepositoryCustom;

@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {

	@PersistenceContext private EntityManager em;
	
	@Override
	public List<Comment> getCommentList(int boardIdx) {
		Board board = (Board) em.createQuery("select b from Board b where b.idx = :boardIdx", Board.class).setParameter("boardIdx", boardIdx)
				.getSingleResult();
		return em.createQuery("select c from Comment c where c.board = :board order by c.circle desc, c.step asc, c.idx desc", Comment.class)
				.setParameter("board", board)
				.getResultList();
	}

	@Override
	public List<Comment> getCommentList(int boardIdx, int circle) {
		Board board = (Board) em.createQuery("select b from Board b where b.idx = :boardIdx", Board.class).setParameter("boardIdx", boardIdx).getSingleResult();
		return em.createQuery("select c from Comment c where c.board = :board and c.circle = :circle order by c.step desc", Comment.class)
				.setParameter("board", board)
				.setParameter("circle", circle)
				.getResultList();
	}

	@Override
	public List<Comment> getCommentList(int boardIdx, int circle, int step) {
		Board board = (Board) em.createQuery("select b from Board b where b.idx = :boardIdx", Board.class).setParameter("boardIdx", boardIdx).getSingleResult();
		return em.createQuery("select c from Comment c where c.board = :board and c.circle = :circle and c.step > :step", Comment.class)
				.setParameter("board", board)
				.setParameter("circle", circle)
				.setParameter("step", step)
				.getResultList();
	}

	@Override
	public List<Comment> getCommentList(int boardIdx, int circle, int step, int depth) {
		Board board = (Board) em.createQuery("select b from Board b where b.idx = :boardIdx", Board.class).setParameter("boardIdx", boardIdx).getSingleResult();
		return em.createQuery("select c from Comment c where c.board = :board and c.circle = :circle and c.step > :step and c.depth <= :depth order by c.step asc", Comment.class)
				.setParameter("board", board)
				.setParameter("circle", circle)
				.setParameter("step", step)
				.setParameter("depth", depth)
				.getResultList();
	}
}
