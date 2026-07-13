import { Component } from '@angular/core';
import { NavController, ToastController } from '@ionic/angular';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-registro-donante',
  templateUrl: './registro-donante.page.html',
  styleUrls: ['./registro-donante.page.scss'],
  standalone: false,
})
export class RegistroDonantePage {
  form = { nombre: '', rut: '', email: '', password: '', confirmar: '' };

  private apiUrl = `${environment.apiUrl}/usuario/registro`;

  constructor(
    private navCtrl: NavController,
    private http: HttpClient,
    private toastCtrl: ToastController
  ) {}

  goTo(p: string) { this.navCtrl.navigateRoot(p); }

  private async showToast(message: string, color: 'success' | 'danger') {
    const toast = await this.toastCtrl.create({
      message,
      duration: 3000,
      position: 'bottom',
      cssClass: `toast-bottom-right toast-${color}`,
    });
    await toast.present();
  }

  private rutValido(rut: string): boolean {
    return /^\d{7,8}-[\dkK]$/.test(rut.trim());
  }

  private emailValido(email: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim());
  }

  async onSubmit() {
    if (!this.rutValido(this.form.rut)) {
      await this.showToast('El RUT no es válido. Formato esperado: 12345678-9', 'danger');
      return;
    }

    if (!this.emailValido(this.form.email)) {
      await this.showToast('El correo electrónico no es válido.', 'danger');
      return;
    }

    if (this.form.password !== this.form.confirmar) {
      await this.showToast('Las contraseñas no coinciden.', 'danger');
      return;
    }

    const data = {
      nombre: this.form.nombre,
      rut: this.form.rut,
      email: this.form.email,
      password: this.form.password,
      rol: 'DONANTE'
    };

    this.http.post(this.apiUrl, data, { responseType: 'text' }).subscribe({
      next: async (response) => {
        const texto = response.replace(/"/g, '').trim();
        if (texto === 'Registro almacenado correctamente') {
          await this.showToast('¡Registro exitoso! Bienvenido a Donaton.', 'success');
          setTimeout(() => this.goTo('/login'), 2000);
        } else {
          await this.showToast(texto, 'danger');
        }
      },
      error: async () => {
        await this.showToast('Error al registrar. Intenta nuevamente.', 'danger');
      }
    });
  }
}

