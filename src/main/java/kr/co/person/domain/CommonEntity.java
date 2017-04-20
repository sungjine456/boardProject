package kr.co.person.domain;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

@MappedSuperclass
public class CommonEntity {
	@Type(type="kr.co.person.domain.LocalDateTimeUserType")
	private LocalDateTime regDate;
	@Type(type="kr.co.person.domain.LocalDateTimeUserType")
	private LocalDateTime updateDate;
	
	public CommonEntity(){
	}
	public CommonEntity(LocalDateTime regDate, LocalDateTime updateDate){
		this.regDate = regDate;
		this.updateDate = updateDate;
	}
	
	public LocalDateTime getRegDate() {
		return regDate;
	}
	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}
	public LocalDateTime getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}
}
