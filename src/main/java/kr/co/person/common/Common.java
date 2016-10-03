package kr.co.person.common;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.regex.Pattern;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kr.co.person.domain.User;
import kr.co.person.pojo.OkCheck;
import kr.co.person.service.UserService;

@Component
public class Common {
	@Value("${emailId}") private String EMAIL_ID;
	@Autowired Message message;
	@Autowired JavaMailSender mailSender;
	@Autowired UserService userService;
	
	public String passwordEncryption(String str){
		if(StringUtils.isEmpty(str)){
			return "";
		}
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(str.getBytes()); 
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			return sb.toString() + "personProject";
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			return ""; 
		}
	}
	
	public String cookieValueEncryption(String str){
		if(StringUtils.isEmpty(str)){
			return "";
		}
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(str.getBytes()); 
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			return sb.toString() + "cookiesAutoLogin";
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			return ""; 
		}
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
    
    public String cleanXss(String str){
    	if(StringUtils.isEmpty(str)){
    		return "";
    	}
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
    
    public Cookie addCookie(String key, String value){
    	DateTime date1 = DateTime.now();
    	DateTime date2 = date1.plusYears(1);
    	int expiredate = (int)(date2.getMillis()-date1.getMillis())/1000;
    	Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(expiredate);
	    return cookie;
    }

    public Cookie removeCookie(String key){
    	Cookie cookie = new Cookie(key, null);
    	cookie.setMaxAge(0);
    	return cookie;
    }
    
    public String createImg(MultipartFile file, String id, String kind) {
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
			imgPath = "img"+se+kind+se+fileName;
			File dayFile = new File(filePath);
			if(!dayFile.exists()){
			   dayFile.mkdirs();
			}
			try {
				file.transferTo(new File(filePath + se + fileName));
			} catch(Exception e) {
				e.printStackTrace(); 
			}
		}
		return imgPath;
	}
    
    public boolean sendMail(String toEmail, String title, String content){
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			messageHelper.setFrom(EMAIL_ID);
			messageHelper.setTo(toEmail);
			messageHelper.setSubject(title);
			messageHelper.setText("", content);

			mailSender.send(mimeMessage);
		} catch (Exception e) {
			return false;
   	    }
    	return true;
    }
    
    public boolean sessionComparedToDB(HttpSession session){
		String loginYn = IsValid.isValidObjects(session.getAttribute("loginYn"))?(String)session.getAttribute("loginYn"):"";
		User sessionUser = IsValid.isValidObjects(session.getAttribute("user"))?(User)session.getAttribute("user"):null;
		if(sessionUser == null){
			return false;
		}
		User user = userService.findUserForIdx(sessionUser.getIdx());
		return (IsValid.isNotValidObjects(user) || !StringUtils.equals(sessionUser.getId(), user.getId())
				|| !StringUtils.equals(sessionUser.getName(), user.getName()) || !StringUtils.equals(sessionUser.getEmail(), user.getEmail())
				|| !StringUtils.equals(loginYn, "Y"))
				?false:true;
	}
}
