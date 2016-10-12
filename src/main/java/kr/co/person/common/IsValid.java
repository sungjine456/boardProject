package kr.co.person.common;

import org.apache.commons.lang3.StringUtils;

import kr.co.person.domain.User;

public class IsValid {
	public IsValid(){
	}
	
	public static boolean isValidInts(int... ints){
		if(ints != null){
			for(int i : ints) {
		    	if(i == 0){
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
		    	if(i == 0){
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
    				&& StringUtils.isEmpty(user.getId()) 
    				&& StringUtils.isEmpty(user.getImg()) 
    				&& StringUtils.isEmpty(user.getName()) 
    				&& StringUtils.isEmpty(user.getPassword()) && user.getIdx() == 0){
    			return false;
    		}
    	} else {
    		return false;
    	}
    	return true;
    }
    
    public static boolean isNotValidUser(User user){
    	if(user != null){
    		if(StringUtils.isEmpty(user.getEmail()) 
    				&& StringUtils.isEmpty(user.getId()) 
    				&& StringUtils.isEmpty(user.getImg()) 
    				&& StringUtils.isEmpty(user.getName()) 
    				&& StringUtils.isEmpty(user.getPassword()) && user.getIdx() == 0){
    			return true;
    		} else {
    			return false;
    		}
    	}
    	return true;
    }
}
