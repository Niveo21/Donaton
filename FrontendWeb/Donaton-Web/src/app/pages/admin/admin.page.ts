import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NavController, ToastController } from '@ionic/angular';
import { forkJoin } from 'rxjs';
import { AuthService } from '../../services/auth.service';
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

interface InventarioItem {
  id: number;
  recurso: string;
  stockActual: number;
  estado: string;
  acopioId?: number;
}

@Component({
  selector: 'app-admin',
  templateUrl: './admin.page.html',
  styleUrls: ['./admin.page.scss'],
  standalone: false,
})
export class AdminPage implements OnInit, OnDestroy {
  tab: 'acopio' | 'transporte' | 'voluntarios' = 'acopio';

  acopios: Acopio[] = [];
  transportes: Transporte[] = [];
  voluntarios: VoluntarioDTO[] = [];

  seleccionTransporte: Record<string, number | null> = {};
  seleccionAcopio: Record<string, number | null> = {};

  nuevoAcopio: {
    nombre: string; direccion: string; comuna: string; region: string;
    tipoEmergencia: string; titulo: string; descripcion: string; urgente: boolean;
    latitud: number | null; longitud: number | null;
  } = {
    nombre: '', direccion: '', comuna: '', region: '',
    tipoEmergencia: 'OTRO', titulo: '', descripcion: '', urgente: false,
    latitud: null, longitud: null
  };

  nuevoTransporte = { tipo: '', modelo: '', placa: '' };

  tiposEmergencia = ['INCENDIO', 'INUNDACION', 'TERREMOTO', 'SEQUIA', 'OTRO'];

  // Estado del modal y mapa

  modalAbierto = false;
  coordsSeleccionadas: { lat: number; lng: number } | null = null;
  busqueda = '';
  puntoSeleccionado: Acopio | null = null;

  // Modal de detalles (voluntarios + inventario del punto)
  
  modalDetalles = false;
  cargandoDetalles = false;
  voluntariosAcopio: VoluntarioDTO[] = [];
  inventarioAcopio: InventarioItem[] = [];
  nuevoItemInventario = { recurso: '', cantidad: 0 };
  editandoItemId: number | null = null;
  edicionItem = { recurso: '', cantidad: 0 };

  private map: L.Map | null = null;
  private marcadores: Map<number, L.Marker> = new Map();
  private pinTemporal: L.Marker | null = null;
  private marcadorActivo: L.Marker | null = null;

