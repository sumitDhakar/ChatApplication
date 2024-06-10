import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserHomeComponent } from './user/user-home/user-home.component';
import { UserDashboardComponent } from './user/user-dashboard/user-dashboard.component';
import { ChatSideComponent } from './user/bars/chat-side/chat-side.component';
import { ProfileComponent } from './user/profile/profile.component';
import { ChatsComponent } from './user/chats/chats.component';
import { GroupsComponent } from './user/groups/groups.component';
import { ContactsComponent } from './user/contacts/contacts.component';
import { SettingsComponent } from './user/settings/settings.component';
import { ConversationComponent } from './user/conversation/conversation.component';
import { LoginComponent } from './user/authentication/login/login.component';
import { SignupComponent } from './user/authentication/signup/signup.component';
import { ForgetPasswordComponent } from './user/authentication/forget-password/forget-password.component';
import { OtpVerificationComponent } from './user/authentication/otp-verification/otp-verification.component';
import { NotificationsComponent } from './user/notifications/notifications.component';
import { InvitaionsComponent } from './user/invitaions/invitaions.component';
import { GroupConversationComponent } from './user/group-conversation/group-conversation.component';

const routes: Routes = [

{
  path:'',component:LoginComponent,pathMatch:'full'
},
{
  path:'signup',component:SignupComponent
},
{
  path:'otp',component:OtpVerificationComponent
},
{
  path:'forget-password',component:ForgetPasswordComponent
},

  {
    path: 'gapshap', component: UserHomeComponent,
    children: [
      // {
      //   path: '', component: UserDashboardComponent
      // },
      {
        path: 'profile', component: ProfileComponent, outlet: 'chatside'
      },
      {
        path: '', component: ChatsComponent, outlet: 'chatside'
      },
      {
        path: '', component: ChatSideComponent, outlet: 'convermain'
      },
      {
        path:'notifications',component:NotificationsComponent,outlet:'chatside'
      },
      {
        path:'invitations',component:InvitaionsComponent,outlet:'chatside'
      },
      {
        path: 'groups', component: GroupsComponent, outlet: 'chatside'
      },
      {
        path: 'contacts', component: ContactsComponent, outlet: 'chatside'
      },
      {
        path:'settings',component:SettingsComponent,outlet:'chatside'
      },
      {
        path:'conversation/:id',component:ConversationComponent,outlet:'convermain'
      },
      {
        path:'group-conversation/:id',component:GroupConversationComponent,outlet:'convermain'
      }
    ]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
