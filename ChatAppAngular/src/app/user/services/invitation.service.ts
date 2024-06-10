import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiRoutes } from 'src/app/utils/api-routes';
import { ConversationRequest } from '../payload/conversation-request';
import { InvitationRequest } from '../payload/invitation-request';
import { UpdateInvitationRequest } from '../payload/update-invitation-request';

@Injectable({
  providedIn: 'root'
})
export class InvitationService {
  constructor(private httpClient:HttpClient) { }

  public createInvitation(invitationRequest:InvitationRequest){
     return this.httpClient.post(ApiRoutes.INVITATION_CREATE,invitationRequest);
  }

  public getAllInvitations(){
    return this.httpClient.get(ApiRoutes.INVITATION_GET_ALL);
  }

 public acceptOrRejectRequest(invitationRequest:UpdateInvitationRequest){
  const headers = new HttpHeaders({
    'enctype': 'multipart/form-data'
    //'Content-type':'multipart/form-data;boundary=BOEC8DO7-EBF1-4EA7-966C-E492A9F2C36E'
  });
     const formData = new FormData();
     formData.append("invitationRequest",new Blob([JSON.stringify(invitationRequest)],{type:'application/json'}));
    
    
     return this.httpClient.put(ApiRoutes.INVITATION_UPDATE,formData,{headers});
    
 }


}