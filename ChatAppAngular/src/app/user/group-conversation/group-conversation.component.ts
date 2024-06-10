import { DatePipe, Location } from '@angular/common';
import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { AppUtils } from 'src/app/utils/app-utils';
import { UserStatus } from 'src/app/utils/constants/user-status';
import { Conversation } from '../models/conversation';
import { User } from '../models/user';
import { ConversationMessageHistoryResponse } from '../payload/conversation-message-history-response';
import { ConversationRequest } from '../payload/conversation-request';
import { MessageRequest } from '../payload/message-request';
import { ChatService } from '../services/chat.service';
import { ContactService } from '../services/contact.service';
import { ConversationService } from '../services/conversation.service';
import { UserService } from '../services/user.service';
import { GroupService } from '../services/group-service';
import { GroupChat } from '../models/group-chat';
import { ChatGroupMembers } from '../models/chat-group-members';
import { TypeStatusResponse } from '../payload/type-status-response';
import { ChatFiles } from '../models/chat-files';
declare var $: any;

@Component({
  selector: 'app-group-conversation',
  templateUrl: './group-conversation.component.html',
  styleUrls: ['./group-conversation.component.scss']
})
export class GroupConversationComponent implements OnInit, AfterViewInit {
  @ViewChild('messageArea') messageArea!: ElementRef;

  private usertypingStatusSubscription: Subscription | undefined;
  private userGetMessageSubscription: Subscription | undefined;
  // private userOnlineStatusSubscription: Subscription | undefined;


  userStatus = UserStatus;
  searchValue = "";
  todayDate = new Date().toJSON().slice(0, 10).replace(/-/g, '-');

  constructor(private groupServices: GroupService, private activatedRoute: ActivatedRoute, private chatService: ChatService,
    private datePipe: DatePipe) { }
  ngAfterViewInit(): void {
    // this.ngAfterContentChecked();
  }


  appserviceUser = AppUtils;

  ngOnInit(): void {

    this.getProfileUser();
    this.intializeBar();
    this.activatedRoute.params.subscribe((param: any) => {
      let id = param['id'];
      this.conversationId = id;
      this.getGroupDetailsAndMessagesByConversationId(id);
      // this.getAllUsersContactToStartConversation();
    })
    this.userGetMessageSubscription = this.chatService.get_message_group.subscribe((data: any) => {
      if (this.conversationId === data.conversationId) {
        this.pushNewMessageToList(this.datePipe.transform(data.sentAt, 'yyyy-MM-dd'), data);
      }
    });

    this.usertypingStatusSubscription = this.chatService.get_User_TypingStatus.subscribe((data: TypeStatusResponse) => {
      if (this.conversationId === data.conversationId && data.email != this.currentUser.email) {
        // Manipulate the conversation object here
        this.currentTypingUserData = data;
        if (data.userStatus === 'typing') {
          this.currentTypingUserData.userStatus = UserStatus.TYPING

          let i = setTimeout(() => {
            if (this.currentTypingUserData.userStatus == this.userStatus.TYPING) {
              this.currentTypingUserData = new TypeStatusResponse(0, '', '', '', '', 'ONLINE', '');

            }
          }, 3000);
        }
        if ((data.userStatus === 'ONLINE')) {
          this.currentTypingUserData.userStatus = UserStatus.ONLINE
        }

      }

    });

  }


  ngOnDestroy() {
    // Unsubscribe to avoid memory leaks
    if (this.userGetMessageSubscription) {
      this.userGetMessageSubscription.unsubscribe();
    }

    if (this.usertypingStatusSubscription) {
      this.usertypingStatusSubscription.unsubscribe();
    }
  }


  // -------------started code for group chats------------------------------
  currentUser: User = new User();
  currentUserId: any;
  groupDetails: GroupChat = new GroupChat();
  composedMessage: MessageRequest = new MessageRequest('', '', '', '', '');
  allMessageChatFiles: ChatFiles[] = [];
  messages: Map<any, ConversationMessageHistoryResponse[]> = new Map<any, any[]>;
  conversationId = '';
  groupMembersCurrent: ChatGroupMembers[] = [];
  selectedFileName: string | null = null;
  currentTypingUserData: TypeStatusResponse = new TypeStatusResponse(0, '', '', '', '', 'ONLINE', '');


