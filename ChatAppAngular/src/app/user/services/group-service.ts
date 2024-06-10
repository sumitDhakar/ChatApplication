import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiRoutes } from 'src/app/utils/api-routes';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  constructor(private http: HttpClient) { }

  createGroup(groupRequest: any) {
    const headers = new HttpHeaders({
      'enctype': 'multipart/form-data'
      //'Content-type':'multipart/form-data;boundary=BOEC8DO7-EBF1-4EA7-966C-E492A9F2C36E'
    });
    const formData = new FormData();


    if (groupRequest.profileGroupImage != null) {
      formData.append("image", groupRequest.profileGroupImage);
    }

    else {
      formData.append("image", 'null');
    }


    formData.append("data", new Blob([JSON.stringify(groupRequest)], { type: 'application/json' }));




    return this.http.post(ApiRoutes.GROUP_CREATE, formData, { headers });
  }


  updateGroup(groupRequest: any) {
    const headers = new HttpHeaders({
      'enctype': 'multipart/form-data'
      //'Content-type':'multipart/form-data;boundary=BOEC8DO7-EBF1-4EA7-966C-E492A9F2C36E'
    });
    const formData = new FormData();


    if (groupRequest.profileGroup != null) {
      formData.append("image", groupRequest.profileGroup);
    }

    else {
      formData.append("image", 'null');
    }


    formData.append("groupRequest", JSON.stringify(groupRequest));


    return this.http.put(ApiRoutes.GROUP_UPDATE, formData);
  }



  getGroupByConversationId(conversationId: any) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const params = { conversationId: conversationId }; // Include your parameter here

    return this.http.get<any>(ApiRoutes.GetById_GROUP, { headers, params });
  }

  public getAllGroupOfCurrentUser(forDestination: any) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const params = { forDestination: forDestination }; // Include your parameter here

    return this.http.get<any>(ApiRoutes.GetALL_GROUP, { headers, params });
  }
  deleteGroup(groupId: any) {
    return this.http.delete(ApiRoutes.DELETE_GROUP + `/${groupId}`);
  }


}
