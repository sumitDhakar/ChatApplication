import { HttpClient, HttpEvent, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserRegistration } from '../payload/user-registration';
import { ApiRoutes } from 'src/app/utils/api-routes';
import { VerificationRequest } from '../payload/verification-request';
import { LoginRequest } from '../payload/login-request';
import { Router } from '@angular/router';
import { ResendOtpRequest } from '../payload/resend-otp-request';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  otpCount = 0;
  public user = new BehaviorSubject<any>(null);
  loggedInUser: Observable<any> = this.user.asObservable();


  constructor(private httpClient: HttpClient, private router: Router) { }

  // user registration
  public registerUser(userRegistration: UserRegistration) {
    return this.httpClient.post(ApiRoutes.USER_REGISTRATION, userRegistration);
  }

  public verifyOtp(verificationRequest: VerificationRequest) {
    return this.httpClient.post(ApiRoutes.USER_VERIFY, verificationRequest);
  }


  public login(loginRequest: LoginRequest) {
    return this.httpClient.post(ApiRoutes.USER_LOGIN, loginRequest);
  }

  // fetching current user
  public currentUser() {
    return this.httpClient.get(ApiRoutes.USER_CURRENT);
  }


  public resendOtp(resendRequest: ResendOtpRequest) {
    return this.httpClient.post(ApiRoutes.OTP_RESEND, resendRequest);
  }


  setToken(token: string) {
    localStorage.setItem('accessToken', token);
  }

  getToken() {
    let token = localStorage.getItem("accessToken");
    return token === undefined ? null : token;
  }

  setEmail(email: string) {
    localStorage.setItem('email', email);
  }


  getEmail() {
    let email = localStorage.getItem("email");
    return email === undefined ? null : email;
  }

  isLoggedIn() {
    return this.getToken() != null ? true : false
  }

  logOut() {
    localStorage.clear();
    this.router.navigate(['']);
  }

  // getting user from local storage
  getUser() {
    let user = localStorage.getItem("user");
    if (user == undefined || user == null)
      return null;
    else
      return JSON.parse(user);


  }// setting user to local storage
  setUser(user: any) {

    localStorage.setItem("user", JSON.stringify(user));
  }



  getImage(imageName: string): any {
    const params = new HttpParams().set('imageName', imageName);
  
    return this.httpClient.get(ApiRoutes.USER_GetImage, {
      observe: 'events',
      responseType: 'blob',
      params: params,
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    })
  }
  


}
