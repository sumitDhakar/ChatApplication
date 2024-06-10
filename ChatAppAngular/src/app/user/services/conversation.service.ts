import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConversationRequest } from '../payload/conversation-request';
import { ApiRoutes } from 'src/app/utils/api-routes';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConversationService {

  // public localConversationList = new BehaviorSubject<any>(null);
  // allLocalConversationList:Observable<any> = this.localConversationList.asObservable();


  constructor(private httpClient:HttpClient) { }

  public createConversation(conversationRequest:ConversationRequest){
     return this.httpClient.post(ApiRoutes.CONVERSATION_CREATE,conversationRequest);
  }

  public getAllConversationListOfCurrentUSer(){
    return this.httpClient.get(ApiRoutes.CONVERSATION_GET_CURRENTUSER);
 }
  public getConversation(conversationUserEmail:string){
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const params = { receiverEmail: conversationUserEmail }; // Include your parameter here

    return this.httpClient.get(ApiRoutes.CONVERSATION_GET, { headers, params });

  }

}
