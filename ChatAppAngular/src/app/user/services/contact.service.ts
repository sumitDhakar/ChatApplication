import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiRoutes } from 'src/app/utils/api-routes';
import { InvitationRequest } from '../payload/invitation-request';

@Injectable({
  providedIn: 'root'
})
export class ContactService {

  constructor(private httpClient:HttpClient) { }

  // public createInvitation(invitationRequest:InvitationRequest){
  //    return this.httpClient.post(ApiRoutes.INVITATION_CREATE,invitationRequest);
  // }

  public getAllContactsOfCurrentUser(forDestination:any){
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const params = { forDestination: forDestination }; // Include your parameter here

    return this.httpClient.get<any>(ApiRoutes.GetALL_CONTACT, { headers, params });
  }

  
    //delete Contacts   
    deleteContacts(contactstId: number) {
      const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
      const params = { contactId: contactstId }; // Include your parameter here
  
      return this.httpClient.delete(ApiRoutes.CONTACT_DELETE, { headers, params });
  
    }

}