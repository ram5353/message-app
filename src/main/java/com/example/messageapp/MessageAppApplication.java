package com.example.messageapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class MessageAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageAppApplication.class, args);
	}
	

}
