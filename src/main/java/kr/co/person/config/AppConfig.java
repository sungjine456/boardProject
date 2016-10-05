package kr.co.person.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.utility.XmlEscape;
import kr.co.person.common.Message;
import kr.co.person.common.ServerCustomization;
import kr.co.person.interceptor.LoginInterceptor;

@Configuration
@EnableWebMvc
@EnableAsync(proxyTargetClass=true)
@ConfigurationProperties(locations = "classpath:application.yml", prefix="mail")
@PropertySource("classpath:key.properties")
public class AppConfig extends WebMvcConfigurerAdapter {
	@Value("${emailId}") private String EMAIL_ID;
	@Value("${emailPassword}") private String EMAIL_PASSWORD;
	
	private String host;
	private int port;
	private String protocol;
	private String encoding;
	
	@Bean(name="freemarkerConfig")
	public FreeMarkerConfigurer freemarkerConfig() {
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		configurer.setTemplateLoaderPath("/");
		Map<String, Object> map = new HashMap<>();
		map.put("xml_escape", new XmlEscape());
		configurer.setFreemarkerVariables(map);
		return configurer;
	}
	
	@Bean
	public ServerProperties getServerProperties(){
		return new ServerCustomization();
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.ignoreUnknownPathExtensions(false).defaultContentType(MediaType.TEXT_HTML);
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.freeMarker();
	}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/img/**").addResourceLocations("file:///C:/boardProject/img/");
		super.addResourceHandlers(registry);
	}
	
	@Bean
	public Message message(){
		Message message = new Message(messageSource());
		return message;
	}
	
	@Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
	
	@Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }
 
    @Bean
    public LocaleResolver sessionLocaleResolver(){
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("ko"));
        return localeResolver;
    }
    
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**");
	}
	
	@Bean(name="workExecutor")
	  public TaskExecutor taskExecutor() {
	      ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
	      taskExecutor.setMaxPoolSize(1);
	      taskExecutor.setQueueCapacity(0);
	      taskExecutor.afterPropertiesSet();
	      return taskExecutor;
	  }
	
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
