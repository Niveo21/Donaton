import { Component } from '@angular/core';



import { Router } from '@angular/router';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-voluntarios',
  templateUrl: './voluntarios.page.html',
  styleUrls: ['./voluntarios.page.scss'],
  standalone: false,
  
  
})
export class VoluntariosPage {
  areas = ['Rescate y primera respuesta','Logística y bodega','Apoyo psicosocial','Salud y primeros auxilios','Comunicaciones','Cocina y alimentación'];
  selectedAreas: { [key: string]: boolean } = {};
  form = { nombre:'', apellido:'', rut:'', edad:'', email:'', telefono:'', region:'', experiencia:'', terms:false };
  stats = [
    { icon:'👥', label:'Voluntarios activos', value:'3.200+' },
    { icon:'📍', label:'Regiones cubiertas', value:'16' },
    { icon:'⏱️', label:'Horas donadas en 2025', value:'82.000' },
  ];
  constructor(private router: Router, private navCtrl: NavController) {}
  goTo(p: string) { this.navCtrl.navigateRoot(p); }
  onSubmit() {}
}

