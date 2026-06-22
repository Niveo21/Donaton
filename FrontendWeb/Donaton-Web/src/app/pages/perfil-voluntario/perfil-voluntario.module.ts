import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { RouterModule, Routes } from '@angular/router';
import { PerfilVoluntarioPage } from './perfil-voluntario.page';
import { SharedModule } from '../../shared/shared.module';

const routes: Routes = [{ path: '', component: PerfilVoluntarioPage }];

@NgModule({
  imports: [CommonModule, FormsModule, IonicModule, RouterModule.forChild(routes), SharedModule],
  declarations: [PerfilVoluntarioPage]
})
export class PerfilVoluntarioPageModule {}
