import { Component } from '@angular/core';
import { VerificationRequest } from '../../payload/verification-request';
import { AuthenticationService } from '../../services/authentication.service';
import { AppUtils } from 'src/app/utils/app-utils';
import { ResendOtpRequest } from '../../payload/resend-otp-request';
import { Router } from '@angular/router';

@Component({
  selector: 'app-otp-verification',
  templateUrl: './otp-verification.component.html',
  styleUrls: ['./otp-verification.component.scss']
})
export class OtpVerificationComponent {
  constructor(private authService:AuthenticationService,private router:Router){}
  
  verificationRequest:VerificationRequest = new VerificationRequest();
  

  public verify(){
    this.verificationRequest.email=this.getEmail()||'';
    this.authService.verifyOtp(this.verificationRequest).subscribe((data:any)=>{
      
      AppUtils.showMessage('success',data.message);
        this.router.navigate(['']);
    },err=>{
      console.log(err);
      
      AppUtils.showMessage('error',err.error.message)
      
    })
  }

  getEmail(){
    return this.authService.getEmail();
  }

  resend(){
    let resendRequest:ResendOtpRequest = new ResendOtpRequest(this.getEmail()||'');
    if(this.authService.otpCount>3)
    {
      AppUtils.showMessage('success','You tried many time try after some time');
      return ;
    }
    AppUtils.showProcess('info','We have sent you email Please check.')
    this.authService.resendOtp(resendRequest).subscribe((data:any)=>{
      AppUtils.showMessage('success',data.message);
    },err=>{
      AppUtils.showMessage('error',err.error.message);
    })
  }
}
