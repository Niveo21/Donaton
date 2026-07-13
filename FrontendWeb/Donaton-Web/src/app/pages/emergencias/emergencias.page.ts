import { Component, OnInit, OnDestroy, NgZone } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NavController, ToastController } from '@ionic/angular';
import { iconoDeEmergencia, imagenDeEmergencia } from '../../shared/emergencia-utils';
import { AuthService } from '../../services/auth.service';
import { environment } from '../../../environments/environment';
import * as L from 'leaflet';

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
  latitud?: number;
  longitud?: number;
}

interface VoluntarioDTO {
  rut: string;
  nombre: string;
  email: string;
  rol: string;
}

interface InventarioItem {
  id: number;
  recurso: string;
  stockActual: number;
  estado: string;
  acopioId?: number;
}

@Component({
  selector: 'app-emergencias',
  templateUrl: './emergencias.page.html',
  styleUrls: ['./emergencias.page.scss'],
  standalone: false,
})
export class EmergenciasPage implements OnInit, OnDestroy {
  emergencies: Acopio[] = [];
  puntoSeleccionado: Acopio | null = null;
  busqueda = '';

  // Posición en píxeles donde mostrar las burbujas sobre el mapa
  bubblePosicion: { x: number; y: number } | null = null;

  // Modal de necesidad
  modalNecesidad = false;
  tipoSeleccionado = '';
  necesidadForm = { descripcion: '', urgente: false };

  // Modal de detalles (voluntarios + inventario del punto)
  modalDetalles = false;
  cargandoDetalles = false;
  voluntariosAcopio: VoluntarioDTO[] = [];
  inventarioAcopio: InventarioItem[] = [];
  nuevoItemInventario = { recurso: '', cantidad: 0 };

  // Edición inline de un item de inventario (solo admin)
  editandoItemId: number | null = null;
  edicionItem = { recurso: '', cantidad: 0 };

  private map: L.Map | null = null;
  private marcadores: Map<number, L.Marker> = new Map();
  private marcadorActivo: L.Marker | null = null;

  private acopioUrl     = `${environment.apiUrl}/logistica/acopio`;
  private necesidadUrl  = `${environment.apiUrl}/necesidad/almacenar`;
  private voluntarioUrl = `${environment.apiUrl}/logistica/voluntario`;
  private inventarioUrl = `${environment.apiUrl}/logistica/inventario`;

  constructor(
    private http: HttpClient,
    private navCtrl: NavController,
    private authService: AuthService,
    private toastCtrl: ToastController,
    private zone: NgZone
  ) {}

  ngOnInit() {
    this.http.get<Acopio[]>(this.acopioUrl).subscribe({
      next: (puntos) => { this.emergencies = puntos; this.renderizarPins(); },
      error: () => this.emergencies = []
    });
    setTimeout(() => this.initMap(), 100);
  }

  ngOnDestroy() {
    if (this.map) { this.map.remove(); this.map = null; }
  }

  get esVoluntario(): boolean {
    return this.authService.usuarioActual?.rol === 'VOLUNTARIO';
  }

  get esAdmin(): boolean {
    return this.authService.usuarioActual?.rol === 'ADMIN';
  }

  get puedeVerDetalles(): boolean {
    return this.esVoluntario || this.esAdmin;
  }

  get emergenciesFiltradas(): Acopio[] {
    const q = this.busqueda.trim().toLowerCase();
    if (!q) return this.emergencies;
    return this.emergencies.filter(e =>
      (e.titulo || e.nombre)?.toLowerCase().includes(q) ||
      e.comuna?.toLowerCase().includes(q) ||
      e.direccion?.toLowerCase().includes(q) ||
      e.region?.toLowerCase().includes(q)
    );
  }

  onBusquedaChange() {
    // Si el punto seleccionado ya no aparece en el filtro, cerramos su tarjeta
    if (this.puntoSeleccionado && !this.emergenciesFiltradas.some(e => e.id === this.puntoSeleccionado!.id)) {
      this.deseleccionar();
    }
    this.renderizarPins();
  }

  // ── Mapa ───────────────────────────────────────────────────────────────────

