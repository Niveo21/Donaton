import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { RouterModule, Routes } from '@angular/router';
import { RegistroEmpresaPage } from './registro-empresa.page';
import { SharedModule } from '../../shared/shared.module';

const routes: Routes = [{ path: '', component: RegistroEmpresaPage }];

@NgModule({
  imports: [CommonModule, FormsModule, IonicModule, RouterModule.forChild(routes), SharedModule],
  declarations: [RegistroEmpresaPage]
})
export class RegistroEmpresaPageModule {}
