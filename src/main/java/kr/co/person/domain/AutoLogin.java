package kr.co.person.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="auto_login")
public class AutoLogin{
	@Id
	@GeneratedValue
	@Column(name="auto_login_idx", nullable=false)
	private int loginIdx;
	@Column(name="auto_login_id", nullable=false)
	private String loginId;
	@Column(name="reg_date", nullable=false)
	@Type(type="kr.co.person.domain.LocalDateTimeUserType")
	private LocalDateTime regDate;
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_idx")
    private User user;

	public AutoLogin() {
	}
	public AutoLogin(String loginId, LocalDateTime regDate, User user) {
		this.loginId = loginId;
		this.regDate = regDate;
		this.user = user;
	}
	
	public int getLoginIdx() {
		return loginIdx;
	}
	public void setLoginIdx(int loginIdx) {
		this.loginIdx = loginIdx;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public LocalDateTime getRegDate() {
		return regDate;
	}
	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
