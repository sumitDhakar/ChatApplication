import { UserActiveStatus } from "./user-active-status";

export class User {
  id=0;

	userName=''
	about=''
	  email=''
	
	  bio=''
	
	  profileName:string = ''

	  userStatus:UserActiveStatus=new UserActiveStatus();
	  location=''
	  phoneNumber=''

}
