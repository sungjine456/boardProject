package kr.co.person.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "comment")
public class Comment {
	@Id
	@Column(name = "comment_idx")
	@GeneratedValue
	private int idx;
	@Column(name="comment", nullable = false)
	private String comment;
	@Column(name="circle", nullable = false)
	private int circle;
	@Column(name="step", nullable = false)
	private int step;
	@Column(name="depth", nullable = false)
	private int depth;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="writer")
	private User writer;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="board")
	private Board board;
	@Column(name="reg_date", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime regDate;
	@Column(name="up_date", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime upDate;
	
	public Comment(){
	}	
	public Comment(String comment, int circle, int step, int depth, User writer, Board board, DateTime regDate, DateTime upDate){
		this.comment = comment;
		this.circle = circle;
		this.step = step;
		this.depth = depth;
		this.writer = writer;
		this.board = board;
		this.regDate = regDate;
		this.upDate = upDate;
	}
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getCircle() {
		return circle;
	}
	public void setCircle(int circle) {
		this.circle = circle;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public User getWriter() {
		return writer;
	}
	public void setWriter(User writer) {
		this.writer = writer;
	}
	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	public DateTime getRegDate() {
		return regDate;
	}
	public void setRegDate(DateTime regDate) {
		this.regDate = regDate;
	}
	public DateTime getUpDate() {
		return upDate;
	}
	public void setUpDate(DateTime upDate) {
		this.upDate = upDate;
	}
}
