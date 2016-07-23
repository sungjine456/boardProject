package kr.co.person.common;

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
    	} else {
    		return true;
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
    	} else {
    		return true;
    	}
    	return false;
    }
}
