import { GroupChat } from "./group-chat";
import { User } from "./user";

export class ChatGroupMembers {
    id=0;
    groupChatId:GroupChat=new GroupChat();
    userId:User=new User();
    isLeader=false;
    deleted=false;

    
}
