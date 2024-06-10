import { UserStatus } from "src/app/utils/constants/user-status";

export class UserActiveStatus {
	  id :any;
	
	  lastSeen :any;
	
	  isOnline :any;
	  status:UserStatus=UserStatus.OFFLINE;
}
