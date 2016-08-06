package kr.co.person.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "board_like")
public class BoardLike {

	@Id
	@Column(name = "idx")
	@GeneratedValue
	private int idx;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_idx")
	private Board board;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_idx")
	private User user;
	
	public BoardLike(){
	}
	public BoardLike(Board board, User user){
		this.board = board;
		this.user = user;
	}

	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
