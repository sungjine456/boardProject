package kr.co.person.pojo;

public class BoardLikeCount {
	private int boardIdx;
	private int userIdx;
	
	public BoardLikeCount(){
	}
	public BoardLikeCount(int boardIdx, int userIdx){
		this.boardIdx = boardIdx;
		this.userIdx = userIdx;
	}

	public int getBoardIdx() {
		return boardIdx;
	}
	public void setBoardIdx(int boardIdx) {
		this.boardIdx = boardIdx;
	}
	public int getUserIdx() {
		return userIdx;
	}
	public void setUserIdx(int userIdx) {
		this.userIdx = userIdx;
	}
}