  sendTypingStatus() {

    const typingStatusResponse = new TypeStatusResponse(this.currentUserId, this.currentUser.userName, this.currentUser.email, this.conversationId, 'typing', "", this.currentUser.profileName);
    this.chatService.sendMessage(typingStatusResponse, 'send_typing_status');

  }




  findUserByUserId(userIdToFind: number): any {
    const contactUser = this.groupMembersCurrent.find(member => member.userId.id === userIdToFind);
    const user = contactUser?.userId;

    return user;
  }


  getFieldFromUserObject(senderId: number, getFieldName: string): string {

    if (senderId === this.currentUserId) {
      return '';
    } else {
      const member = this.findUserByUserId(senderId);
      if (member) {
        return member[getFieldName];
      } else {
        return 'Unknown User';
      }
    }
  }





  getProfileUser() {
    const storedItem = localStorage.getItem('user');
    if (storedItem != null) {

      this.currentUser = JSON.parse(storedItem);
      this.currentUserId = this.currentUser.id;
    }

  }
  getGroupDetailsAndMessagesByConversationId(conversationId: any) {
    this.groupServices.getGroupByConversationId(conversationId).subscribe((data: any) => {
      this.groupDetails = data.data;
      this.groupMembersCurrent = this.groupDetails.members;
      this.getOldMessages();

    })
  }

  sendMessageToGroupMembers() {
    if (this.composedMessage.message == '' && this.composedMessage.files.length == 0) {
      return;
    }
    else {
      const composedMessage: MessageRequest = new MessageRequest(this.composedMessage.message, this.currentUser.email, "", this.conversationId, '');
      composedMessage.files = this.composedMessage.files
      this.chatService.sendMessageToServerToMaintainHistory(composedMessage, AppUtils.SEND_MESSAGE_FOR_GROUP).subscribe((data: any) => {
        const typingStatusResponse = new TypeStatusResponse(this.currentUserId, this.currentUser.userName, this.currentUser.email, this.conversationId, 'ONLINE', "", this.currentUser.profileName);

        this.chatService.sendMessage(typingStatusResponse, 'send_typing_status');
        this.composedMessage = new MessageRequest('', '', '', '', '');
        this.composedMessage.files = [];
        this.pushNewMessageToList(this.datePipe.transform(data.data.sentAt, 'yyyy-MM-dd'), data.data);

      })
    }


  }
  pushNewMessageToList(key: any, message: any) {
    // Check if the key already exists in the map
    if (this.messages.has(key)) {
      // If key exists, get the array associated with it and push the new message
      const existingMessages = this.messages.get(key);
      if (existingMessages) {
        existingMessages.push(message);
      } else {
        console.error("Existing messages array is null or undefined.");
      }
    } else {
      // If key doesn't exist, create a new array with the message
      this.messages.set(key, [message]);
    }
  }


  // getting old conversation of group
  getOldMessages() {
    this.chatService.getMessageHistoryByConversationId(this.conversationId, AppUtils.GET_MESSAGE_FOR_GROUP).subscribe((data: any) => {
      this.messages = new Map(Object.entries(data.data));
      this.allMessageChatFiles = data.chatFiles
      this.updateMessageIsSeenStatus(this.conversationId);


    })
  }
  updateMessageIsSeenStatus(conversationId: any) {
    this.chatService.updateMessageIsSeenStatus(conversationId).subscribe((data: any) => {

    }, (err: any) => {

      AppUtils.showMessage('error', err.error.message);

    })
  }

  fileInput(event: any) {
    const file = event.target.files[0];
    this.selectedFileName = file ? file.name : null;
    // Your logic to handle the selected file
    this.selectFileToUpload(event);
  }


