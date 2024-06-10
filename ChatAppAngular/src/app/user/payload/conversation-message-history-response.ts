
export class ConversationMessageHistoryResponse {
  id: number=0;
  sender: number=0;
  recipient: number=0;
  message: string='';
  conversationId: string='';
  sentAt: string='';
  recieved: string='';
  chatFiles: any[]=[]; // Assuming ChatFiles is another class
  images: any[]=[]; // Assuming ChatFiles is another class
  isSeen: string='';

  constructor(
    id: number,
    sender: number,
    recipient: number,
    message: string,
    conversationId: string,
    sentAt: string,
    recieved: string,
    chatFiles: any[],
    images: any[],
    isSeen: string
  ) {
    this.id = id;
    this.sender = sender;
    this.recipient = recipient;
    this.message = message;
    this.conversationId = conversationId;
    this.sentAt = sentAt;
    this.recieved = recieved;
    this.chatFiles = chatFiles;
    this.images = images;
    this.isSeen = isSeen;
  }
}
