export class ApiRoutes {
  public static BASE_URL = 'http://localhost:8080/gapshap'

  public static IMAGE_URL = "http://localhost:8080/gapshap/auth/profileImage/"


  //   ---------- User ----------------
  public static USER_REGISTRATION = this.BASE_URL + '/auth/';
  public static USER_GetImage = this.BASE_URL + '/auth/profileImage';
  public static USER_VERIFY = this.BASE_URL + '/auth/verify';
  public static USER_LOGIN = this.BASE_URL + '/auth/login';
  public static USER_CURRENT = this.BASE_URL + '/auth/current-user';
  public static OTP_RESEND = this.BASE_URL + '/auth/resend';
  public static USER_BY_ID = this.BASE_URL + "/user/"
  public static USER_ALL = this.BASE_URL + "/user/"
  public static USER_STATUS_GET  =this.BASE_URL +"/user/status/";
public static USER_UPDATE= this.BASE_URL+"/user/update";

  //   ---------- Conversation -----------
  public static CONVERSATION_CREATE = this.BASE_URL +"/conversation/";
  public static CONVERSATION_GET = this.BASE_URL +"/conversation/get";
  public static CONVERSATION_GET_CURRENTUSER = this.BASE_URL +"/conversation/getConversations";

//   ---------- Message URL -----------
public static SEND_MESSAGE = this.BASE_URL +"/chat/sendMessage";
public static SEND_MESSAGE_GROUP = this.BASE_URL +"/chat/sendMessageGroup";
public static GET_MESSAGE_HISTORY = this.BASE_URL +"/chat/oldMessageHistory";
public static DELETE_MESSAGE = this.BASE_URL +"/chat/deleteMessage";
public static UPDATE_MESSAGE_MARK_AS_SEEN = this.BASE_URL +"/chat/markedAsRead";


  //   ---------- Invitation -----------
  public static INVITATION_CREATE = this.BASE_URL +"/invitation/invite";
  public static INVITATION_GET_ALL = this.BASE_URL +"/invitation/getAll";
  public static INVITATION_UPDATE = this.BASE_URL +"/invitation/update";
  public static INVITATION_DELETE = this.BASE_URL +"/invitation/delete";



   //   ---------- CONTACT -----------
   public static CONTACT_CREATE = this.BASE_URL +"/contacts/invite";
   public static GetALL_CONTACT = this.BASE_URL +"/contacts/allContacts";
   public static CONTACT_UPDATE = this.BASE_URL +"/contacts/update";
   public static CONTACT_DELETE = this.BASE_URL +"/contacts/deleteContact";


  //  -----------Group-----------------------
  public static GROUP_CREATE = this.BASE_URL +"/groups/createGroup";
  public static GROUP_UPDATE = this.BASE_URL +"/groups/updateGroup";
  public static GetALL_GROUP = this.BASE_URL +"/groups/getAllGroups";
  public static GetById_GROUP = this.BASE_URL +"/groups/getGroupById";
  public static DELETE_GROUP = this.BASE_URL +"/groups/deleteGroup";



}
