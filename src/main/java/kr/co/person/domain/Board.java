package kr.co.person.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.joda.time.DateTime;

import kr.co.person.pojo.CommonEntity;

@Entity
@Table(name = "board")
@AttributeOverrides({
	@AttributeOverride(name = "regDate", column = @Column(name = "reg_date", nullable = false)),
	@AttributeOverride(name = "upDate", column = @Column(name = "up_date", nullable = false))
})
public class Board extends CommonEntity{
	@Id
	@Column(name = "board_idx")
	@GeneratedValue
	private int idx;
	@Column(name = "title", nullable = false)
	private String title;
	@Column(name = "content", nullable = false)
	private String content;
	@Column(name = "hit_count", nullable = false)
	private int hitCount;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "writer")
	private User user;
	
	public Board(){
	}
	public Board(String title, String content, User user, DateTime regDate, DateTime upDate){
		super(regDate, upDate);
		this.title = title;
		this.content = content;
		this.user = user;
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
	public int getHitCount() {
		return hitCount;
	}
	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
