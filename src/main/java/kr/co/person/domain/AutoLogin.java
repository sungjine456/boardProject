package kr.co.person.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;

import kr.co.person.pojo.AutoLoginId;

@Entity
@DynamicUpdate
@Table(name = "auto_login")
@IdClass(AutoLoginId.class)
public class AutoLogin{
	@Id
	@Column(name="reg_ip", nullable = false)
	private String regIp;
	@Id
	@Column(name="reg_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date regDate;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_idx")
    private User user;
	@Column(name="auto_login_check", nullable = false)
	private String loginCheck;

	public AutoLogin() {
	}
	
	public AutoLogin(User user, String loginCheck, String regIp, Date regDate) {
		this.user = user;
		this.loginCheck = loginCheck;
		this.regIp = regIp;
		this.regDate = regDate;
	}
	
	public String getLoginCheck() {
		return loginCheck;
	}
	public void setLoginCheck(String loginCheck) {
		this.loginCheck = loginCheck;
	}
	public String getRegIp() {
		return regIp;
	}
	public void setRegIp(String regIp) {
		this.regIp = regIp;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
