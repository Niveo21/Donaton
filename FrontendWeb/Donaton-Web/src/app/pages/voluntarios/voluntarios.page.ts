import { Component } from '@angular/core';
import { NavController, ToastController } from '@ionic/angular';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-voluntarios',
  templateUrl: './voluntarios.page.html',
  styleUrls: ['./voluntarios.page.scss'],
  standalone: false,
})
export class VoluntariosPage {
  areas = ['Rescate y primera respuesta','Logística y bodega','Apoyo psicosocial','Salud y primeros auxilios','Comunicaciones','Cocina y alimentación'];
  regiones = [
    'Arica y Parinacota', 'Tarapacá', 'Antofagasta', 'Atacama', 'Coquimbo',
    'Valparaíso', 'Metropolitana de Santiago', "Libertador General Bernardo O'Higgins",
    'Maule', 'Ñuble', 'Biobío', 'La Araucanía', 'Los Ríos', 'Los Lagos',
    'Aysén del General Carlos Ibáñez del Campo', 'Magallanes y de la Antártica Chilena',
  ];
  selectedAreas: { [key: string]: boolean } = {};
  form = { nombre:'', apellido:'', rut:'', edad:'', email:'', telefono:'', region:'', experiencia:'', password:'', terms:false };
  stats = [
    { icon:'👥', label:'Voluntarios activos', value:'3.200+' },
    { icon:'📍', label:'Regiones cubiertas', value:'16' },
    { icon:'⏱️', label:'Horas donadas en 2025', value:'82.000' },
  ];

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
    const limpio = rut.replace(/\./g, '').trim();
    return /^\d{7,8}-[\dkK]$/.test(limpio);
  }

  private emailValido(email: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim());
  }

  async onSubmit() {
    if (!this.form.nombre.trim()) {
      await this.showToast('El nombre es obligatorio.', 'danger');
      return;
    }

    if (!this.rutValido(this.form.rut)) {
      await this.showToast('El RUT no es válido. Formato esperado: 12345678-9', 'danger');
      return;
    }

    if (+this.form.edad < 18) {
      await this.showToast('La edad mínima para ser voluntario es 18 años.', 'danger');
      return;
    }

    if (!this.emailValido(this.form.email)) {
      await this.showToast('El correo electrónico no es válido.', 'danger');
      return;
    }

    if (!this.form.region.trim()) {
      await this.showToast('La región es obligatoria.', 'danger');
      return;
    }

    if (this.form.password.length < 6) {
      await this.showToast('La contraseña debe tener al menos 6 caracteres.', 'danger');
      return;
    }

    if (!this.form.terms) {
      await this.showToast('Debes aceptar los términos para continuar.', 'danger');
      return;
    }

    const data = {
      nombre: `${this.form.nombre} ${this.form.apellido}`.trim(),
      rut: this.form.rut,
      email: this.form.email,
      password: this.form.password,
      rol: 'VOLUNTARIO',
      telefono: this.form.telefono,
      edad: +this.form.edad,
      region: this.form.region,
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
