import { User } from "../models/user";

export class ConversationResponse {

    id: any;
  sender: User = new User();
  recipient: User = new User();
  conversationId: string = '';
  lastMessageAt: string = '';
  lastMessage: string = '';
  unSeenMessageCount:number=0;
}
