import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ApiRoutes } from 'src/app/utils/api-routes';
import { UserStatus } from 'src/app/utils/constants/user-status';
import { ContactService } from '../services/contact.service';
import { AppUtils } from 'src/app/utils/app-utils';
import { ConversationService } from '../services/conversation.service';
import { ConversationRequest } from '../payload/conversation-request';
import { ChatService } from '../services/chat.service';
import { User } from '../models/user';
import { Conversation } from '../models/conversation';
import { ConversationResponse } from '../payload/conversation-response';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
declare var $: any;
@Component({
  selector: 'app-chats',
  templateUrl: './chats.component.html',
  styleUrls: ['./chats.component.scss']
})
export class ChatsComponent implements OnInit, AfterViewInit {
  private userStatusSubscription: Subscription|undefined;
  private userMessageGetSubscription: Subscription|undefined;
  ngAfterViewInit(): void {
    this.intialiseStatusComponent();

  }

  ngOnDestroy(): void {
    // Unsubscribe from the observable when the component is destroyed
    if (this.userStatusSubscription) {
      this.userStatusSubscription.unsubscribe();
    }
    if (this.userMessageGetSubscription) {
      this.userMessageGetSubscription.unsubscribe();
    }
  }


  ngOnInit(): void {
    this.getAllUsersContactToStartConversation();
    this.getAllConversationOfUser();
    this.userStatusSubscription = this.chatService.User_Status_Manager.subscribe((data: any) => {

      this.conversationListOfCurrentUser.forEach(conversation => {
        // Check if the recipient's ID matches the provided ID
        if (conversation.recipient.id === data.id) {
          // Manipulate the conversation object here
          // For example, change the user status
          conversation.recipient.userStatus.isOnline = data.isOnline;
        }
      });

      this.contactList.forEach(contact => {
        // Check if the recipient's ID matches the provided ID
        if (contact.id === data.id) {

          contact.userStatus.isOnline = data.isOnline;
        }
      });


    });

    this.userMessageGetSubscription =this.chatService.get_message.subscribe((data: any) => {

      const conversation = this.conversationListOfCurrentUser.find(conversation =>
        conversation.sender.id == data.recipient && conversation.conversationId == data.conversationId
      );

      if (conversation) {
        this.shiftCurrentConversationToTop(data, conversation);
      }
    });
  }
  constructor(private contactService: ContactService, private chatService: ChatService,
    private conversationService: ConversationService, private router: Router,
  ) { }



  conversationListOfCurrentUser: ConversationResponse[] = [];
  contactList: User[] = [];
  unTouchedContactList: any[] = [];
  imageUrl = ApiRoutes.IMAGE_URL;
  searchValue = "";
  userStatus = UserStatus;

  getAllConversationOfUser() {
    this.conversationService.getAllConversationListOfCurrentUSer().subscribe((data: any) => {
      this.conversationListOfCurrentUser = data.data;
      this.carouselItems = data.carouselItem;

    }, err => {
      AppUtils.showMessage("error", err.error.message);
    })
  }


  getAllUsersContactToStartConversation() {
    this.contactService.getAllContactsOfCurrentUser(AppUtils.GETCONTACT_FOR_CONVERSATION).subscribe((data: any) => {
      this.contactList = data.data;
      this.unTouchedContactList = data.data;
    })
  }
  startConversation(conversationEmail: string) {
    this.conversationService.createConversation(new ConversationRequest(null, conversationEmail)).subscribe((data: any) => {
      const found = this.conversationListOfCurrentUser.some(conversation => {
        return conversation.recipient.id === data.data.recipient.id;
      });

      if (!found) {
        this.conversationListOfCurrentUser.unshift(data.data);
      }
      AppUtils.modelDismiss('add');

    }, (err: any) => {
      AppUtils.showMessage("error", err.error.message)
    })
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

  searchingInChats() {
    if (this.searchValue == '')
      this.contactList = this.unTouchedContactList;
    else {
      this.contactList = this.unTouchedContactList.filter(e => {
        return e.email.toLowerCase().includes(this.searchValue.toLowerCase()) || e.userName.toLowerCase().includes(this.searchValue.toLowerCase());
      });
    }
  }
  carouselItems: any[] = [
  ];

  intialiseStatusComponent() {
    $("#user-status-carousel").owlCarousel({
      items: 4,
      loop: false,
      margin: 16,
      nav: false,
      dots: false
    });
  }

  shiftCurrentConversationToTop(data: any, conversation: any) {
    const message = data.message
    const sentAt = data.sentAt

    const index = this.conversationListOfCurrentUser.indexOf(conversation);
    this.conversationListOfCurrentUser.splice(index, 1); // Remove the found conversation
    conversation.lastMessageAt = sentAt;
    conversation.lastMessage = message;
    conversation.unSeenMessageCount = conversation.unSeenMessageCount + 1;
    this.conversationListOfCurrentUser.unshift(conversation); // Add it to the first index
  }


  navigateToConversationAndSetMessagesSeen(goto: any, messageCountCurrent: any) {
    if (messageCountCurrent > 0) {
      this.increaseOrDecreaseCountOfUnSeenMessages(goto, 0);
    }
    this.router.navigate(['/gapshap', { outlets: { convermain: ['conversation', goto] } }]);

  }



  increaseOrDecreaseCountOfUnSeenMessages(conversationRecipieantId: any, messageCount: any) {
    this.conversationListOfCurrentUser.forEach(conversation => {
      // Check if the recipient's ID matches the provided ID
      if (conversation.recipient.id === conversationRecipieantId) {
        conversation.unSeenMessageCount = messageCount;
      }
    });
  }



}
