package kr.co.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("key.properties")
@SpringBootApplication
public class BoardProjectApplication {
	static final Logger log = LoggerFactory.getLogger(BoardProjectApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(BoardProjectApplication.class, args);
	}
}
