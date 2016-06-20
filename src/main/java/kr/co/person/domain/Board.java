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
@Table(name = "board")
public class Board {
	@Id
	@Column(name = "board_idx")
	@GeneratedValue
	private int idx;
	@Column(name="title", nullable = false)
	private String title;
	@Column(name="content", nullable = false)
	private String content;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="writer")
	private User user;
	@Column(name="regDate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date regDate;
	@Column(name="upDate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date upDate;
	
	public Board(){
	}
	public Board(String title, String content, User user, Date regDate, Date upDate){
		this.title = title;
		this.content = content;
		this.user = user;
		this.regDate = regDate;
		this.upDate = upDate;
	}

	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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
