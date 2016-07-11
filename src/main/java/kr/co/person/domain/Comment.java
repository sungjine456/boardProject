package kr.co.person.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
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
	@Column(name="level", nullable = false)
	private int level;
	@Column(name="depth", nullable = false)
	private int depth;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="writer")
	private User writer;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="board")
	private Board board;
	@Column(name="reg_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date regDate;
	@Column(name="up_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date upDate;
	
	public Comment(){
	}
	public Comment(String comment, int circle, int level, int depth, User writer, Board board, Date regDate, Date upDate){
		this.comment = comment;
		this.circle = circle;
		this.level = level;
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
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
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
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public Date getUpDate() {
		return upDate;
	}
	public void setUpDate(Date upDate) {
		this.upDate = upDate;
	}
}
