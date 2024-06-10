package com.gapshap.app.socketconfiguration;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.corundumstudio.socketio.SocketIOClient;
import com.gapshap.app.model.chat.Message;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@CrossOrigin("*")
public class SocketService {

	public void sendMessage(String room, String eventName, SocketIOClient senderClient, String message) {
		for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
			if (!client.getSessionId().equals(senderClient.getSessionId())) {
				client.sendEvent(eventName, new Message());
			}
		}
	}

	

//	public void sendMessage(SocketIOServer socketServer,String sessionId, String room, String eventName, InvitationRequest message) {
//		SocketIONamespace namespace = socketServer.getRoomOperations(room).getNamespace();
//		SocketIOClient client = namespace.getClient(UUID.fromString(sessionId));
//
//		if (client != null) {
//			// Send event to the specific client in the specified room
//			client.sendEvent(eventName, message);
//			System.err.println("Message sent to client with session ID: " + sessionId);
//		} else {
//			// Handle case when client with provided session ID is not found
//			System.err.println("Client with session ID " + sessionId + " not found in room " + room);
//		}
//	}

}