package com.gapshap.app.exception;

import com.gapshap.app.constants.AppConstants;

public class UserNotFoundException extends RuntimeException{
 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4128334835623725566L;
	
	public UserNotFoundException() {
		super(AppConstants.USER_NOT_FOUND_);
	}
	public UserNotFoundException(String msg) {
		super(msg);
	}
}
