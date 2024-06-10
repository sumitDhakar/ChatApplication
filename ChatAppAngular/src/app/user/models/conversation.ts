import { User } from "./user";

export class Conversation {
  id: any;
  sender: User = new User();
  recipient: User = new User();
  conversationId: string = '';
  lastMessageAt: string = '';
  lastMessage: string = '';

}
