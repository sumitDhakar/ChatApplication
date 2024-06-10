package com.gapshap.app.security.invalidEntryPoint;

import java.io.IOException;

import javax.naming.AuthenticationException;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class InvalidAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException authException)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		response.sendError(response.SC_UNAUTHORIZED,"Sorry you are not authorized to access this resource .");
	}
}
