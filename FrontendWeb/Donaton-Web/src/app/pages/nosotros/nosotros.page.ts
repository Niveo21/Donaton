import { Component } from '@angular/core';


import { Router } from '@angular/router';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-nosotros',
  templateUrl: './nosotros.page.html',
  styleUrls: ['./nosotros.page.scss'],
  standalone: false,
  
  
})
export class NosotrosPage {
  stats = [
    { n:'15', l:'Años en terreno' },
    { n:'$8.500M', l:'Recaudados históricamente' },
    { n:'120K+', l:'Familias apoyadas' },
  ];
  constructor(private router: Router, private navCtrl: NavController) {}
  goTo(p: string) { this.navCtrl.navigateRoot(p); }
}

