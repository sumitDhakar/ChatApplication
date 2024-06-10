import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiRoutes } from 'src/app/utils/api-routes';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient:HttpClient) { }

  getUserById(id:any){
    return this.httpClient.get(ApiRoutes.USER_BY_ID+id);
  }


  getAllUserToAddContact(){
    return this.httpClient.get(ApiRoutes.USER_ALL);
  }



  getUserStatusById(id:any){
return this.httpClient.get(ApiRoutes.USER_STATUS_GET+`/${id}`);
  }

  updateUser(userRequest:any){
    const headers = new HttpHeaders({
      'enctype': 'multipart/form-data'
      //'Content-type':'multipart/form-data;boundary=BOEC8DO7-EBF1-4EA7-966C-E492A9F2C36E'
    });
    const formData = new FormData();


    if (userRequest.profileNamedata != null) {
      formData.append("image", userRequest.profileNamedata);
    }

    else {
      formData.append("image", 'null');
    }


    formData.append("userRequest",new Blob([JSON.stringify(userRequest)],{type:'application/json'}));
      
      
       return this.httpClient.put(ApiRoutes.USER_UPDATE,formData,{headers});
      
  }


}
