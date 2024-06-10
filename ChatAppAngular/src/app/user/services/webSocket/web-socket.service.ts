import { Injectable } from '@angular/core';
import { Stomp } from '@stomp/stompjs';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { MessageMappings } from 'src/app/Config/message-mappings';
import { AuthenticationService } from '../authentication.service';
import { UserStatus } from 'src/app/utils/constants/user-status';
import { UserStatusRequest } from '../../payload/user-status-request';
@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
//   public status = new UserStatusRequest();
//   constructor(private authService:AuthenticationService) { 

    
//     this.status.email = this.authService.getEmail();
//   } 
//   private stompClient: any;

//   users:BehaviorSubject<any> = new BehaviorSubject<any>(null);
// searchUsers:Observable<any> = this.users.asObservable();

//   messageReceived: Subject<any> = new Subject<any>();
//   messageData: Observable<any> = this.messageReceived.asObservable();
//   initializeWebSocketConnection() {
//     // const socket = new SockJS('http://localhost:8080/ws');
//     // this.stompClient = Stomp.over(socket);

//     this.connect();
    
//   }

//   connect() {
//     if( this.stompClient===undefined || this.stompClient.status!=='CONNECTED'){
//       // const socket = new SockJS('http://localhost:8080/ws');
//       // this.stompClient = Stomp.over(socket);
//    return new Observable( (observe) =>{ this.stompClient.connect({}, (frame: string) => {
//       console.log('Connected: ' + frame);
//      this.status.status=UserStatus.ONLINE;
//     this.sendMessage(MessageMappings.STATUS_DESTI_UPDATE,this.status);
//        this.stompClient.subscribe(MessageMappings.STATUS_READ_DESTI,(message:{body:any})=>{
//            console.log(JSON.parse(message.body));
//            observe.next(true);
//        })
//     })
//   }
//    )

// }else
//    return new Observable(ob=> ob.next(false));
//   }

// subscribe(designation:String){
// return new Observable<any>( (observer)=>{
//   this.stompClient.subscribe(designation, (messages: { body: any; }) => {
//     // Handle incoming messages
//     console.log(messages);
//     this.messageReceived.next(JSON.parse(messages.body).body);
//     observer.next(JSON.parse(messages.body));
//   })
// });
// }

//   disConnect() {
//     this.status.status = UserStatus.OFFLINE;
//     this.sendMessage(MessageMappings.STATUS_DESTI_UPDATE,this.status);
//    return new Observable( (observe)=>{
//     try{
//        this.stompClient.disconnect();
//       observe.next(true) ;
//     }catch(err:any){
//       console.log(err);
//       observe.next(false);
//     }

//   })
//   }
//   sendMessageTo(message: any) {
//     this.stompClient.send('/app/chat', {}, JSON.stringify(message));
//   }
//   sendMessage(designation:string,message:any){
//   this.stompClient.send(designation,{},JSON.stringify(message));
//   }

//   searchUser(value:string){
//     this.stompClient.send("/app/search",{},JSON.stringify(value));
//   }

  
//   getSearchUsers() {
//     this.stompClient.connect({}, (frame: string) => {
//       console.log('Connected: ' + frame);
//       this.stompClient.subscribe('/topic/search', (messages: { body: any; }) => {
//         // Handle incoming messages
//         console.log(messages);
//         this.users.next(JSON.parse(messages.body));
//       });
//     });
//   }
}
