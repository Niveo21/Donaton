import { Component } from '@angular/core';



import { Router } from '@angular/router';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-contacto',
  templateUrl: './contacto.page.html',
  styleUrls: ['./contacto.page.scss'],
  standalone: false,
  
  
})
export class ContactoPage {
  form = { nombre:'', email:'', asunto:'', mensaje:'' };
  constructor(private router: Router, private navCtrl: NavController) {}
  goTo(p: string) { this.navCtrl.navigateRoot(p); }
  onSubmit() {}
}

