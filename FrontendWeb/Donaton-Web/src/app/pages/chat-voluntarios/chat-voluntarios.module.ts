import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { RouterModule, Routes } from '@angular/router';
import { ChatVoluntariosPage } from './chat-voluntarios.page';
import { SharedModule } from '../../shared/shared.module';

const routes: Routes = [{ path: '', component: ChatVoluntariosPage }];

@NgModule({
  imports: [CommonModule, FormsModule, IonicModule, RouterModule.forChild(routes), SharedModule],
  declarations: [ChatVoluntariosPage]
})
export class ChatVoluntariosPageModule {}
