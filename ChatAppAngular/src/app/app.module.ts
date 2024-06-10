import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http'
import { ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserHomeComponent } from './user/user-home/user-home.component';
import { UserDashboardComponent } from './user/user-dashboard/user-dashboard.component';
import { SidebarComponent } from './user/bars/sidebar/sidebar.component';
import { ProfileComponent } from './user/profile/profile.component';
import { ChatSideComponent } from './user/bars/chat-side/chat-side.component';
import { ChatsComponent } from './user/chats/chats.component';
import { GroupsComponent } from './user/groups/groups.component';
import { ContactsComponent } from './user/contacts/contacts.component';
import { SettingsComponent } from './user/settings/settings.component';
import { ConversationComponent } from './user/conversation/conversation.component';
import { LoginComponent } from './user/authentication/login/login.component';
import { SignupComponent } from './user/authentication/signup/signup.component';
import { ForgetPasswordComponent } from './user/authentication/forget-password/forget-password.component';
import { OtpVerificationComponent } from './user/authentication/otp-verification/otp-verification.component';
import { AuthInterceptor } from './materials/authentication.interceptor';
import { FormsModule } from '@angular/forms';
import { NotificationsComponent } from './user/notifications/notifications.component';
import { InvitaionsComponent } from './user/invitaions/invitaions.component';
import { TimeAgoPipe } from './utils/custompipes/time-ago.pipe';
import { DatePipe } from '@angular/common';
import { GroupConversationComponent } from './user/group-conversation/group-conversation.component';
import { FileSizePipe } from './utils/custompipes/file-size.pipe';

@NgModule({
  declarations: [
    AppComponent,
    UserHomeComponent,
    UserDashboardComponent,
    SidebarComponent,
    ProfileComponent,
    ChatSideComponent,
    ChatsComponent,
    GroupsComponent,
    ContactsComponent,
    SettingsComponent,
    ConversationComponent,
    LoginComponent,
    SignupComponent,
    ForgetPasswordComponent,
    OtpVerificationComponent,
    NotificationsComponent,
    InvitaionsComponent,
    FileSizePipe,
    TimeAgoPipe,
    GroupConversationComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [AuthInterceptor,DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
