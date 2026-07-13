import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NavController, ToastController } from '@ionic/angular';

@Component({
  selector: 'app-contacto',
  templateUrl: './contacto.page.html',
  styleUrls: ['./contacto.page.scss'],
  standalone: false,
})
export class ContactoPage {
  form = { nombre: '', email: '', asunto: '', mensaje: '' };
  enviando = false;

  private apiUrl = 'http://localhost:8085/contacto';

  constructor(
    private navCtrl: NavController,
    private http: HttpClient,
    private toastCtrl: ToastController
  ) {}

  goTo(p: string) { this.navCtrl.navigateRoot(p); }

  private emailValido(email: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim());
  }

  private async showToast(message: string, color: 'success' | 'danger') {
    const toast = await this.toastCtrl.create({
      message,
      duration: 3500,
      position: 'bottom',
      cssClass: `toast-bottom-right toast-${color}`,
    });
    await toast.present();
  }

  async onSubmit() {
    if (!this.form.nombre.trim()) {
      await this.showToast('El nombre es obligatorio.', 'danger');
      return;
    }
    if (!this.emailValido(this.form.email)) {
      await this.showToast('Ingresa un correo válido.', 'danger');
      return;
    }
    if (!this.form.mensaje.trim()) {
      await this.showToast('Escribe un mensaje.', 'danger');
      return;
    }

    this.enviando = true;
    this.http.post<{ mensaje: string }>(this.apiUrl, this.form).subscribe({
      next: async () => {
        this.enviando = false;
        await this.showToast('¡Mensaje enviado! Te responderemos pronto.', 'success');
        this.form = { nombre: '', email: '', asunto: '', mensaje: '' };
      },
      error: async () => {
        this.enviando = false;
        await this.showToast('No se pudo enviar el mensaje. Intenta nuevamente.', 'danger');
      }
    });
  }
}