  private acopioUrl     = 'http://localhost:8085/logistica/acopio';
  private transporteUrl = 'http://localhost:8085/logistica/transporte';
  private voluntarioUrl = 'http://localhost:8085/logistica/voluntario';
  private inventarioUrl = 'http://localhost:8085/logistica/inventario';

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
    // Mapa se inicializa con un pequeño delay para que el DOM esté listo
    setTimeout(() => this.initMap(), 100);
  }

  ngOnDestroy() {
    if (this.map) {
      this.map.remove();
      this.map = null;
    }
  }

  goTo(path: string) { this.navCtrl.navigateRoot(path); }

  get acopiosFiltrados(): Acopio[] {
    const q = this.busqueda.trim().toLowerCase();
    if (!q) return this.acopios;
    return this.acopios.filter(a =>
      (a.titulo || a.nombre)?.toLowerCase().includes(q) ||
      a.comuna?.toLowerCase().includes(q) ||
      a.direccion?.toLowerCase().includes(q) ||
      a.region?.toLowerCase().includes(q)
    );
  }

  onBusquedaChange() {
    if (this.puntoSeleccionado && !this.acopiosFiltrados.some(a => a.id === this.puntoSeleccionado!.id)) {
      this.deseleccionarPin();
    }
    this.renderizarPins();
  }

  setTab(t: 'acopio' | 'transporte' | 'voluntarios') {
    this.tab = t;
    if (t === 'acopio') {

      // Si el mapa ya existe, solo le decimos que recalcule su tamaño

      setTimeout(() => {
        if (this.map) {
          this.map.invalidateSize();
        } else {
          this.initMap();
        }
      }, 50);
    }
  }

  // ── Mapa ────────────────────────────────────────────────────────────────────

  private initMap() {
    const contenedor = document.getElementById('admin-map');
    if (!contenedor || this.map) return;

    this.map = L.map('admin-map').setView([-35.5, -71.5], 5);

    L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
      attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> © <a href="https://carto.com/attributions">CARTO</a>',
      maxZoom: 18
    }).addTo(this.map);

    // Clic en el fondo del mapa → deseleccionar pin activo y abrir modal de creación
    this.map.on('click', (e: L.LeafletMouseEvent) => {
      this.deseleccionarPin();
      this.abrirModal(e.latlng.lat, e.latlng.lng);
    });

    this.renderizarPins();
  }

  private crearIcono(seleccionado = false, urgente = false): L.DivIcon {
    const fondo    = seleccionado ? 'white'   : (urgente ? '#dc2626' : '#1e2033');
    const puntoDot = seleccionado ? '#E05A2B' : 'white';
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

  private agregarPin(acopio: Acopio) {
    if (!this.map || acopio.latitud == null || acopio.longitud == null) return;
    const marker = L.marker([acopio.latitud, acopio.longitud], { icon: this.crearIcono(false, acopio.urgente) })
      .addTo(this.map);

    marker.on('click', (e: L.LeafletMouseEvent) => {


      L.DomEvent.stopPropagation(e); // evita que el clic también dispare la creación de un punto nuevo


      this.seleccionarPin(acopio, marker);
    });

    this.marcadores.set(acopio.id, marker);
  }

  private renderizarPins() {
    if (!this.map) return;


    // Limpiar marcadores existentes antes de volver a dibujar


    this.marcadores.forEach(m => m.remove());
    this.marcadores.clear();
    this.acopiosFiltrados.forEach(a => this.agregarPin(a));
  }

  private seleccionarPin(acopio: Acopio, marker: L.Marker) {
    if (this.marcadorActivo && this.puntoSeleccionado) {
      this.marcadorActivo.setIcon(this.crearIcono(false, this.puntoSeleccionado.urgente));
    }
    marker.setIcon(this.crearIcono(true, acopio.urgente));
    this.marcadorActivo = marker;
    this.puntoSeleccionado = acopio;
  }

  private deseleccionarPin() {
    if (this.marcadorActivo && this.puntoSeleccionado) {
      this.marcadorActivo.setIcon(this.crearIcono(false, this.puntoSeleccionado.urgente));
    }
    this.marcadorActivo = null;
    this.puntoSeleccionado = null;
  }

  cerrarSeleccion() {
    if (this.modalDetalles) this.cerrarModalDetalles();
    this.deseleccionarPin();
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

  irAPin(acopio: Acopio) {
    if (!this.map || acopio.latitud == null || acopio.longitud == null) return;
    const marker = this.marcadores.get(acopio.id);
    if (marker) this.seleccionarPin(acopio, marker);
    this.map.flyTo([acopio.latitud, acopio.longitud], 14, { duration: 1 });
  }

  abrirModal(lat: number, lng: number) {
    this.coordsSeleccionadas = { lat, lng };
    this.nuevoAcopio.latitud = lat;
    this.nuevoAcopio.longitud = lng;

    // Pin temporal para mostrar dónde se va a crear el punto
    if (this.pinTemporal) this.map?.removeLayer(this.pinTemporal);
    if (this.map) {
      this.pinTemporal = L.marker([lat, lng], { icon: this.crearIcono() }).addTo(this.map);
    }

    this.modalAbierto = true;
  }

  cerrarModal() {
    this.modalAbierto = false;
    this.coordsSeleccionadas = null;
    if (this.pinTemporal) {
      this.map?.removeLayer(this.pinTemporal);
      this.pinTemporal = null;
    }
    this.nuevoAcopio = {
      nombre: '', direccion: '', comuna: '', region: '',
      tipoEmergencia: 'OTRO', titulo: '', descripcion: '', urgente: false,
      latitud: null, longitud: null
    };
  }

  // ── CRUD ────────────────────────────────────────────────────────────────────

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
      next: (data) => {
        this.acopios = data;
        this.renderizarPins(); // actualizar pins cuando llegan los datos
      },
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
        // Quitar el pin del mapa inmediatamente sin recargar todo
        const marker = this.marcadores.get(id);
        if (marker) { marker.remove(); this.marcadores.delete(id); }
        if (this.puntoSeleccionado?.id === id) this.deseleccionarPin();
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
          this.cerrarModal();   // cierra modal y quita pin temporal
          this.cargarAcopios(); // recarga lista y redibuja pins (incluye el nuevo)
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
