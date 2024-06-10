import { InvitaionStatus } from "src/app/utils/constants/invitaion-status";

export class UpdateInvitationRequest {

    id:number= 0;
    requestStatus: InvitaionStatus = InvitaionStatus.ACCEPTED;


}
