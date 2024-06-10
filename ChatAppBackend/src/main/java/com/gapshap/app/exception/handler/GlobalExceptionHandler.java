package com.gapshap.app.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.exception.ResourceAlreadyExists;
import com.gapshap.app.exception.ResourceNotFoundException;
import com.gapshap.app.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> userNotFoundException(UserNotFoundException ex){
		Map<String, Object> response = new HashMap<>();
		response.put(AppConstants.MESSAGE, ex.getMessage());
		response.put(AppConstants.STATUS_CODE,AppConstants.STATUS_CODE_404);
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex){
		Map<String, Object> response = new HashMap<>();
		response.put(AppConstants.MESSAGE, ex.getMessage());
		response.put(AppConstants.STATUS_CODE,AppConstants.STATUS_CODE_400);
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}

	
	@ExceptionHandler(ResourceAlreadyExists.class)
	public ResponseEntity<?> userNotFoundException(ResourceAlreadyExists ex){
		Map<String, Object> response = new HashMap<>();
		response.put(AppConstants.MESSAGE, ex.getMessage());
		response.put(AppConstants.STATUS_CODE,AppConstants.STATUS_CODE_403);
		return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
	}
}
