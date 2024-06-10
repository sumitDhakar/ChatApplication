import { Component } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import Swal from 'sweetalert2';
import { ChatService } from '../../services/chat.service';
import { User } from '../../models/user';
import { AppUtils } from 'src/app/utils/app-utils';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  user:User=new User();
  constructor(private authService:AuthenticationService,private chatService:ChatService){}


  ngOnInit(): void {
   
    this.getProfileUser();
    this.authService.loggedInUser.subscribe((data:any)=>{
      
      this.user=data;
      
    })
   
   }
  
   getProfileUser(){
    let currentUser =this.authService.getUser();
    this.user=currentUser;
    this.authService.user.next(this.user);

   }
  
logOut(){
  Swal.fire({
    title: "Are you sure?",
    text: "You  want to logOut?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#7269ef",
    cancelButtonColor: "#7CD1F2",
    confirmButtonText: "Logout"
  }).then((result) => {
    if (result.isConfirmed) {
      Swal.fire({
        title: "Logging out",
        icon: "success"
      });
      this.authService.logOut();
      
      // this.webSocketService.disConnect(); 
      this.chatService.disConnect(); 
    }
  });
}
}

