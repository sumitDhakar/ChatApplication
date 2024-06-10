import { MessageRequest } from "../payload/message-request";
import { ChatGroupMembers } from "./chat-group-members";
import { User } from "./user";

export class GroupChat {

    id: number=0;
    groupName: string ='';
    members:ChatGroupMembers[]=[];
    profileGroup='';
    profileGroupImage='';
    description: string ='';
    conversationId:string ='';
}
