package com.gapshap.app.exception;

import com.gapshap.app.constants.AppConstants;

public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5373317318125033147L;

	public ResourceNotFoundException() {
		super(AppConstants.RESOURCE_NOT_FOUND);
	}

	public ResourceNotFoundException(String msg) {
		super(msg);
	}
	
	
	
	
	
}
