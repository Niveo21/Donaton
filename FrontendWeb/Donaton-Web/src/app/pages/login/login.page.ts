import { Component } from '@angular/core';
import { NavController, ToastController } from '@ionic/angular';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: false,
})
export class LoginPage {
  email = '';
  password = '';

  private apiUrl = 'http://localhost:8085/usuario/login';

  constructor(
    private navCtrl: NavController,
    private http: HttpClient,
    private toastCtrl: ToastController,
    private authService: AuthService
  ) {}

  goTo(p: string) {
    this.navCtrl.navigateRoot(p);
  }

  private async showToast(message: string, color: 'success' | 'danger') {
    const toast = await this.toastCtrl.create({
      message,
      duration: 3000,
      position: 'bottom',
      cssClass: `toast-bottom-right toast-${color}`,
    });
    await toast.present();
  }

  async onSubmit() {
    if (this.email.trim() === '' || this.password.trim() === '') {
      await this.showToast('Por favor, complete todos los campos.', 'danger');
      return;
    }

    const credentials = {
      email: this.email,
      password: this.password
    };

    this.http.post<any>(this.apiUrl, credentials).subscribe({
      next: async (response) => {
        this.authService.login(response);
        await this.showToast('¡Inicio de sesión exitoso!', 'success');
        this.goTo('/home');
      },
      error: async (error) => {
        if (error.status === 401) {
          await this.showToast('Correo o contraseña incorrectos.', 'danger');
        } else {
          await this.showToast('Error al conectar con el servidor. Intente más tarde.', 'danger');
        }
      }
    });
  }
}
