import { Component } from '@angular/core';



import { Router } from '@angular/router';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.page.html',
  styleUrls: ['./registro.page.scss'],
  standalone: false,
  
  
})
export class RegistroPage {
  roles = [
    { title: 'Donante', desc: 'Aporta económicamente a las campañas activas y recibe tu certificado de donación.', icon: '❤️', gradient: 'bg-gradient-accent', path: '/registro/donante' },
    { title: 'Empresa', desc: 'Suma a tu organización con donaciones corporativas, RSE y alianzas estratégicas.', icon: '🏢', gradient: 'bg-gradient-primary', path: '/registro/empresa' },
    { title: 'Voluntario', desc: 'Dona tu tiempo y habilidades en terreno cuando más se necesita en Chile.', icon: '🤲', gradient: 'bg-gradient-hero', path: '/voluntarios' },
  ];
  constructor(private router: Router, private navCtrl: NavController) {}
  goTo(p: string) { this.navCtrl.navigateRoot(p); }
}

