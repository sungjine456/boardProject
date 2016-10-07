package kr.co.person.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.person.pojo.TestAsync;

@Controller
public class TestController {
	private static final Logger log = LoggerFactory.getLogger(TestController.class);
	
    @Autowired TestAsync TestAsync;
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
    	log.info("start");
        for(int i = 0; i < 10; i++) {
        	TestAsync.print(i);
        }
        log.info("finish");
        return "view/user/login";
    }
}