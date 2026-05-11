import { Component } from '@angular/core';



import { Router } from '@angular/router';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-emergencias',
  templateUrl: './emergencias.page.html',
  styleUrls: ['./emergencias.page.scss'],
  standalone: false,
  
  
})
export class EmergenciasPage {
  emergencies = [
    { img:'assets/emergency-fire.jpg', icon:'🔥', region:'Valparaíso', title:'Incendios Forestales', desc:'Miles de familias damnificadas necesitan refugio, agua y kits de emergencia urgente.', raised:184500000, goal:300000000 },
    { img:'assets/emergency-earthquake.jpg', icon:'⛰️', region:'Coquimbo', title:'Reconstrucción Post-Terremoto', desc:'Reconstruyamos viviendas y escuelas en las zonas más afectadas del norte chico.', raised:92000000, goal:250000000 },
    { img:'assets/emergency-flood.jpg', icon:'🌊', region:'Biobío y Ñuble', title:'Inundaciones del Sur', desc:'Apoyo a comunidades aisladas por crecidas de ríos y lluvias torrenciales.', raised:56700000, goal:180000000 },
  ];
  constructor(private router: Router, private navCtrl: NavController) {
    console.log("EmergenciasPage Loaded!");
  }
  goTo(p: string) { this.navCtrl.navigateRoot(p); }
  getPercent(e: any) { return Math.round((e.raised / e.goal) * 100); }
  formatCLP(n: number) { return new Intl.NumberFormat('es-CL', { style:'currency', currency:'CLP', maximumFractionDigits:0 }).format(n); }
}

