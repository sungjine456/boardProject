package kr.co.person.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TestAsync {
	private static final Logger log = LoggerFactory.getLogger(TestAsync.class);
	
	@Async
    public void print(int count){
		log.info("start async : " + count);
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		log.info("final async : " + count);
	}
}
