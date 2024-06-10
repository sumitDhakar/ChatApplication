import { AfterContentChecked, AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../services/user.service';
import { ActivatedRoute } from '@angular/router';
import { User } from '../models/user';

import { ConversationService } from '../services/conversation.service';
import { ConversationRequest } from '../payload/conversation-request';
import { Conversation } from '../models/conversation';
import { AppUtils } from 'src/app/utils/app-utils';
import { UserStatus } from 'src/app/utils/constants/user-status';
import { ChatService } from '../services/chat.service';
import { MessageRequest } from '../payload/message-request';
import { DatePipe, Location } from '@angular/common';
import { Subscription, debounce, fromEvent, timer } from 'rxjs';
import { ContactService } from '../services/contact.service';
import { ConversationMessageHistoryResponse } from '../payload/conversation-message-history-response';
import { TypeStatusResponse } from '../payload/type-status-response';
import { ChatFiles } from '../models/chat-files';
import { AuthenticationService } from '../services/authentication.service';
import { HttpEventType } from '@angular/common/http';

declare var $: any;

@Component({
  selector: 'app-conversation',
  templateUrl: './conversation.component.html',
  styleUrls: ['./conversation.component.scss']
})
// AfterContentChecked  AfterViewInit
export class ConversationComponent implements OnInit {
  private usertypingStatusSubscription: Subscription | undefined;
  private userGetMessageSubscription: Subscription | undefined;
  private userOnlineStatusSubscription: Subscription | undefined;

  searchValue = "";
  unTouchedContactList: any[] = [];
  appserviceUser = AppUtils;

  contactList: any[] = [];
  todayDate = new Date().toJSON().slice(0, 10).replace(/-/g, '-');
  constructor(private userService: UserService, private activatedRoute: ActivatedRoute
    , private contactService: ContactService, private conversationService: ConversationService,
    private chatService: ChatService, private datePipe: DatePipe,
  ) { }

  selectedFileName: string | null = null;
  ngOnInit(): void {

    this.getProfileUser();
    this.intializeBar();
    this.activatedRoute.params.subscribe((param: any) => {
      let id = param['id'];
      this.getUserById(id);
      this.getAllUsersContactToStartConversation();

      this.userGetMessageSubscription = this.chatService.get_message.subscribe((data: any) => {

        if (this.conversation.conversationId === data.conversationId && data.recipient === this.currentUserId) {
          this.pushNewMessageToList(this.datePipe.transform(data.sentAt, 'yyyy-MM-dd'), data);
        }
      });

      this.usertypingStatusSubscription = this.chatService.get_User_TypingStatus.subscribe((data: TypeStatusResponse) => {
        // Check if the recipient's ID matches the provided ID
        // if((data.toEmail!=null||data.toEmail!=undefined)&&(data.conversationId!=null||data.conversationId!=undefined))
        if (this.currentUser.email && this.currentUser.email === data.toEmail && this.conversation.conversationId === data.conversationId) {
          // Manipulate the conversation object here

          if (data.userStatus === 'typing') {
            this.currentUser.userStatus.status = UserStatus.TYPING

            let i = setTimeout(() => {
              if (this.currentUser.userStatus.status == this.userStatus.TYPING) {
                this.currentUser.userStatus.status = UserStatus.ONLINE
              }
            }, 3000);
          }
          if ((data.userStatus === 'ONLINE')) {
            this.currentUser.userStatus.status = UserStatus.ONLINE
          }

        }

      });

      this.userOnlineStatusSubscription = this.chatService.User_Status_Manager.subscribe((data: any) => {
        if (this.recieverUser.id === data.id) {
          if (data.isOnline != undefined || data.isOnline != null)
            this.recieverUser.userStatus.isOnline = data.isOnline;
        }
      });
    })
  }


  ngOnDestroy() {
    // Unsubscribe to avoid memory leaks
    if (this.userOnlineStatusSubscription) {
      this.userOnlineStatusSubscription.unsubscribe();
    }

    if (this.userGetMessageSubscription) {
      this.userGetMessageSubscription.unsubscribe();
    }

    if (this.usertypingStatusSubscription) {
      this.usertypingStatusSubscription.unsubscribe();
    }
  }


  sendTypingStatus() {

    const typingStatusResponse = new TypeStatusResponse(this.currentUserId, this.currentUser.userName, this.currentUser.email, this.conversation.conversationId, 'typing', this.recieverUser.email, this.currentUser.profileName);
    this.chatService.sendMessage(typingStatusResponse, 'send_typing_status');

  }


  // ngAfterViewInit() {
  //   this.scrollToBottom();
  // }

  currentUser: User = new User();
  currentUserId: any;

  getProfileUser() {
    const storedItem = localStorage.getItem('user');
    if (storedItem != null) {

      this.currentUser = JSON.parse(storedItem);
      this.currentUserId = this.currentUser.id;
    }

  }
  userStatus = UserStatus;
  recieverUser: User = new User();
  conversation: Conversation = new Conversation();
  conversationRequest: ConversationRequest = new ConversationRequest(null, null);

  messages: Map<any, ConversationMessageHistoryResponse[]> = new Map<any, any[]>;
  allMessageChatFiles: ChatFiles[] = [];

  getUserById(id: any) {
    this.userService.getUserById(id).subscribe((data: any) => {
      this.recieverUser = data.data;
      this.getConversation();

    })
  }


  getConversation() {

    this.conversationService.getConversation(this.recieverUser.email).subscribe((data: any) => {

      this.conversation = data.data;

      this.getOldMessages();
      this.updateMessageIsSeenStatus(this.conversation.conversationId);
    }, err => {
      this.conversationRequest.recipient = this.recieverUser.email;
      this.conversationRequest.sender = this.currentUser.email;

      this.conversationService.createConversation(this.conversationRequest).subscribe((data: any) => {
        this.conversation = data.data;
        AppUtils.showMessage('success', 'Conversation created successfully');
      })
    })
  }
  updateMessageIsSeenStatus(conversationId: any) {
    this.chatService.updateMessageIsSeenStatus(conversationId).subscribe((data: any) => {

    }, (err: any) => {

      AppUtils.showMessage('error', err.error.message);

    })
  }

  // getting old conversation of sender and recipient
  getOldMessages() {
    this.chatService.getMessageHistoryByConversationId(this.conversation.conversationId, AppUtils.GET_MESSAGE_FOR_Single).subscribe((data: any) => {
      this.messages = new Map(Object.entries(data.data));
      this.allMessageChatFiles = data.chatFiles

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
    const fileInput = document.getElementById("file") as HTMLInputElement;
    fileInput.click();
  }

  composedMessage: MessageRequest = new MessageRequest('', '', '', '', '');
  sendMessageToUser() {

    if (this.composedMessage.message == '' && this.composedMessage.files.length == 0) {
      return;
    }
    else {
      const composedMessage: MessageRequest = new MessageRequest(this.composedMessage.message, this.currentUser.email, this.recieverUser.email, this.conversation.conversationId, '');
      composedMessage.files = this.composedMessage.files
      this.chatService.sendMessageToServerToMaintainHistory(composedMessage, AppUtils.SEND_MESSAGE_FOR_Single).subscribe((data: any) => {
        const typingStatusResponse = new TypeStatusResponse(this.currentUserId, this.currentUser.userName, this.currentUser.email, this.conversation.conversationId, 'ONLINE', this.recieverUser.email, this.currentUser.profileName);

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


  isProfileOpen = false;

  toggleProfile(open: boolean) {
    if (open)
      this.div.nativeElement.style = 'width:64% !important'
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

  startConversation(conversationEmail: string) {
    this.conversationService.createConversation(new ConversationRequest(null, conversationEmail)).subscribe((data: any) => {
      console.log(data.data);

    }, (err: any) => {
      AppUtils.showMessage("error", err.error.message)
    })
    AppUtils.modelDismiss('add');
  }
  searching() {
    if (this.searchValue == '')
      this.contactList = this.unTouchedContactList;
    else {
      this.contactList = this.unTouchedContactList.filter(e => {
        return e.email.toLowerCase().includes(this.searchValue.toLowerCase()) || e.userName.toLowerCase().includes(this.searchValue.toLowerCase());
      });
    }

  }

  getAllUsersContactToStartConversation() {
    this.contactService.getAllContactsOfCurrentUser(AppUtils.GETCONTACT_FOR_CONVERSATION).subscribe((data: any) => {
      this.contactList = data.data;
      this.unTouchedContactList = data.data;
    })
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

}
