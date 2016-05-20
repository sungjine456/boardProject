package kr.co.person.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.person.domain.User;
import kr.co.person.service.UserService;

@Controller
public class AddUserController {
	@Autowired UserService userService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String addUserView(){
		return "view/addUser";
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String addUser(HttpServletRequest req){
		String id = req.getParameter("user_id");
		String password = req.getParameter("password");
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		Date date = new Date();
		User user = new User(id, email, password, name, date, date);
		
		userService.create(user);
		
		return "view/addUser";
	}
}
