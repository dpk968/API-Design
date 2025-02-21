package com.dpk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class OAuthContriller {
	
	@RequestMapping("/regUserList")
	public String requestMethodName() {
		return "product.html";
	}
	

}
