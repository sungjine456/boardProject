package kr.co.person.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;

import kr.co.person.domain.Board;
import kr.co.person.domain.Comment;
import kr.co.person.domain.QComment;
import kr.co.person.repository.custom.CommentRepositoryCustom;

@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {

	@PersistenceContext private EntityManager em;

	@Override
	public List<Comment> getCommentList(Board board, int circle, int step, int depth) {
		JPAQuery<Comment> query = new JPAQuery<Comment>(em);
		QComment qComment = new QComment("c");
		return query.from(qComment)
				.where(qComment.board.eq(board).and(qComment.circle.eq(circle)).and(qComment.step.gt(step)).and(qComment.depth.loe(depth)))
				.orderBy(qComment.step.asc()).fetch();
	}

	@Override
	@Transactional
	public void saveComment(Comment comment) {
		JPAQuery<Comment> query = new JPAQuery<Comment>(em);
		em.persist(comment);
		QComment qComment = new QComment("c");
		Comment zeroCircleComment = query.from(qComment)
										.where(qComment.circle.eq(0))
										.fetchFirst();
		zeroCircleComment.setCircle(zeroCircleComment.getIdx());
	}

	@Override
	@Transactional
	public void increaseCommentIdx(Board board, int circle, int step) {
		QComment qComment = new QComment("c");
		new JPAUpdateClause(em, qComment).where(qComment.board.eq(board).and(qComment.circle.eq(circle)).and(qComment.step.gt(step)))
										.set(qComment.step, qComment.step.add(1))
										.execute();
	}
}