  selectFileToUpload(event: any) {

    if (this.composedMessage.messageFiles.filter(f => {
      return f.fileName == event.target.files[0].name;
    }).length == 0) {
      this.composedMessage.files.push(event.target.files[0]);
      this.composedMessage.messageFiles.push(event.target.files[0].name);
    }
    console.log('Selected file:', this.composedMessage.files);
    console.log('Selected chatFiles:', this.composedMessage.messageFiles);

  }

  removeFile(fileName: any) {
    if (this.composedMessage.messageFiles.filter(f => {
      return f.fileName == fileName;
    }).length == 0) {
      this.composedMessage.files = this.composedMessage.files.filter(f => {
        return f.name != fileName;
      })
      this.composedMessage.messageFiles = this.composedMessage.messageFiles.filter(f => {
        return f.fileName != fileName;
      })
    }
  }


  openFileInput() {
    // Trigger a click on the hidden file input
    const fileInput = document.getElementById("files") as HTMLInputElement;
    fileInput.click();
  }




  isProfileOpen = false;

  toggleProfile(open: boolean) {
    if (open)
      this.div.nativeElement.style = 'width:60% !important'
    else
      this.div.nativeElement.style = 'width:100% !important'

  }

  @ViewChild('di') div!: ElementRef;

  intializeBar() {
    // Ensure the DOM is ready before executing your script
    $(document).ready(function () {
      // Handle user-profile-hide click event
      $("#user-profile-hide").click(function () {

        $(".user-profile-sidebar").hide();
      });

      // Handle user-profile-show click event
      $(".user-profile-show").click(function () {
        $(".user-profile-sidebar").show();
      });

      // Handle chat-user-list item click event
      $(".chat-user-list li a").click(function () {
        $(".user-chat").addClass("user-chat-show");
      });

      // Handle user-chat-remove click event
      $(".user-chat-remove").click(function () {
        $(".user-chat").removeClass("user-chat-show");
      });
    });
  }

  // scrollToBottom(): void {
  //   try {
  //     this.chatContainer.nativeElement.scrollToBottom = this.chatContainer.nativeElement.scrollHeight;
  //   } catch(err) { }
  // }

  deleteMessages(id: any) {
    this.chatService.deleteMessges(id).subscribe((data: any) => {
      this.removeMessageFromConversation(id);
    })
  }

  async copyTextToClipboard(data: any, typeDefination: any) {
    try {
      let textToCopy;

      // Check if the data is a string (text)
      if (typeDefination === 'string') {
        textToCopy = data;
      } else {
        // For files or images, convert the URL to a Blob object
        this.downloadFile(data, '', false).then(async value => {
          if (value)
            textToCopy = URL.createObjectURL(value); // Handle the downloaded file blob here

        });

      }

      // Create a temporary textarea element to hold the text or URL
      const textarea = document.createElement('textarea');
      textarea.value = textToCopy;
      document.body.appendChild(textarea);

      // Select the text inside the textarea
      textarea.select();

      // Copy the selected text to the clipboard
      document.execCommand('copy');

      // Remove the temporary textarea
      document.body.removeChild(textarea);

      console.log('Copied to clipboard:', textToCopy);
    } catch (error) {
      console.error('Error copying to clipboard:', error);
    }
  }



  async downloadFile(fileUrl: string, fileName: string, downloadOrNot: boolean): Promise<Blob | null> {
    try {
      const response = await fetch(fileUrl);
      const blob = await response.blob();
      const anchor = document.createElement('a');
      anchor.href = URL.createObjectURL(blob);
      anchor.download = fileName != null ? fileName : ''; // Provide a filename here if needed
      if (downloadOrNot)
        anchor.click(); // Trigger the download
      return blob;
    } catch (error) {
      console.error('Error downloading file:', error);
      return null;
    }
  }
  removeMessageFromConversation(messageId: any) {
    this.messages.forEach((value: ConversationMessageHistoryResponse[], key: any) => {
      const index = value.findIndex((message: ConversationMessageHistoryResponse) => message.id === messageId);
      if (index !== -1) {
        value.splice(index, 1);
        // If you also want to delete the key from the map if the array becomes empty
        if (value.length === 0) {
          this.messages.delete(key);
        }
      }
    });
  }

  // -------------end  code for group chats------------------------------

}
