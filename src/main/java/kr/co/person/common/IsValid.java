package kr.co.person.common;

public class IsValid {
	public IsValid(){
	}
	
	public static boolean isValid(int a){
		if(a == 0){
    		return false;
    	}
    	return true;
	}
	
	public static boolean isValid(int a, int b){
		if(a == 0 || b == 0){
    		return false;
    	}
    	return true;
	}
	
	public static boolean isValid(int a, int b, int c){
    	if(a == 0 || b == 0 || c == 0){
    		return false;
    	}
    	return true;
    }
	
	public static boolean isNotValid(int a){
		if(a != 0){
    		return false;
    	}
    	return true;
	}
	
	public static boolean isNotValid(int a, int b){
		if(a != 0 && b != 0){
    		return false;
    	}
    	return true;
	}
	
	public static boolean isNotValid(int a, int b, int c){
    	if(a != 0 && b != 0 && c != 0){
    		return false;
    	}
    	return true;
    }
	
	public static boolean isValid(Object a){
    	if(a == null){
    		return false;
    	}
    	return true;
    }
	
	public static boolean isValid(Object a, Object b){
    	if(a == null || b == null){
    		return false;
    	}
    	return true;
    }
    
    public static boolean isValid(Object a, Object b, Object c){
    	if(a == null || b == null || c == null){
    		return false;
    	}
    	return true;
    }
    
    public static boolean isNotValid(Object a){
    	if(a != null){
    		return false;
    	}
    	return true;
    }
	
	public static boolean isNotValid(Object a, Object b){
    	if(a != null && b != null){
    		return false;
    	}
    	return true;
    }
    
    public static boolean isNotValid(Object a, Object b, Object c){
    	if(a != null && b != null && c != null){
    		return false;
    	}
    	return true;
    }
}
