import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { ActivatedRoute, Router } from '@angular/router';
import { VerificationRequest } from '../../payload/verification-request';
import { AppUtils } from 'src/app/utils/app-utils';
import { LoginRequest } from '../../payload/login-request';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  constructor(private authService: AuthenticationService, private activatedRouter: ActivatedRoute, private router: Router) { }

  verificationRequest: VerificationRequest = new VerificationRequest();
  loginRequest: LoginRequest = new LoginRequest();

  ngOnInit(): void {

    if (this.authService.isLoggedIn()) {
      this.getCurrentUser();
    } else
      this.activatedRouter.queryParamMap.subscribe((param: any) => {
        this.verificationRequest.email = param.get('email');
        this.verificationRequest.otp = param.get('value')

        this.verify();
      })
  }



  public login() {
    this.authService.login(this.loginRequest).subscribe((data: any) => {
      AppUtils.showMessage('success', 'Login success')
      console.log(data);

      this.authService.setToken(data.accessToken);
      this.authService.setEmail(this.loginRequest.email);
      // this.router.navigate(['gapshap'])
      this.getCurrentUser();
    }, err => {
      this.authService.logOut();
      console.log(err);

      AppUtils.showMessage('error', 'Login failed');
    })
  }


  public verify() {
    if (this.verificationRequest.email !== undefined && this.verificationRequest.email !== null && this.verificationRequest.email !== '')
      this.authService.verifyOtp(this.verificationRequest).subscribe((data: any) => {
        AppUtils.showMessage('success', data.message);

        this.router.navigate(['']);
      }, err => {
        AppUtils.showMessage('error', err.error.message)
        this.router.navigate(['']);
      })
  }


  getCurrentUser() {
    this.authService.currentUser().subscribe((data: any) => {
      AppUtils.showMessage('success', 'Login Success');
      this.authService.setUser(data.data);
      this.authService.user.next(data.data);
      
      this.router.navigate(['gapshap'])
    }, err => {
      AppUtils.showMessage('error', 'Session expire');
      this.authService.logOut();
    })
  }
}
