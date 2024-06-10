import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../services/authentication.service';
import { UserService } from '../services/user.service';
import { User } from '../models/user';
import { AppUtils } from 'src/app/utils/app-utils';
import { UserRequest } from '../payload/user-request';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent {
  user: User = new User();
  myForm: FormGroup;
  myLocation: FormGroup;
  myPhoneNumber: FormGroup;
  userRequest: any = new UserRequest();
  
  constructor(private UserService: UserService,private authenticationService:AuthenticationService,private fb: FormBuilder,){
    this.myForm = this.fb.group({
      userName: new FormControl('', [Validators.required,  AppUtils.min(), AppUtils.max()]),
    }),
    this.myLocation = this.fb.group({
      location: new FormControl('',[Validators.required, AppUtils.min(), AppUtils.max()]),
    }),
    this.myPhoneNumber = this.fb.group({
      phoneNumber: new FormControl('',[Validators.required, AppUtils.isPhoneValid()]),
    });
    }
    
    
  

    ngOnInit(): void {
      this.getProfileUser();
    }
  
    getProfileUser() {
      const storedItem = localStorage.getItem('user');
      if (storedItem != null) {
  
        this.user = JSON.parse(storedItem);
      }
  
    }
  
    setDataForUpdate() {
      this.userRequest = this.user;
  
    }
  
  
  
    openFileInput(id: any) {
      // Trigger a click on the hidden file input
      const fileInput = document.getElementById(id) as HTMLInputElement;
      fileInput.click();
    }
  
  
    imagePreview = 'assets/images/users/avatar-1.jpg';
  
    changeImage(event: any) {
      event.target.files[0];
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagePreview = e.target.result
      }
      reader.readAsDataURL(event.target.files[0]);
    }
  
  
  
    // updating data
    updateUser() {
    
        this.UserService.updateUser(this.userRequest).subscribe((data: any) => {
          const storedItem = localStorage.getItem('user');
  
          this.authenticationService.user.next(data.data)
          AppUtils.modelDismiss('add')
  
          AppUtils.showMessage('success', 'Profile Update Successfully')
         
  
          this.user = data.data;
          if (storedItem) {
            if (storedItem) {
              const parsedItem = JSON.parse(storedItem); // If stored as JSON
              if (parsedItem.id == data.data.id) {
                this.authenticationService.user.next(data.data);
                this.authenticationService.setUser(data.data);
  
              }
            }
          }
          this.getProfileUser();
  
        }, (err: any) => {
          AppUtils.showMessage('error', err.error.message);
        })
  
    }
  
  
  
  
    openFile(target: any) {
      target.click();
    }
    selectFile(event: any) {
      this.userRequest.profileNamedata = event.target.files[0];
  
      var reader = new FileReader();
      console.log(event);
  
      reader.onload = function (e) {
        let button = document.getElementById("file");
        button?.setAttribute("src", e!.target!.result + "");
      }
      reader.readAsDataURL(event.target.files[0]);
    }
  
  
}
