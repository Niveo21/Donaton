import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NavController, ToastController } from '@ionic/angular';
import { forkJoin } from 'rxjs';
import { AuthService } from '../../services/auth.service';

interface Acopio {
  id: number;
  nombre: string;
  direccion: string;
  comuna: string;
  region?: string;
  tipoEmergencia?: string;
  titulo?: string;
  descripcion?: string;
  urgente?: boolean;
}

interface Transporte {
  id: number;
  tipo: string;
  modelo: string;
  placa: string;
}

interface VoluntarioDTO {
  rut: string;
  nombre: string;
  email: string;
  rol: string;
  transporteId?: number | null;
  acopioId?: number | null;
}

@Component({
  selector: 'app-admin',
  templateUrl: './admin.page.html',
  styleUrls: ['./admin.page.scss'],
  standalone: false,
})
export class AdminPage implements OnInit {
  tab: 'acopio' | 'transporte' | 'voluntarios' = 'acopio';

  acopios: Acopio[] = [];
  transportes: Transporte[] = [];
  voluntarios: VoluntarioDTO[] = [];

  seleccionTransporte: Record<string, number | null> = {};
  seleccionAcopio: Record<string, number | null> = {};

  nuevoAcopio = {
    nombre: '', direccion: '', comuna: '', region: '',
    tipoEmergencia: 'OTRO', titulo: '', descripcion: '', urgente: false
  };
  nuevoTransporte = { tipo: '', modelo: '', placa: '' };

  tiposEmergencia = ['INCENDIO', 'INUNDACION', 'TERREMOTO', 'SEQUIA', 'OTRO'];

  private acopioUrl = 'http://localhost:8085/logistica/acopio';
  private transporteUrl = 'http://localhost:8085/logistica/transporte';
  private voluntarioUrl = 'http://localhost:8085/logistica/voluntario';

  constructor(
    private http: HttpClient,
    private navCtrl: NavController,
    private authService: AuthService,
    private toastCtrl: ToastController
  ) {}

  ngOnInit() {
    const usuario = this.authService.usuarioActual;
    if (!usuario || usuario.rol !== 'ADMIN') {
      this.goTo('/home');
      return;
    }
    this.cargarAcopios();
    this.cargarTransportes();
    this.cargarVoluntarios();
  }

  goTo(path: string) { this.navCtrl.navigateRoot(path); }

  setTab(t: 'acopio' | 'transporte' | 'voluntarios') { this.tab = t; }

  private async showToast(message: string, color: 'success' | 'danger') {
    const toast = await this.toastCtrl.create({
      message,
      duration: 3000,
      position: 'bottom',
      cssClass: `toast-bottom-right toast-${color}`,
    });
    await toast.present();
  }

  private cargarAcopios() {
    this.http.get<Acopio[]>(this.acopioUrl).subscribe({
      next: (data) => this.acopios = data,
      error: () => this.acopios = []
    });
  }

  private cargarTransportes() {
    this.http.get<Transporte[]>(this.transporteUrl).subscribe({
      next: (data) => this.transportes = data,
      error: () => this.transportes = []
    });
  }

  private cargarVoluntarios() {
    this.http.get<VoluntarioDTO[]>(this.voluntarioUrl).subscribe({
      next: (data) => {
        this.voluntarios = data;
        data.forEach(v => {
          this.seleccionTransporte[v.rut] = v.transporteId ?? null;
          this.seleccionAcopio[v.rut] = v.acopioId ?? null;
        });
      },
      error: () => this.voluntarios = []
    });
  }

  async asignar(v: VoluntarioDTO) {
    const transporteId = this.seleccionTransporte[v.rut];
    const acopioId = this.seleccionAcopio[v.rut];

    const peticiones = [];
    if (transporteId) {
      peticiones.push(this.http.put(`${this.voluntarioUrl}/${v.rut}/transporte/${transporteId}`, {}));
    }
    if (acopioId) {
      peticiones.push(this.http.put(`${this.voluntarioUrl}/${v.rut}/acopio/${acopioId}`, {}));
    }

    if (peticiones.length === 0) {
      await this.showToast('Selecciona al menos un transporte o acopio.', 'danger');
      return;
    }

    forkJoin(peticiones).subscribe({
      next: async () => {
        await this.showToast(`Asignación guardada para ${v.nombre}.`, 'success');
        this.cargarVoluntarios();
      },
      error: async () => await this.showToast('No se pudo guardar la asignación.', 'danger')
    });
  }

  private siguienteId(lista: { id: number }[]): number {
    return lista.length ? Math.max(...lista.map(x => x.id)) + 1 : 1;
  }

  async eliminarAcopio(id: number) {
    this.http.delete(`${this.acopioUrl}/${id}`, { responseType: 'text' }).subscribe({
      next: async () => {
        await this.showToast('Punto de acopio eliminado.', 'success');
        this.cargarAcopios();
      },
      error: async () => await this.showToast('No se pudo eliminar el punto de acopio.', 'danger')
    });
  }

  async eliminarTransporte(id: number) {
    this.http.delete(`${this.transporteUrl}/${id}`, { responseType: 'text' }).subscribe({
      next: async () => {
        await this.showToast('Transporte eliminado.', 'success');
        this.cargarTransportes();
      },
      error: async () => await this.showToast('No se pudo eliminar el transporte.', 'danger')
    });
  }

  async crearAcopio() {
    if (!this.nuevoAcopio.nombre.trim() || !this.nuevoAcopio.direccion.trim() || !this.nuevoAcopio.comuna.trim()) {
      await this.showToast('Completa nombre, dirección y comuna.', 'danger');
      return;
    }

    const payload = { id: this.siguienteId(this.acopios), ...this.nuevoAcopio };

    this.http.post(this.acopioUrl, payload, { responseType: 'text' }).subscribe({
      next: async (respuesta) => {
        if (respuesta.includes('correctamente')) {
          await this.showToast('Punto de acopio creado correctamente.', 'success');
          this.nuevoAcopio = { nombre: '', direccion: '', comuna: '', region: '', tipoEmergencia: 'OTRO', titulo: '', descripcion: '', urgente: false };
          this.cargarAcopios();
        } else {
          await this.showToast(respuesta, 'danger');
        }
      },
      error: async () => await this.showToast('No se pudo crear el punto de acopio.', 'danger')
    });
  }

  async crearTransporte() {
    if (!this.nuevoTransporte.tipo.trim() || !this.nuevoTransporte.modelo.trim() || !this.nuevoTransporte.placa.trim()) {
      await this.showToast('Completa tipo, modelo y placa.', 'danger');
      return;
    }

    const payload = { id: this.siguienteId(this.transportes), ...this.nuevoTransporte };

    this.http.post(this.transporteUrl, payload, { responseType: 'text' }).subscribe({
      next: async (respuesta) => {
        if (respuesta.includes('correctamente')) {
          await this.showToast('Transporte creado correctamente.', 'success');
          this.nuevoTransporte = { tipo: '', modelo: '', placa: '' };
          this.cargarTransportes();
        } else {
          await this.showToast(respuesta, 'danger');
        }
      },
      error: async () => await this.showToast('No se pudo crear el transporte.', 'danger')
    });
  }
}
