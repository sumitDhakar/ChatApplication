import { Component } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { UserRegistration } from '../../payload/user-registration';
import Swal, { SweetAlertIcon } from 'sweetalert2';
import { delay } from 'rxjs';
import { Router } from '@angular/router';
import { AppUtils } from 'src/app/utils/app-utils';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent {
  myForm: FormGroup;
  
  constructor(private authService:AuthenticationService,private router:Router,private builder: FormBuilder){
    this.myForm = this.builder.group({
      userName: ['', [Validators.required,  AppUtils.min(),AppUtils.max()]],
      email: ['', [Validators.required, AppUtils.isEmail]],
      password: ['', [Validators.required,AppUtils.min()]],
    

    })
  }

public userRegistration:UserRegistration = new UserRegistration();
  

  public register(){
    if (this.myForm.valid)
     this.authService.registerUser(this.userRegistration).subscribe((data:any)=>{
      console.log(data);
      AppUtils.showMessage('success',data.message)
      
         window.setTimeout( () =>{
          AppUtils.showMessage('success',data.emailStatus)
         },3000);
         this.authService.setEmail(data.email);
         this.router.navigate(['otp'])
        
     },err=>{
      AppUtils.showMessage('error',err.error.message);
     })
  }

}
