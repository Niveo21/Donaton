import { Component } from '@angular/core';
import { NavController, ToastController } from '@ionic/angular';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-registro-empresa',
  templateUrl: './registro-empresa.page.html',
  styleUrls: ['./registro-empresa.page.scss'],
  standalone: false,
})
export class RegistroEmpresaPage {
  form = { razon: '', rut: '', giro: '', contacto: '', email: '', telefono: '', password: '' };

  private apiUrl = 'http://localhost:8085/usuario/registro';

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
    const limpio = rut.replace(/\./g, '').trim();
    return /^\d{7,8}-[\dkK]$/.test(limpio);
  }

  private emailValido(email: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim());
  }

  async onSubmit() {
    if (!this.form.razon.trim()) {
      await this.showToast('La razón social es obligatoria.', 'danger');
      return;
    }

    if (!this.rutValido(this.form.rut)) {
      await this.showToast('El RUT no es válido. Formato esperado: 76123456-7', 'danger');
      return;
    }

    if (!this.form.giro.trim()) {
      await this.showToast('El giro comercial es obligatorio.', 'danger');
      return;
    }

    if (!this.emailValido(this.form.email)) {
      await this.showToast('El correo electrónico no es válido.', 'danger');
      return;
    }

    if (!this.form.telefono.trim()) {
      await this.showToast('El teléfono es obligatorio.', 'danger');
      return;
    }

    if (this.form.password.length < 6) {
      await this.showToast('La contraseña debe tener al menos 6 caracteres.', 'danger');
      return;
    }

    const data = {
      nombre: this.form.contacto,
      rut: this.form.rut,
      email: this.form.email,
      password: this.form.password,
      rol: 'EMPRESA',
      telefono: this.form.telefono,
      giro: this.form.giro,
      razonSocial: this.form.razon,
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
