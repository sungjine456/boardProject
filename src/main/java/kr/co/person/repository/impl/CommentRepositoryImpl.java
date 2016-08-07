package kr.co.person.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
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
	public List<Comment> getCommentList(int boardIdx) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Board> boardcq = cb.createQuery(Board.class);
		Root<Board> b = boardcq.from(Board.class);
		Predicate boardIdxEqual = cb.equal(b.get("idx"), boardIdx);
		boardcq.select(b)
			.where(boardIdxEqual);
		Board board = em.createQuery(boardcq).getSingleResult();
		
		CriteriaQuery<Comment> commentcq = cb.createQuery(Comment.class);
		Root<Comment> c = commentcq.from(Comment.class);
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(c.get("circle")));
		orderList.add(cb.asc(c.get("step")));
		commentcq.select(c)
			.where(cb.equal(c.get("board"), board))
			.orderBy(orderList);
			
		return em.createQuery(commentcq).getResultList();
	}

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
		List<Comment> comments = getCommentList(boardIdx, circle, step);
		int size = comments.size();
		for(int i = 0; i < size; i++){
			Comment comment = comments.get(i);
			comment.setStep(comment.getStep() + 1);
		}
	}
}
