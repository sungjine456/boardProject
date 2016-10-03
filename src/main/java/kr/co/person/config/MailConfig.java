package kr.co.person.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ConfigurationProperties(locations = "classpath:application.yml", prefix="mail")
public class MailConfig {
	@Value("${emailId}") private String EMAIL_ID;
	@Value("${emailPassword}") private String EMAIL_PASSWORD;
	
	private String host;
	private int port;
	private String protocol;
	private String encoding;

	@Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setProtocol(protocol);
        mailSender.setUsername(EMAIL_ID);
        mailSender.setPassword(EMAIL_PASSWORD);
        mailSender.setDefaultEncoding(encoding);
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.debug", true);
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }

	public void setHost(String host) {
		this.host = host;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
