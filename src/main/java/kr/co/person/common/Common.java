package kr.co.person.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class Common {
	public String passwordEncryption(String str){
		String sha = ""; 
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(str.getBytes()); 
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			sha = sb.toString() + "personProject";
			
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			sha = null; 
		}
		return sha;
	}
	
	public boolean isEmail(String email) {
		return Pattern.compile("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$").matcher(email).matches();
    }
}
