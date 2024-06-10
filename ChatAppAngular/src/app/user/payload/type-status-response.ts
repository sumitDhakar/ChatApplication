export class TypeStatusResponse {
    id: number = 0;
    userName: string = ' ';
    profileName: string = ' ';
    email: string = ' ';
    toEmail = " ";
    conversationId: string  = ' ';
    userStatus: string = 'ONLINE';
    constructor(id: number, name: string, email: string, conversationId: string,userStatus:string,toEmail:string,profileName:string) {
        this.id = id;
        this.userName = name;
        this.email = email;
        this.toEmail = toEmail;
        this.conversationId = conversationId;
        this.userStatus = userStatus;
        this.profileName=profileName;
    }
}
