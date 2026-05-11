import { Component } from '@angular/core';



import { Router } from '@angular/router';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-registro-empresa',
  templateUrl: './registro-empresa.page.html',
  styleUrls: ['./registro-empresa.page.scss'],
  standalone: false,
  
  
})
export class RegistroEmpresaPage {
  form = { razon:'', rut:'', giro:'', contacto:'', email:'', telefono:'', web:'' };
  constructor(private router: Router, private navCtrl: NavController) {}
  goTo(p: string) { this.navCtrl.navigateRoot(p); }
  onSubmit() {}
}

