package kr.co.person.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import kr.co.person.domain.Board;
import kr.co.person.domain.Comment;
import kr.co.person.domain.QComment;
import kr.co.person.repository.BoardRepository;
import kr.co.person.repository.custom.CommentRepositoryCustom;

@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {

	@Autowired BoardRepository boardRepository;
	@PersistenceContext private EntityManager em;

	@Override
	public List<Comment> getCommentList(int boardIdx, int circle) {
		JPAQuery query = new JPAQuery(em);
		Board board = boardRepository.findOne(boardIdx);
		QComment qComment = new QComment("c");
		return query.from(qComment)
				.where(qComment.board.eq(board).and(qComment.circle.eq(circle)))
				.orderBy(qComment.step.desc())
				.list(qComment);
	}

	@Override
	public List<Comment> getCommentList(int boardIdx, int circle, int step) {
		JPAQuery query = new JPAQuery(em);
		Board board = boardRepository.findOne(boardIdx);
		QComment qComment = new QComment("c");
		return query.from(qComment)
				.where(qComment.board.eq(board).and(qComment.circle.eq(circle)).and(qComment.step.gt(step)))
				.list(qComment);
	}

	@Override
	public List<Comment> getCommentList(int boardIdx, int circle, int step, int depth) {
		JPAQuery query = new JPAQuery(em);
		Board board = boardRepository.findOne(boardIdx);
		QComment qComment = new QComment("c");
		return query.from(qComment)
				.where(qComment.board.eq(board).and(qComment.circle.eq(circle)).and(qComment.step.gt(step)).and(qComment.depth.loe(depth)))
				.orderBy(qComment.step.asc())
				.list(qComment);
	}

	@Override
	@Transactional
	public void saveComment(Comment comment) {
		JPAQuery query = new JPAQuery(em);
		em.persist(comment);
		QComment qComment = new QComment("c");
		Comment zeroCircleComment = query.from(qComment)
										.where(qComment.circle.eq(0))
										.uniqueResult(qComment);
		zeroCircleComment.setCircle(zeroCircleComment.getIdx());
	}

	@Override
	@Transactional
	public void increaseCommentIdx(int boardIdx, int circle, int step) {
		Board board = boardRepository.findOne(boardIdx);
		QComment qComment = new QComment("c");
		new JPAUpdateClause(em, qComment).where(qComment.board.eq(board).and(qComment.circle.eq(circle)).and(qComment.step.gt(step)))
										.set(qComment.step, qComment.step.add(1))
										.execute();
	}
}
