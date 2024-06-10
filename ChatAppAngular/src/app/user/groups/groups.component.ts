import { Component, OnInit } from '@angular/core';
import { GroupService } from '../services/group-service';
import { GroupChat } from '../models/group-chat';
import { AppUtils } from 'src/app/utils/app-utils';
import { GroupRequest } from '../payload/group-request';
import { ContactService } from '../services/contact.service';
import { User } from '../models/user';
import { ChatGroupMembers } from '../models/chat-group-members';
import { Router } from '@angular/router';

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.scss']
})
export class GroupsComponent implements OnInit {
  groupChat: GroupChat = new GroupChat();
  groupList: GroupChat[] = [];
  groupRequest: any = new GroupRequest();
  searchValue = "";

  allGroupOfCurrentUser: Map<any, GroupChat[]> = new Map<any, any[]>;
  allUnTouchedGroupOfCurrentUser: Map<any, GroupChat[]> = new Map<any, any[]>;

  allContactsOfCurrentUser: Map<any, any[]> = new Map<any, any[]>;
  constructor(private contactService: ContactService, private groupService: GroupService,
    private router: Router
  ) {
    // this.groupChat.members = [];
  }


  navigateToConversation(goto: string) {
    // Assuming clocu is available in your component's context
    const recipientId = goto;
    this.router.navigate(['/gapshap', { outlets: { convermain: ['group-conversation', recipientId] } }]);
  }


  ngOnInit(): void {
    this.getAllContacts();
    this.getAllGroup();
  }

  // -------------------------------group method implementation  start-----------------------------------------


  createGroup() {
    //  console.log(this.groupChat.members);
    const selectedMembers: ChatGroupMembers[] = [];
    for (const group of this.allContactsOfCurrentUser.values()) {
      for (const user of group) {
        if (user.isSelected) {
          const m: ChatGroupMembers = new ChatGroupMembers();
          m.userId = user;
          m.isLeader = false;
          m.deleted = false;
          selectedMembers.push(m);
        }
      }
    }
    this.groupChat.members = selectedMembers;

    this.groupService.createGroup(this.groupChat).subscribe((data: any) => {
      // this.group= new GroupChat();


      AppUtils.showMessage('success', 'group Created successfully.')
      AppUtils.modelDismiss("add")
      this.getAllGroup();
    }, (err: any) => {

      console.log(err);

      AppUtils.showMessage('error', err.error.message);

    })
  }


  openFileInput(id: any) {
    // Trigger a click on the hidden file input
    const fileInput = document.getElementById(id) as HTMLInputElement;
    fileInput.click();
  }
  openFile(target: any) {
    target.click();
  }
  getAllContacts() {

    this.contactService.getAllContactsOfCurrentUser(AppUtils.GETCONTACT_FOR_CONTACTLIST).subscribe((data: any) => {
      this.allContactsOfCurrentUser = new Map(Object.entries(data.data));

    }, (err: any) => {

      console.log(err);

      AppUtils.showMessage('error', err.error.message);

    })
  }


  selectFile(event: any) {
    this.groupChat.profileGroupImage = event.target.files[0];

    var reader = new FileReader();
    console.log(event);

    reader.onload = function (e) {
      let button = document.getElementById("file");
      button?.setAttribute("src", e!.target!.result + "");
    }
    reader.readAsDataURL(event.target.files[0]);
  }


  getAllGroup() {

    this.groupService.getAllGroupOfCurrentUser(AppUtils.GETCONTACT_FOR_GROUPLIST).subscribe((data: any) => {
      this.allUnTouchedGroupOfCurrentUser = new Map(Object.entries(data.data));
      this.allGroupOfCurrentUser = this.allUnTouchedGroupOfCurrentUser

    }, (err: any) => {

      console.log(err);

      AppUtils.showMessage('error', err.error.message);

    })
  }

  setDataForUpdate(id: number) {

    this.groupService.getGroupByConversationId(id).subscribe((data: any) => {


      this.groupChat = data.data;

      this.groupRequest = this.groupChat;


    })
  }
  // updating data
  updateGroup() {

    this.groupService.updateGroup(this.groupRequest).subscribe((data: any) => {


      AppUtils.showMessage('success', 'Profile Update Successfully')
      AppUtils.modelDismiss('add')

    }, (err: any) => {


      AppUtils.showMessage('error', err.error.message);

    })

  }

  deleteGroup(deleteId: any) {
    this.groupService.deleteGroup(deleteId).subscribe((data: any) => {
      AppUtils.showMessage('success', 'Group Deleted successfully.')

      this.getAllGroup();

    })
  }




  searchGroup() {
    if (this.searchValue === '') {
      // If search value is empty, show all contacts
      this.allGroupOfCurrentUser = new Map<any, any[]>(this.allUnTouchedGroupOfCurrentUser);
    } else {
      // Filter contacts based on search value
      const filteredContacts = new Map<any, any[]>();
      this.allUnTouchedGroupOfCurrentUser.forEach((value, key) => {
        const filteredEntries = value.filter((groupContact: any) =>
          groupContact.groupName.toLowerCase().includes(this.searchValue.toLowerCase())

        );
        if (filteredEntries.length > 0) {
          filteredContacts.set(key, filteredEntries);
        }
      });
      this.allGroupOfCurrentUser = filteredContacts;
    }
  }
  // -------------------------------group method implementation  end-----------------------------------------
}
