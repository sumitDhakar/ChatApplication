import { Component, OnInit } from '@angular/core';
import { InvitationService } from '../services/invitation.service';
import { InvitationRequest } from '../payload/invitation-request';
import { AppUtils } from 'src/app/utils/app-utils';
import { ChatService } from '../services/chat.service';
import { AuthenticationService } from '../services/authentication.service';
import { ContactService } from '../services/contact.service';
import { Contacts } from '../models/contacts';
import { User } from '../models/user';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-contacts',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.scss']
})
export class ContactsComponent implements OnInit {

  unTouchedContactList: Map<any, any[]> = new Map<any, any[]>;
  searchValue = "";
  allContactsOfCurrentUser: Map<any, any[]> = new Map<any, any[]>;
  allUsersListForCreateContact: any[] = [];
  allUnTouchedUsersListForCreateContact: any[] = [];

  constructor(private invitationService: InvitationService,
    private contactService: ContactService, private userService: UserService,
    private authService: AuthenticationService
  ) {

  }
  ngOnInit(): void {
    this.getAllContacts();
    this.getAllUsersToAddContact();
  }


  setValueForSendInvitation(emailToInvite: any) {
    this.invitationRequest.recipient = emailToInvite;
  }

  getAllUsersToAddContact() {
    this.userService.getAllUserToAddContact().subscribe((data: any) => {
      this.allUsersListForCreateContact = data.data;
      this.allUnTouchedUsersListForCreateContact = data.data;

    }, (err: any) => {

      AppUtils.showMessage('error', err.error.message);

    })
  }



  getAllContacts() {
    this.contactService.getAllContactsOfCurrentUser(AppUtils.GETCONTACT_FOR_CONTACTLIST).subscribe((data: any) => {
      this.allContactsOfCurrentUser = new Map(Object.entries(data.data));
      this.unTouchedContactList = new Map(Object.entries(data.data));

    }, (err: any) => {

      console.log(err);

      AppUtils.showMessage('error', err.error.message);

    })
  }

  invitationRequest: InvitationRequest = new InvitationRequest();
  createinvitation() {
    const senderEmail = this.authService.getEmail();
    if (senderEmail != null)
      this.invitationRequest.sender = senderEmail;
    // this.chatService.sendMessage(this.invitationRequest,"send_Invitation");

    this.invitationService.createInvitation(this.invitationRequest).subscribe((data: any) => {
      AppUtils.showMessage('success', 'Invitation Sent Successfully')
      AppUtils.modelDismiss("add")
    
   
    }, (err: any) => {
    

      AppUtils.showMessage('error', err.error.message);
    })
  }

  deleteContacts(deleteId: any) {

    this.contactService.deleteContacts(deleteId).subscribe((data: any) => {
      AppUtils.showMessage('success', 'Contacts Deleted successfully.')
      this.removeContactFromContactList(deleteId);

    })

  }


  searchContacts() {

    if (this.searchValue === '') {
      // If search value is empty, show all contacts
      this.allContactsOfCurrentUser = new Map<any, any[]>(this.unTouchedContactList);
    } else {
      // Filter contacts based on search value
      const filteredContacts = new Map<any, any[]>();
      this.unTouchedContactList.forEach((value, key) => {
        const filteredEntries = value.filter((contact: any) =>
          contact.email.toLowerCase().includes(this.searchValue.toLowerCase()) ||
          contact.userName.toLowerCase().includes(this.searchValue.toLowerCase())
        );
        if (filteredEntries.length > 0) {
          filteredContacts.set(key, filteredEntries);
        }
      });
      this.allContactsOfCurrentUser = filteredContacts;
    }
  }


  searchingForUserEmail() {

    if (this.invitationRequest.recipient == '')
      this.allUsersListForCreateContact = this.allUnTouchedUsersListForCreateContact;
    else {
      this.allUsersListForCreateContact = this.allUnTouchedUsersListForCreateContact.filter(e => {
        return e[0].toLowerCase().includes(this.invitationRequest.recipient.toLowerCase());
      });
    }
  }

  removeContactFromContactList(messageId: any) {
    this.allContactsOfCurrentUser.forEach((value: any[], key: any) => {
      const index = value.findIndex((message: any) => message.id === messageId);
      if (index !== -1) {
        value.splice(index, 1);
        // If you also want to delete the key from the map if the array becomes empty
        if (value.length === 0) {
          this.allContactsOfCurrentUser.delete(key);
        }
      }
    });
  }

}
