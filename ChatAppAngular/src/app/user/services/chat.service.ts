import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import * as io from 'socket.io-client';
import { Conversation } from '../models/conversation';
import { AuthenticationService } from './authentication.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ApiRoutes } from 'src/app/utils/api-routes';
import { MessageRequest } from '../payload/message-request';
import { ConversationMessageHistoryResponse } from '../payload/conversation-message-history-response';
import { TypeStatusResponse } from '../payload/type-status-response';
import { UserActiveStatus } from '../models/user-active-status';
import { ChatFiles } from '../models/chat-files';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  public invitation_Request = new BehaviorSubject<any>(null);
  public User_Status_Manager = new BehaviorSubject<UserActiveStatus>(new UserActiveStatus());
  public get_User_TypingStatus = new BehaviorSubject<TypeStatusResponse>(new TypeStatusResponse(0, '', '', '', '', '',''));
  public conversation_created = new BehaviorSubject<any>(null);
  public get_message = new BehaviorSubject<ConversationMessageHistoryResponse>(new ConversationMessageHistoryResponse(0, 0, 0, '', '', '', '', [], [], ''));
  public get_message_group = new BehaviorSubject<ConversationMessageHistoryResponse>(new ConversationMessageHistoryResponse(0, 0, 0, '', '', '', '', [], [], ''));


  constructor(private authService: AuthenticationService, private httpClient: HttpClient) { }
  private invitationSubject: Subject<any> = new Subject<any>();

  private socket: any;
  private url: string = 'ws://192.168.0.16:8085';
  //  private url: string = 'ws://192.168.0.111:8085';

  connect(): void {
    const email = this.authService.getEmail();
    this.socket = io(this.url, {
      path: '/socket.io',
      transports: ['websocket'],

      query: {
        email: email,
      }
    });

    this.socket.on('connect', (data: any) => {

    });

    this.socket.on('send_active_user', (data: any) => {
      this.User_Status_Manager.next(data);

    });


    this.socket.on('send_Invitation', (message: any) => {

      this.invitation_Request.next(message);

    });

    this.socket.on('get_message', (message: any) => {

      this.get_message.next(message);

    });

    this.socket.on('get_typing_status', (message: any) => {

      this.get_User_TypingStatus.next(message);

    });

    this.socket.on('get_message_group', (message: any) => {

      this.get_message_group.next(message);

    });


    this.socket.on('get_conversation', (message: any) => {

      this.conversation_created.next(message);

    });



    this.socket.on('connect_error', (message: any) => {
      console.log('New message:', message);
    });

    this.socket.on('disconnect', () => {
      console.log('Disconnected from WebSocket server');
    });
  }

  sendMessage(message: any, typeEvent: any) {
    this.socket.emit(typeEvent, message);
  }

  disConnect(): void {
    if (this.socket) {
      this.socket.disconnect();
      console.log('Disconnected from WebSocket server');
    }
  }


  getMessages() {
    let observable = new Observable<any>(observer => {
      this.socket.on('send_Invitation', (data: any) => {
        observer.next(data);
        console.log(data);

      });
      return () => { this.socket.disconnect(); };
    });
    return observable;
  }

  // Method to expose the observable for 'send_Invitation' event
  getInvitation(): Observable<any> {
    return this.invitationSubject.asObservable();
  }



  getMessageHistoryByConversationId(conversationId: string, forWhichChats: string) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const params = {
      conversationId: conversationId,
      forWhichChats: forWhichChats,
    }; // Include your parameter here

    return this.httpClient.get<any>(ApiRoutes.GET_MESSAGE_HISTORY, { headers, params });

  }


  sendMessageToServerToMaintainHistory(messageRequest: MessageRequest, sendOrSaveMessageFor: string) {
    const headers = new HttpHeaders({
      'enctype': 'multipart/form-data'
    });
    const params = { sendOrSaveMessageFor: sendOrSaveMessageFor }; // Include your parameter here
    const formData = new FormData();

    if (messageRequest.files != null) {
      for (const file of messageRequest.files) {

        formData.append("filesData", file);
      }
    }

    else {
      formData.append("filesData", 'null');
    }

    formData.append("message", new Blob([JSON.stringify(messageRequest)], { type: 'application/json' }));


    return this.httpClient.post(ApiRoutes.SEND_MESSAGE, formData, { headers, params });
  }


  public deleteMessges(messageDeleteRequestId: number) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
   
    const params = { deleteMessage: messageDeleteRequestId }; // Include your parameter here

    return this.httpClient.delete<any>(ApiRoutes.DELETE_MESSAGE, { headers, params });

  }


  public updateMessageIsSeenStatus(conversationIdData: string) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const params = {
      conversationId: conversationIdData,
     
    }; // Include your parameter here

    return this.httpClient.get<any>(ApiRoutes.UPDATE_MESSAGE_MARK_AS_SEEN, { headers, params });

   
  }


  
}
