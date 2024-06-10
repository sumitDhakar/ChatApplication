package com.gapshap.app.socketconfiguration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.model.chat.UserActiveStatus;
import com.gapshap.app.payload.MessageRequest;
import com.gapshap.app.payload.TypeStatusResponse;
import com.gapshap.app.payload.UserStatusRequest;
import com.gapshap.app.service.IUserActiveStatusService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SocketModule {

	@Autowired
	private IUserActiveStatusService userActiveStatusService;
	private final SocketIOServer server;
	private final SocketService socketService;
	private final Map<String, SocketIOClient> connectedClients;

	public SocketModule(SocketIOServer server, SocketService socketService) {
		this.connectedClients = new HashMap<>();
		this.server = server;
		this.socketService = socketService;
		server.addConnectListener(onConnected());
		server.addDisconnectListener(onDisconnected());
		server.addEventListener("send_message", MessageRequest.class, onChatReceived());
		server.addEventListener("send_typing_status", TypeStatusResponse.class, onTypingStatusRecieved());

	}

	
	
	private DataListener<TypeStatusResponse> onTypingStatusRecieved() {
		return (senderClient, data, ackSender) -> {
			log.info(data.toString());
			// Determine the operation type based on event name or other parameters
			if(!data.getToEmail().equals("")) {
				this.sendMessage(data.getToEmail(), "get_typing_status", data);
			}
			if(data.getToEmail().equals(""))
			this.sendMessageToAllUsers("get_typing_status", data, "");
			
			};
	}

	private DataListener<MessageRequest> onChatReceived() {
		return (senderClient, data, ackSender) -> {
			log.info(data.toString());
			
			socketService.sendMessage(data.getConversationId(), "get_message", senderClient, data.getMessage());
		};
	}

	private ConnectListener onConnected() {
		return (client) -> {
			String room = client.getHandshakeData().getSingleUrlParam("email");
			client.joinRoom(room);
			connectedClients.put(room, client); // Add client to the map

			UserActiveStatus save = this.userActiveStatusService
					.updateUserStatus(new UserStatusRequest(room, "ONLINE"));

			this.sendMessageToAllUsers(AppConstants.SEND_USERACTIVE_STATUS, save, room);
			log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
		};

	}

	private DisconnectListener onDisconnected() {
		return client -> {

			UserActiveStatus save = this.userActiveStatusService
					.updateUserStatus(new UserStatusRequest(this.iterateUserChatId(client), "OFFLINE"));
			this.deleteUserChatEntity(client);
			this.sendMessageToAllUsers(AppConstants.SEND_USERACTIVE_STATUS, save, "");
			log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
		};
	}

	// Method to get value by key
	public SocketIOClient getClient(String key) {
		return connectedClients.get(key);
	}

	// Method to iterate over the map and print its contents
	public String iterateUserChatId(SocketIOClient socketIOClient) {
//		System.out.println("Contents of the Map:");
		for (Map.Entry<String, SocketIOClient> entry : connectedClients.entrySet()) {
			if (socketIOClient.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	public void deleteUserChatEntity(SocketIOClient eventName) {
		for (Map.Entry<String, SocketIOClient> entry : connectedClients.entrySet()) {
			if (eventName.equals(entry.getValue())) {
			
				boolean remove = connectedClients.remove(entry.getKey(), entry.getValue());
				
				break;
			}
		}
	}

	public void sendMessage(String room, String eventName, Object message) {
		SocketIOClient senderClient = this.getClient(room);

		if (senderClient != null)
			senderClient.sendEvent(eventName, message);
	}

	public void sendMessageToAllUsers(String eventName, Object message, String currentUserEmail) {
		for (Map.Entry<String, SocketIOClient> entry : connectedClients.entrySet()) {
			if (!currentUserEmail.equals(entry.getKey())) {
				SocketIOClient senderClient = entry.getValue();

				senderClient.sendEvent(eventName, message);
			}

		}
	}

}
