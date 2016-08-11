package kr.co.person.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import kr.co.person.domain.Board;
import kr.co.person.domain.Comment;
import kr.co.person.repository.custom.CommentRepositoryCustom;

@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {

	@PersistenceContext private EntityManager em;

	@Override
	public List<Comment> getCommentList(int boardIdx, int circle) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Board> boardcq = cb.createQuery(Board.class);
		Root<Board> b = boardcq.from(Board.class);
		Predicate boardIdxEqual = cb.equal(b.get("idx"), boardIdx);
		boardcq.select(b)
			.where(boardIdxEqual);
		Board board = em.createQuery(boardcq).getSingleResult();
		
		CriteriaQuery<Comment> commentcq = cb.createQuery(Comment.class);
		Root<Comment> c = commentcq.from(Comment.class);
		commentcq.select(c)
			.where(cb.equal(c.get("board"), board), cb.equal(c.get("circle"), circle))
			.orderBy(cb.desc(c.get("step")));
		
		return em.createQuery(commentcq).getResultList();
	}

	@Override
	public List<Comment> getCommentList(int boardIdx, int circle, int step) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Board> boardcq = cb.createQuery(Board.class);
		Root<Board> b = boardcq.from(Board.class);
		Predicate boardIdxEqual = cb.equal(b.get("idx"), boardIdx);
		boardcq.select(b)
			.where(boardIdxEqual);
		Board board = em.createQuery(boardcq).getSingleResult();
		
		CriteriaQuery<Comment> commentcq = cb.createQuery(Comment.class);
		Root<Comment> c = commentcq.from(Comment.class);
		commentcq.select(c)
			.where(cb.equal(c.get("board"), board), cb.equal(c.get("circle"), circle), cb.greaterThan(c.get("step"), step));
		
		return em.createQuery(commentcq).getResultList();
	}

	@Override
	public List<Comment> getCommentList(int boardIdx, int circle, int step, int depth) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Board> boardcq = cb.createQuery(Board.class);
		Root<Board> b = boardcq.from(Board.class);
		Predicate boardIdxEqual = cb.equal(b.get("idx"), boardIdx);
		boardcq.select(b)
			.where(boardIdxEqual);
		Board board = em.createQuery(boardcq).getSingleResult();
		
		CriteriaQuery<Comment> commentcq = cb.createQuery(Comment.class);
		Root<Comment> c = commentcq.from(Comment.class);
		commentcq.select(c)
			.where(cb.equal(c.get("board"), board), cb.equal(c.get("circle"), circle), cb.greaterThan(c.get("step"), step), cb.lessThanOrEqualTo(c.get("depth"), depth))
			.orderBy(cb.asc(c.get("step")));
		
		return em.createQuery(commentcq).getResultList();
	}

	@Override
	@Transactional
	public void saveComment(Comment comment) {
		em.persist(comment);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Comment> cq = cb.createQuery(Comment.class);
		Root<Comment> c = cq.from(Comment.class);
		cq.select(c)
			.where(cb.equal(c.get("circle"), 0));
		Comment zeroCircleComment = em.createQuery(cq).getSingleResult();
		zeroCircleComment.setCircle(zeroCircleComment.getIdx());
	}

	@Override
	@Transactional
	public void updateComment(int boardIdx, int circle, int step) {
		Board board = (Board) em.createQuery("select b from Board b where b.idx = :boardIdx", Board.class).setParameter("boardIdx", boardIdx).getSingleResult();
		em.createQuery("update Comment c set c.step = (c.step + 1) where c.board = :board and c.circle = :circle and c.step > :step")
			.setParameter("board", board)
			.setParameter("circle", circle)
			.setParameter("step", step)
			.executeUpdate();
	}
}