  private initMap() {
    const contenedor = document.getElementById('emergencias-map');
    if (!contenedor || this.map) return;

    this.map = L.map('emergencias-map').setView([-35.5, -71.5], 5);

    L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
      attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> © <a href="https://carto.com/attributions">CARTO</a>',
      maxZoom: 18
    }).addTo(this.map);

    // Al mover el mapa, actualizar posición de las burbujas en tiempo real
    this.map.on('move', () => {
      this.zone.run(() => this.actualizarPosBurbujas());
    });

    // Clic en fondo del mapa → deseleccionar pin
    this.map.on('click', () => {
      this.zone.run(() => this.deseleccionar());
    });

    this.renderizarPins();
  }

  private crearIcono(seleccionado = false, urgente = false): L.DivIcon {
    const fondo    = seleccionado ? 'white'    : (urgente ? '#dc2626' : '#1e2033');
    const puntoDot = seleccionado ? '#E05A2B'  : 'white';
    return L.divIcon({
      className: '',
      html: `
        <div style="width:36px; height:36px; display:flex; align-items:center; justify-content:center;">
          <div style="
            width: 26px;
            height: 26px;
            background: ${fondo};
            border: 2px solid #E05A2B;
            border-radius: 50% 50% 50% 0;
            transform: rotate(-45deg);
            box-shadow: 0 2px 8px rgba(0,0,0,0.5);
            display: flex;
            align-items: center;
            justify-content: center;
          ">
            <div style="
              width: 8px;
              height: 8px;
              background: ${puntoDot};
              border-radius: 50%;
              transform: rotate(45deg);
            "></div>
          </div>
        </div>`,
      iconSize: [36, 36],
      iconAnchor: [18, 36],
      popupAnchor: [0, -36]
    });
  }

  private renderizarPins() {
    if (!this.map) return;
    this.marcadores.forEach(m => m.remove());
    this.marcadores.clear();

    this.emergenciesFiltradas.forEach(a => {
      if (a.latitud == null || a.longitud == null) return;
      const marker = L.marker([a.latitud, a.longitud], { icon: this.crearIcono(false, a.urgente) })
        .addTo(this.map!);

      marker.on('click', (e: L.LeafletMouseEvent) => {
        L.DomEvent.stopPropagation(e); // evita que el clic llegue al mapa
        this.zone.run(() => this.seleccionarPin(a, marker));
      });

      this.marcadores.set(a.id, marker);
    });
  }

  private seleccionarPin(acopio: Acopio, marker: L.Marker) {
    // Resetear pin anterior
    if (this.marcadorActivo && this.puntoSeleccionado) {
      this.marcadorActivo.setIcon(this.crearIcono(false, this.puntoSeleccionado.urgente));
    }
    // Activar pin nuevo
    marker.setIcon(this.crearIcono(true, acopio.urgente));
    this.marcadorActivo = marker;
    this.puntoSeleccionado = acopio;
    this.actualizarPosBurbujas();
  }

  private actualizarPosBurbujas() {
    if (!this.map || !this.puntoSeleccionado?.latitud || !this.puntoSeleccionado?.longitud) return;
    const pos = this.map.latLngToContainerPoint([
      this.puntoSeleccionado.latitud,
      this.puntoSeleccionado.longitud
    ]);
    this.bubblePosicion = { x: pos.x, y: pos.y };
  }

  private deseleccionar() {
    if (this.marcadorActivo && this.puntoSeleccionado) {
      this.marcadorActivo.setIcon(this.crearIcono(false, this.puntoSeleccionado.urgente));
    }
    this.marcadorActivo = null;
    this.puntoSeleccionado = null;
    this.bubblePosicion = null;
  }

  cerrarSeleccion() {
    if (this.modalDetalles) this.cerrarModalDetalles();
    this.deseleccionar();
  }

  irAPin(acopio: Acopio) {
    if (!this.map || acopio.latitud == null || acopio.longitud == null) return;
    const marker = this.marcadores.get(acopio.id);
    if (marker) this.seleccionarPin(acopio, marker);

    this.bubblePosicion = null; // ocultar durante la animación
    this.map.flyTo([acopio.latitud, acopio.longitud], 14, { duration: 1 });
    this.map.once('moveend', () => {
      this.zone.run(() => this.actualizarPosBurbujas());
    });
  }

  // ── Modal de necesidad ─────────────────────────────────────────────────────

  seleccionarBurbuja(tipo: string) {
    this.tipoSeleccionado = tipo;
    this.necesidadForm = { descripcion: '', urgente: false };
    this.modalNecesidad = true;
  }

  cerrarModalNecesidad() {
    this.modalNecesidad = false;
    this.tipoSeleccionado = '';
  }

  async enviarNecesidad() {
    if (!this.puntoSeleccionado || !this.tipoSeleccionado) return;
    const payload = {
      id: Date.now(),
      acopioId: this.puntoSeleccionado.id,
      acopioNombre: this.puntoSeleccionado.titulo || this.puntoSeleccionado.nombre,
      tipo: this.tipoSeleccionado,
      descripcion: this.necesidadForm.descripcion,
      urgente: this.necesidadForm.urgente
    };
    this.http.post(this.necesidadUrl, payload, { responseType: 'text' }).subscribe({
      next: async () => {
        this.cerrarModalNecesidad();
        await this.showToast('Necesidad reportada. El administrador ha sido notificado.', 'success');
      },
      error: async () => await this.showToast('No se pudo enviar la necesidad.', 'danger')
    });
  }

  // ── Modal de detalles ──────────────────────────────────────────────────────

  abrirDetalles() {
    if (!this.puntoSeleccionado) return;
    this.modalDetalles = true;
    this.cargarDetalles();
  }

  cerrarModalDetalles() {
    this.modalDetalles = false;
    this.voluntariosAcopio = [];
    this.inventarioAcopio = [];
    this.nuevoItemInventario = { recurso: '', cantidad: 0 };
    this.editandoItemId = null;
  }

  private cargarDetalles() {
    if (!this.puntoSeleccionado) return;
    const acopioId = this.puntoSeleccionado.id;
    this.cargandoDetalles = true;

    this.http.get<VoluntarioDTO[]>(`${this.voluntarioUrl}/acopio/${acopioId}`).subscribe({
      next: (data) => this.voluntariosAcopio = data,
      error: () => this.voluntariosAcopio = []
    });

    this.http.get<InventarioItem[]>(`${this.inventarioUrl}/acopio/${acopioId}`).subscribe({
      next: (data) => { this.inventarioAcopio = data; this.cargandoDetalles = false; },
      error: () => { this.inventarioAcopio = []; this.cargandoDetalles = false; }
    });
  }

  async agregarItemInventario() {
    if (!this.puntoSeleccionado) return;
    if (!this.nuevoItemInventario.recurso.trim() || this.nuevoItemInventario.cantidad <= 0) {
      await this.showToast('Ingresa un recurso y una cantidad válida.', 'danger');
      return;
    }

    const payload = {
      recurso: this.nuevoItemInventario.recurso,
      stockActual: this.nuevoItemInventario.cantidad
    };

    this.http.post(`${this.inventarioUrl}/acopio/${this.puntoSeleccionado.id}`, payload, { responseType: 'text' })
      .subscribe({
        next: async () => {
          this.nuevoItemInventario = { recurso: '', cantidad: 0 };
          this.cargarDetalles();
          await this.showToast('Item agregado al inventario.', 'success');
        },
        error: async () => await this.showToast('No se pudo agregar el item.', 'danger')
      });
  }

  comenzarEdicion(item: InventarioItem) {
    this.editandoItemId = item.id;
    this.edicionItem = { recurso: item.recurso, cantidad: item.stockActual };
  }

  cancelarEdicion() {
    this.editandoItemId = null;
  }

  async guardarEdicionItem(id: number) {
    if (!this.edicionItem.recurso.trim() || this.edicionItem.cantidad <= 0) {
      await this.showToast('Ingresa un recurso y una cantidad válida.', 'danger');
      return;
    }

    const payload = { recurso: this.edicionItem.recurso, stockActual: this.edicionItem.cantidad };

    this.http.put(`${this.inventarioUrl}/${id}`, payload, { responseType: 'text' }).subscribe({
      next: async () => {
        this.editandoItemId = null;
        this.cargarDetalles();
        await this.showToast('Item actualizado.', 'success');
      },
      error: async () => await this.showToast('No se pudo actualizar el item.', 'danger')
    });
  }

  eliminarItemInventario(id: number) {
    this.http.delete(`${this.inventarioUrl}/${id}`, { responseType: 'text' }).subscribe({
      next: async () => {
        this.cargarDetalles();
        await this.showToast('Item eliminado.', 'success');
      },
      error: async () => await this.showToast('No se pudo eliminar el item.', 'danger')
    });
  }

  private async showToast(message: string, color: 'success' | 'danger') {
    const toast = await this.toastCtrl.create({
      message, duration: 3500, position: 'bottom',
      cssClass: `toast-bottom-right toast-${color}`
    });
    await toast.present();
  }

  iconoDe(t?: string) { return iconoDeEmergencia(t); }
  imagenDe(t?: string) { return imagenDeEmergencia(t); }
  goTo(p: string) { this.navCtrl.navigateRoot(p); }
}
