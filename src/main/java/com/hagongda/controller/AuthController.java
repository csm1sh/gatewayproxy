package com.hagongda.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hagongda.devicebean.User;

@RestController
@RequestMapping(value="/api}") 
public class AuthController {
	@RequestMapping(value="/auth") 
	public User auth(@RequestBody User user){
	 return new User();
 }
}
