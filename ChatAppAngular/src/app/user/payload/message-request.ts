import { ChatFiles } from "../models/chat-files";

export class MessageRequest {
    message: string='';
    sender: string='';
    recipient: string='';
    conversationId: string='';
    operationType: string='';
	messageFiles: ChatFiles[] = [];
	files:File[]=[]
  
    constructor(
      message: string,
      sender: string,
      recipient: string,
      conversationId: string,
      operationType: string
    ) {
      this.message = message;
      this.sender = sender;
      this.recipient = recipient;
      this.conversationId = conversationId;
      this.operationType = operationType;
    }
  }
  