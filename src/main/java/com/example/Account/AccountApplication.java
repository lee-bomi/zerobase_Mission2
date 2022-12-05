package com.example.Account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SpringBootApplication
public class AccountApplication {

	public static void main(String[] args) {

		SpringApplication.run(AccountApplication.class, args);


	}

}
