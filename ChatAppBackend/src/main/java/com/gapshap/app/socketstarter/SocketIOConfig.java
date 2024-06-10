package com.gapshap.app.socketstarter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corundumstudio.socketio.SocketIOServer;

@Configuration
public class SocketIOConfig {

	@Value("${socket-server.host}")
	private String host;

	@Value("${socket-server.port}")
	private Integer port;

	@Bean
	public SocketIOServer socketIOServer() {
		com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
		config.setHostname(host);
		config.setPort(port);
		   // Configure security filters
      
		SocketIOServer server = new SocketIOServer(config);
//	    server.start();
        System.out.println("Socket.IO server started");
        return server;
	}

}
