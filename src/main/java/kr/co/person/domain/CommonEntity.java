package kr.co.person.domain;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@MappedSuperclass
public class CommonEntity {
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime regDate;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime updateDate;
	
	public CommonEntity(){
	}
	public CommonEntity(DateTime regDate, DateTime updateDate){
		this.regDate = regDate;
		this.updateDate = updateDate;
	}
	
	public DateTime getRegDate() {
		return regDate;
	}
	public void setRegDate(DateTime regDate) {
		this.regDate = regDate;
	}
	public DateTime getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(DateTime updateDate) {
		this.updateDate = updateDate;
	}
}
