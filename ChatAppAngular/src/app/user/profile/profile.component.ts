import { Component } from '@angular/core';
import { User } from '../models/user';
import { UserService } from '../services/user.service';
import { AuthenticationService } from '../services/authentication.service';
import { AppUtils } from 'src/app/utils/app-utils';
import { UserRequest } from '../payload/user-request';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {
  user: User = new User();
  userRequest: any = new UserRequest();
  myForm: FormGroup;



  constructor(private UserService: UserService, private authenticationService: AuthenticationService,
    private builder: FormBuilder) {
    this.myForm = this.builder.group({
      userName: ['', [Validators.required, AppUtils.min(), AppUtils.max()]],
      location: ['', [Validators.required, AppUtils.min(), AppUtils.max()]],
      phoneNumber: ['', [Validators.required, AppUtils.isPhoneValid()]],
      about: ['', [Validators.required, AppUtils.min(), AppUtils.max()]],


    })

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
    if (this.myForm.valid)
      this.UserService.updateUser(this.userRequest).subscribe((data: any) => {
        const storedItem = localStorage.getItem('user');

        this.authenticationService.user.next(data.data)
        AppUtils.showMessage('success', 'Profile Update Successfully')
        AppUtils.modelDismiss('add')

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
