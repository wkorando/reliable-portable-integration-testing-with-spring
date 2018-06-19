package com.bk.movie;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(MovieClientException.class)
	public ResponseEntity<Errors> handleExceptions(MovieClientException ex){
		return ResponseEntity.badRequest().body(ex.getErrors());
	}
}
