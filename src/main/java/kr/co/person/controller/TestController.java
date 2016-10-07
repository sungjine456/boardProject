package kr.co.person.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.person.pojo.TestAsync;

@Controller
public class TestController {
    @Autowired TestAsync TestAsync;
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        for(int i = 0; i < 10; i++) {
        	TestAsync.print(i);
        }
        return "view/user/login";
    }
}