package kr.co.person.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.person.BoardProjectApplication;
import kr.co.person.domain.User;
import kr.co.person.service.UserService;

@Controller
public class AddUserController {
	static final Logger log = LoggerFactory.getLogger(BoardProjectApplication.class);
	
	@Autowired UserService userService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String addUserView(){
		return "view/addUser";
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String addUser(@ModelAttribute User user){
		log.info("AddUserController");
		Date date = new Date();
		user.setRegDate(date);
		user.setUpDate(date);
		
		userService.create(user);
		
		return "view/addUser";
	}
}
