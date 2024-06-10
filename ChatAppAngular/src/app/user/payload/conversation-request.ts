export class ConversationRequest {
  recipient:any;
  sender:any
  constructor(sender:any,recipient:any){
    this.recipient=recipient;
    this.sender=sender;
  }
}
