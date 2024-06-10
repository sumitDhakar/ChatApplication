import { ChatGroupMembers } from "../models/chat-group-members";

export class GroupRequest {
    id: number=0;
    groupName: string ='';
    chatGroupMembers:ChatGroupMembers[]=[];
    profileGroupImage!:File;
    description: string ='';
}

