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
	public boolean isBool() {
		return bool;
	}
}
