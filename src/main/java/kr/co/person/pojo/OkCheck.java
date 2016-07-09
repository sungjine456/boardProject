package kr.co.person.pojo;

public class OkCheck {
	private String message;
	private boolean bool;
	
	public OkCheck(){
	}
	public OkCheck(String message, boolean bool){
		this.message = message;
		this.bool = bool;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isBool() {
		return bool;
	}
	public void setBool(boolean bool) {
		this.bool = bool;
	}
}
