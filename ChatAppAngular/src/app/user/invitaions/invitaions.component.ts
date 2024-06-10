import { Component, OnInit } from '@angular/core';
import { InvitationService } from '../services/invitation.service';
import { AppUtils } from 'src/app/utils/app-utils';
import { InvitationRequest } from '../payload/invitation-request';
import { Subscription } from 'rxjs';
import { ChatService } from '../services/chat.service';
import { Invitation } from '../models/invitation';
import { UpdateInvitationRequest } from '../payload/update-invitation-request';
import { InvitaionStatus } from 'src/app/utils/constants/invitaion-status';

@Component({
  selector: 'app-invitaions',
  templateUrl: './invitaions.component.html',
  styleUrls: ['./invitaions.component.scss']
})
export class InvitaionsComponent implements OnInit {
  invitationSubscription: Subscription | undefined;


  ngOnInit(): void {
    this.getAllInvitationOfCurrentUser();

    this.invitationSubscription = this.chatService.invitation_Request.subscribe((data: any) => {
      this.allInvitations.unshift(data);
      // Handle the user value here
    });


  }
  ngOnDestroy() {
    // Unsubscribe to avoid memory leaks
    if (this.invitationSubscription) {
      this.invitationSubscription.unsubscribe();
    }
  }
  allInvitations: Invitation[] = [];
  allUnTouchedInvitations: Invitation[] = [];

  constructor(private invitationService: InvitationService, private chatService: ChatService) { }

  getAllInvitationOfCurrentUser() {
    this.invitationService.getAllInvitations().subscribe((data: any) => {
      this.allInvitations = data;
      this.allUnTouchedInvitations = data;
    }, (err: any) => {
      //  this.authService.logOut();
      console.log(err);

      AppUtils.showMessage('error', err.error.message);

    })
  }



  acceptOrDeclineInvitation(index:any,id: any, status: any) {
    const updateInvitationRequest = new UpdateInvitationRequest();
    updateInvitationRequest.id = id;
    updateInvitationRequest.requestStatus = status;
    this.invitationService.acceptOrRejectRequest(updateInvitationRequest).subscribe((data: any) => {
      AppUtils.showMessage('success',data.message)
    console.log(data);
       if(data.message=='Invitation Rejected successfully'){
        this.allInvitations = this.allInvitations.filter(f => {
          return f.id != id;
        })
       
       }
       else{
        this.allInvitations[index].requestStatus = InvitaionStatus.ACCEPTED;
        this.allInvitations[index].invitationMessage = data.invitationMessage;
       }
    }, (err:any) => {
      AppUtils.showMessage('error', err.error.message);
  
    })
  }

  calculateTimeDifference(sentDate: any): string {
    const currentTime = new Date();
    const sendDate = new Date(sentDate);
    const difference = currentTime.getTime() - sendDate.getTime();

    // Calculate days, hours, minutes, and seconds
    const days = Math.floor(difference / (1000 * 60 * 60 * 24));
    const hours = Math.floor((difference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    const minutes = Math.floor((difference % (1000 * 60 * 60)) / (1000 * 60));
    const seconds = Math.floor((difference % (1000 * 60)) / 1000);

    if (days > 0) {
      return `${days} day${days > 1 ? 's' : ''} ago`;
    } else if (hours > 0) {
      return `${hours} hour${hours > 1 ? 's' : ''} ago`;
    } else if (minutes > 0) {
      return `${minutes} min${minutes > 1 ? 's' : ''} ago`;
    } else {
      return `${seconds} sec${seconds > 1 ? 's' : ''} ago`;
    }
  }

  searchValue = "";

  searchInvitation() {
    if (this.searchValue == '')
      this.allInvitations=this.allUnTouchedInvitations;
    else {
      this.allInvitations = this.allUnTouchedInvitations.filter(e => {
        return e.sender.email.toLowerCase().includes(this.searchValue.toLowerCase()) || e.sender.userName.toLowerCase().includes(this.searchValue.toLowerCase());
      });
    }

  }

}
