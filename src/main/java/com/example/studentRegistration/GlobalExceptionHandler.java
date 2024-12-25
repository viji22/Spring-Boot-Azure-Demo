package com.example.studentRegistration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException e){
		return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

}
