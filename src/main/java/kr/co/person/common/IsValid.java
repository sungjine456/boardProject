package kr.co.person.common;

import org.apache.commons.lang3.StringUtils;

import kr.co.person.domain.Board;
import kr.co.person.domain.Comment;
import kr.co.person.domain.User;

public class IsValid {
	public IsValid(){
	}
	
	public static boolean isValidInts(int... ints){
		if(ints != null){
			for(int i : ints) {
		    	if(i <= 0){
		    		return false;
		    	}
			}
		} else {
			return false;
		}
    	return true;
	}
	
	public static boolean isNotValidInts(int... ints){
		if(ints != null){
    		for(int i : ints) {
		    	if(i <= 0){
		    		return true;
		    	}
			}
    	}
    	return false;
	}
	
	public static boolean isValidObjects(Object... objects){
		if(objects != null){
			for(Object object : objects) {
		    	if(object == null){
		    		return false;
		    	}
			}
		} else {
			return false;
		}
    	return true;
    }
    
    public static boolean isNotValidObjects(Object... objects){
    	if(objects != null){
    		for(Object object : objects) {
		    	if(object == null){
		    		return true;
		    	}
			}
    	}
    	return false;
    }
    
    public static boolean isValidArrays(Object[]... objects){
    	if(objects != null){
    		for(Object[] object : objects){
		    	if(object == null || object.length == 0){
		    		return false;
		    	}
    		}
    	} else {
    		return false;
    	}
    	return true;
    }

    public static boolean isNotValidArrays(Object[]... objects){
    	if(objects != null){
    		for(Object[] object : objects){
    			if(object == null || object.length == 0){
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    public static boolean isValidUser(User user){
    	if(user != null){
    		if(StringUtils.isEmpty(user.getEmail()) 
    				|| StringUtils.isEmpty(user.getId()) 
    				|| StringUtils.isEmpty(user.getName()) 
    				|| StringUtils.isEmpty(user.getPassword())
    				|| user.getIdx() == 0){
    			return false;
    		}
    	} else {
    		return false;
    	}
    	return true;
    }
    
    public static boolean isNotValidUser(User user){
    	if(user != null){
    		if(StringUtils.isNotEmpty(user.getEmail()) 
    				&& StringUtils.isNotEmpty(user.getId()) 
    				&& StringUtils.isNotEmpty(user.getName()) 
    				&& StringUtils.isNotEmpty(user.getPassword())
    				&& user.getIdx() != 0){
    			return false;
    		} else {
    			return true;
    		}
    	}
    	return true;
    }
    
    public static boolean isValidBoard(Board board){
    	if(board != null){
    		if(StringUtils.isEmpty(board.getContent()) 
    				|| StringUtils.isEmpty(board.getTitle())
    				|| isNotValidUser(board.getUser())
    				|| board.getIdx() == 0){
    			return false;
    		}
    	} else {
    		return false;
    	}
    	return true;
    }
    
    public static boolean isNotValidBoard(Board board){
    	if(board != null){
    		if(StringUtils.isNotEmpty(board.getContent()) 
    				&& StringUtils.isNotEmpty(board.getTitle())
    				&& isValidUser(board.getUser())
    				&& board.getIdx() != 0){
    			return false;
    		} else {
    			return true;
    		}
    	}
    	return true;
    }
    
    public static boolean isValidComment(Comment comment){
    	if(comment != null){
    		if(StringUtils.isEmpty(comment.getComment()) 
    				|| isNotValidBoard(comment.getBoard())
    				|| isNotValidUser(comment.getWriter())
    				|| comment.getIdx() == 0){
    			return false;
    		}
    	} else {
    		return false;
    	}
    	return true;
    }
    
    public static boolean isNotValidComment(Comment comment){
    	if(comment != null){
    		if(StringUtils.isNotEmpty(comment.getComment()) 
    				&& isValidBoard(comment.getBoard())
    				&& isValidUser(comment.getWriter())
    				&& comment.getIdx() != 0){
    			return false;
    		} else {
    			return true;
    		}
    	}
    	return true;
    }
}
