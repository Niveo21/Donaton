import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { RouterModule, Routes } from '@angular/router';
import { VoluntariosPage } from './voluntarios.page';
import { SharedModule } from '../../shared/shared.module';

const routes: Routes = [{ path: '', component: VoluntariosPage }];

@NgModule({
  imports: [CommonModule, FormsModule, IonicModule, RouterModule.forChild(routes), SharedModule],
  declarations: [VoluntariosPage]
})
export class VoluntariosPageModule {}
