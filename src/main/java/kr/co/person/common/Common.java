package kr.co.person.common;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kr.co.person.common.exception.EmptyStringException;
import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;
import kr.co.person.repository.UserRepository;

@Component
public class Common {
	@Autowired private Message message;
	@Autowired private UserRepository userRepository;
	@Autowired private Encryption encryption;
	private final String ENCRYPTION_SALT = "personProject";
	
	public String passwordEncryption(String str) throws EmptyStringException, NoSuchAlgorithmException {
		System.out.println(encryption);
		return encryption.oneWayEncryption(str, ENCRYPTION_SALT);
	}
	
	public OkCheck isEmail(String email) {
		if(StringUtils.isEmpty(email)){
			return new OkCheck(message.USER_NO_EMAIL, false);
		}
		if(Pattern.compile("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$").matcher(email).matches()){
			return new OkCheck("", true);
		} else {
			return new OkCheck(message.USER_NO_EMAIL_FORMAT, false);
		}
    }
    
    public String cleanXss(String str) {
    	str = str.replaceAll("&", "&amp;");
    	str = str.replaceAll("%2F", "");
    	str = str.replaceAll("\"","&#34;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("%", "&#37;");
        str = str.replaceAll("..\\\\", "");
        str = str.replaceAll("\0", " ");
        
        return str;
    }
    
    public String enter(String content){
    	content = content.replace("\r\n","<br/>");
    	
    	return content;
    }
    
    public Cookie addCookie(String key, String value) throws UnsupportedEncodingException{
    	LocalDateTime date1 = LocalDateTime.now();
    	LocalDateTime date2 = date1.plusYears(1);
    	int expiredate = date2.getNano()-date1.getNano()/1000;
    	Cookie cookie = new Cookie(key, URLEncoder.encode(value, "UTF-8"));
		cookie.setMaxAge(expiredate);
	    return cookie;
    }

    public Cookie removeCookie(String key){
    	Cookie cookie = new Cookie(key, null);
    	cookie.setMaxAge(0);
    	return cookie;
    }
    
    public String createImg(MultipartFile file, String id, String kind) throws IOException {
    	String[] strArray = file.getOriginalFilename().split("\\.");
		String ext = "";
		if(strArray.length == 2){
			ext = strArray[1];
		}
		String imgPath = "";
		String se = File.separator;
		if(StringUtils.equalsIgnoreCase(ext, "gif") || StringUtils.equalsIgnoreCase(ext, "jpg") || StringUtils.equalsIgnoreCase(ext, "jpeg") || StringUtils.equalsIgnoreCase(ext, "png")){
			Date date = new Date();
			String fileName = id + "_" + date.getTime() + "." + ext;
			String filePath = "C:"+se+"boardProject"+se+"img"+se+kind;
			imgPath = "/img"+se+kind+se+fileName;
			File dayFile = new File(filePath);
			if(!dayFile.exists()){
			   dayFile.mkdirs();
			}
			try {
				file.transferTo(new File(filePath + se + fileName));
			} catch(IOException e) {
				throw new IOException();
			}
		}
		return imgPath;
	}
    
    public boolean sessionComparedToDB(HttpSession session){
		String loginYn = IsValid.isValidObjects(session.getAttribute("loginYn"))?(String)session.getAttribute("loginYn"):"";
		User sessionUser = IsValid.isValidObjects(session.getAttribute("user"))?(User)session.getAttribute("user"):null;
		if(sessionUser == null){
			return false;
		}
		User user = userRepository.findOne(sessionUser.getIdx());
		return (IsValid.isNotValidUser(user) || !StringUtils.equals(sessionUser.getId(), user.getId())
				|| !StringUtils.equals(sessionUser.getName(), user.getName()) || !StringUtils.equals(sessionUser.getEmail(), user.getEmail())
				|| !StringUtils.equals(loginYn, "Y"))
				?false:true;
	}
    
    public boolean adminSessionComparedToDB(HttpSession session){
		String loginYn = IsValid.isValidObjects(session.getAttribute("loginYn"))?(String)session.getAttribute("loginYn"):"";
		User sessionUser = IsValid.isValidObjects(session.getAttribute("user"))?(User)session.getAttribute("user"):null;
		if(sessionUser == null){
			return false;
		}
		User user = userRepository.findOne(sessionUser.getIdx());
		return (IsValid.isNotValidObjects(user) || !StringUtils.equals(sessionUser.getId(), user.getId())
				|| !StringUtils.equals(sessionUser.getName(), user.getName()) || !StringUtils.equals(sessionUser.getEmail(), user.getEmail())
				|| ((sessionUser.getAdmin().equals(user.getAdmin()))?user.getAdmin():"N").equals("N") || !StringUtils.equals(loginYn, "Y"))
				?false:true;
	}
}
