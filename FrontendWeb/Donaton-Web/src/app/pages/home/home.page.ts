import { Component } from '@angular/core';



import { Router } from '@angular/router';
import { NavController } from '@ionic/angular';

interface Emergency { img:string; icon:string; region:string; title:string; desc:string; raised:number; goal:number; }

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.scss'],
  standalone: false,
  
  
})
export class HomePage {
  presets = [5000, 10000, 25000, 50000];
  amount = 10000;
  currentYear = new Date().getFullYear();

  emergencies: Emergency[] = [
    { img: 'assets/emergency-fire.jpg', icon: '🔥', region: 'Valparaíso', title: 'Incendios Forestales', desc: 'Miles de familias damnificadas necesitan refugio, agua y kits de emergencia urgente.', raised: 184500000, goal: 300000000 },
    { img: 'assets/emergency-earthquake.jpg', icon: '⛰️', region: 'Coquimbo', title: 'Reconstrucción Post-Terremoto', desc: 'Reconstruyamos viviendas y escuelas en las zonas más afectadas del norte chico.', raised: 92000000, goal: 250000000 },
    { img: 'assets/emergency-flood.jpg', icon: '🌊', region: 'Biobío y Ñuble', title: 'Inundaciones del Sur', desc: 'Apoyo a comunidades aisladas por crecidas de ríos y lluvias torrenciales.', raised: 56700000, goal: 180000000 },
  ];

  constructor(private router: Router, private navCtrl: NavController) {}

  getPercent(e: Emergency) { return Math.round((e.raised / e.goal) * 100); }
  formatCLP(n: number) { return new Intl.NumberFormat('es-CL', { style: 'currency', currency: 'CLP', maximumFractionDigits: 0 }).format(n); }
  get donationImpact() {
    if (this.amount >= 50000) return 'Kits completos de emergencia para 4 familias.';
    if (this.amount >= 25000) return 'Agua potable para una familia por 2 semanas.';
    if (this.amount >= 10000) return 'Alimentos esenciales para 3 días.';
    return 'Frazadas térmicas para una persona.';
  }
  setAmount(p: number) { this.amount = p; }
  goTo(path: string) { this.navCtrl.navigateRoot(path); }
  scrollTo(id: string) { const el = document.getElementById(id); if (el) el.scrollIntoView({ behavior: 'smooth' }); }
}

