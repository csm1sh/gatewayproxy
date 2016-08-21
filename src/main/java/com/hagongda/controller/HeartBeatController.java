package com.hagongda.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api}") 
public class HeartBeatController {
   public String hello(@RequestParam String message){
	   return "{ greeting: 'hello'}";
   }
}
