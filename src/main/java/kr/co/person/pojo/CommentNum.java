package kr.co.person.pojo;

public class CommentNum {
	private int num;
	private int idx;
	private String comment;
	
	public CommentNum(){
	}
	public CommentNum(int num, int idx, String comment){
		this.num = num;
		this.idx = idx;
		this.comment = comment;
	}
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
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
}
