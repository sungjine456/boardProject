package kr.co.person.pojo;

public class CommentNum {
	private int idx;
	private String comment;
	
	public CommentNum(){
	}
	public CommentNum(int idx, String comment){
		this.idx = idx;
		this.comment = comment;
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
