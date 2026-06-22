import { Component, Input } from '@angular/core';
import { NavController } from '@ionic/angular';
import { AuthService, Usuario } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  standalone: false,
})
export class HeaderComponent {
  @Input() logoIcon = 'D';
  @Input() logoPrefix = 'Chile';
  @Input() logoText = 'Donaton';

  menuAbierto = false;
  popoverEvent: Event | undefined;

  constructor(private navCtrl: NavController, private authService: AuthService) {}

  get usuario$() {
    return this.authService.usuario$;
  }

  goTo(p: string) {
    this.menuAbierto = false;
    this.navCtrl.navigateRoot(p);
  }

  toggleMenu(ev: Event) {
    this.popoverEvent = ev;
    this.menuAbierto = !this.menuAbierto;
  }

  cerrarSesion() {
    this.menuAbierto = false;
    this.authService.logout();
    this.navCtrl.navigateRoot('/home');
  }
}
