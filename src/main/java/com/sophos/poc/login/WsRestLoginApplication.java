package com.sophos.poc.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class WsRestLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsRestLoginApplication.class, args);
	}

}
