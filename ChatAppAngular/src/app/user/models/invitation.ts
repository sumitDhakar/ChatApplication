import { InvitaionStatus } from "src/app/utils/constants/invitaion-status";
import { User } from "./user";

export class Invitation {
    id:number= 0;
    sender: User = new User();
    recipient: User = new User();
    requestStatus: InvitaionStatus = InvitaionStatus.NEW;
    invitationMessage: any = "Be My Friend 😊😊";
    createdAt: any;
    isAccepted = false;

}
