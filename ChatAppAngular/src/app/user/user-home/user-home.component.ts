import { Component, OnInit } from '@angular/core';
import { WebSocketService } from '../services/webSocket/web-socket.service';
import { MessageMappings } from 'src/app/Config/message-mappings';
import { ChatService } from '../services/chat.service';

@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.component.html',
  styleUrls: ['./user-home.component.scss']
})
export class UserHomeComponent implements OnInit{

  constructor(private webSocketService:WebSocketService,private chatService:ChatService){}
  ngOnInit(): void {
    
  //  this.webSocketService.connect().subscribe((data)=>{
  //   console.log(data+"---------");
    
  //  });
  this.chatService.connect();

  }


}
