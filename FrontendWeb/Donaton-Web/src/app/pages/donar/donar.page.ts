import { Component, OnInit } from '@angular/core';
import { NavController, ToastController } from '@ionic/angular';
import { HttpClient } from '@angular/common/http';
import { forkJoin } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { iconoDeEmergencia, imagenDeEmergencia } from '../../shared/emergencia-utils';
import { environment } from '../../../environments/environment';

interface ItemDonacion { nombre: string; cantidad: number; }
interface PuntoAcopio {
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

type TipoDonacion = 'DINERO' | 'ROPA' | 'ALIMENTOS' | 'HIGIENE';
type MetodoEntrega = 'ACOPIO' | 'DOMICILIO';
type MetodoPago = 'WEBPAY' | 'TRANSFERENCIA' | 'TARJETA';

@Component({
  selector: 'app-donar',
  templateUrl: './donar.page.html',
  styleUrls: ['./donar.page.scss'],
  standalone: false,
})
export class DonarPage implements OnInit {
  presets = [5000, 10000, 25000, 50000];
  amount = 10000;

  donationType: TipoDonacion = 'DINERO';
  deliveryMethod: MetodoEntrega = 'ACOPIO';
  paymentMethod: MetodoPago = 'WEBPAY';

  itemsRopa: ItemDonacion[] = [
    { nombre: 'Abrigo adulto', cantidad: 0 },
    { nombre: 'Abrigo niño/a', cantidad: 0 },
    { nombre: 'Calzado', cantidad: 0 },
    { nombre: 'Frazadas', cantidad: 0 },
    { nombre: 'Ropa interior nueva', cantidad: 0 },
  ];

  itemsAlimentos: ItemDonacion[] = [
    { nombre: 'Arroz', cantidad: 0 },
    { nombre: 'Aceite', cantidad: 0 },
    { nombre: 'Conservas', cantidad: 0 },
    { nombre: 'Agua embotellada', cantidad: 0 },
    { nombre: 'Leche en polvo', cantidad: 0 },
  ];

  itemsHigiene: ItemDonacion[] = [
    { nombre: 'Pañales', cantidad: 0 },
    { nombre: 'Toallas higiénicas', cantidad: 0 },
    { nombre: 'Jabón', cantidad: 0 },
    { nombre: 'Pasta dental', cantidad: 0 },
    { nombre: 'Papel higiénico', cantidad: 0 },
  ];

  puntosAcopio: PuntoAcopio[] = [];
  acopioSeleccionado: PuntoAcopio | null = null;

  domicilioForm = { direccion: '', comuna: '', telefono: '' };
  tarjetaForm = { numero: '', vencimiento: '', cvv: '' };

  private apiUrl = `${environment.apiUrl}/donacion`;
  private acopioUrl = `${environment.apiUrl}/logistica/acopio`;

  constructor(
    private navCtrl: NavController,
    private http: HttpClient,
    private toastCtrl: ToastController,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.http.get<PuntoAcopio[]>(this.acopioUrl).subscribe({
      next: (puntos) => this.puntosAcopio = puntos,
      error: () => this.puntosAcopio = []
    });
  }

  formatCLP(n: number) { return new Intl.NumberFormat('es-CL', { style: 'currency', currency: 'CLP', maximumFractionDigits: 0 }).format(n); }

  get donationImpact() {
    if (this.amount >= 50000) return 'Kits completos de emergencia para 4 familias.';
    if (this.amount >= 25000) return 'Agua potable para una familia por 2 semanas.';
    if (this.amount >= 10000) return 'Alimentos esenciales para 3 días.';
    return 'Frazadas térmicas para una persona.';
  }

  get itemsActuales(): ItemDonacion[] {
    switch (this.donationType) {
      case 'ROPA': return this.itemsRopa;
      case 'ALIMENTOS': return this.itemsAlimentos;
      case 'HIGIENE': return this.itemsHigiene;
      default: return [];
    }
  }

  get totalItems(): number {
    return this.itemsActuales.reduce((suma, i) => suma + i.cantidad, 0);
  }

  iconoDe(tipoEmergencia?: string): string {
    return iconoDeEmergencia(tipoEmergencia);
  }

  imagenDe(tipoEmergencia?: string): string {
    return imagenDeEmergencia(tipoEmergencia);
  }

  setDonationType(tipo: TipoDonacion) { this.donationType = tipo; }
  setDeliveryMethod(metodo: MetodoEntrega) { this.deliveryMethod = metodo; }
  setPaymentMethod(metodo: MetodoPago) { this.paymentMethod = metodo; }

  incrementarItem(item: ItemDonacion) { item.cantidad++; }
  decrementarItem(item: ItemDonacion) { if (item.cantidad > 0) item.cantidad--; }

  seleccionarAcopio(p: PuntoAcopio) { this.acopioSeleccionado = p; }

  setAmount(p: number) { this.amount = p; }
  goTo(path: string) { this.navCtrl.navigateRoot(path); }

  onTarjetaNumeroInput(event: Event) {
    const input = event.target as HTMLInputElement;
    const digitos = input.value.replace(/\D/g, '').slice(0, 16);
    this.tarjetaForm.numero = digitos.replace(/(.{4})/g, '$1 ').trim();
    input.value = this.tarjetaForm.numero;
  }

  onVencimientoInput(event: Event) {
    const input = event.target as HTMLInputElement;
    let digitos = input.value.replace(/\D/g, '').slice(0, 4);
    this.tarjetaForm.vencimiento = digitos.length > 2
      ? `${digitos.slice(0, 2)}/${digitos.slice(2)}`
      : digitos;
    input.value = this.tarjetaForm.vencimiento;
  }

  onCvvInput(event: Event) {
    const input = event.target as HTMLInputElement;
    this.tarjetaForm.cvv = input.value.replace(/\D/g, '').slice(0, 3);
    input.value = this.tarjetaForm.cvv;
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

  async donar() {
    const usuario = this.authService.usuarioActual;
    if (!usuario) {
      await this.showToast('Debes iniciar sesión para donar.', 'danger');
      this.goTo('/login');
      return;
    }

    if (this.amount < 1000) {
      await this.showToast('El monto mínimo es $1.000 CLP.', 'danger');
      return;
    }

    if (this.paymentMethod === 'TARJETA') {
      if (!this.tarjetaForm.numero.trim() || !this.tarjetaForm.vencimiento.trim() || !this.tarjetaForm.cvv.trim()) {
        await this.showToast('Completa los datos de la tarjeta.', 'danger');
        return;
      }
    }

    const metodoPagoTexto: Record<MetodoPago, string> = {
      WEBPAY: 'Webpay',
      TRANSFERENCIA: 'Transferencia',
      TARJETA: 'Tarjeta',
    };

    const payload = {
      tipoDonacion: 'DINERO',
      cantidad: this.amount,
      moneda: 'CLP',
      metodoPago: metodoPagoTexto[this.paymentMethod],
      numeroTarjeta: this.paymentMethod === 'TARJETA' ? this.tarjetaForm.numero : null,
    };

    this.http.post<any>(this.apiUrl, payload).subscribe({
      next: async (response) => {
        if (response?.estado === 'Pago aprobado') {
          await this.showToast(`¡Gracias por tu donación de ${this.formatCLP(this.amount)}!`, 'success');
        } else {
          await this.showToast('El pago fue rechazado. Intenta con otro método.', 'danger');
        }
      },
      error: async () => {
        await this.showToast('Ocurrió un error al procesar la donación.', 'danger');
      }
    });
  }

  async confirmarEntregaAcopio() {
    if (!this.acopioSeleccionado) {
      await this.showToast('Selecciona un punto de acopio.', 'danger');
      return;
    }
    await this.enviarDonacionFisica('ACOPIO');
  }

  async solicitarRetiroDomicilio() {
    if (!this.domicilioForm.direccion.trim() || !this.domicilioForm.comuna.trim()) {
      await this.showToast('Completa dirección y comuna.', 'danger');
      return;
    }
    if (!this.telefonoValido(this.domicilioForm.telefono)) {
      await this.showToast('Ingresa un teléfono válido (9 dígitos, ej: 912345678).', 'danger');
      return;
    }
    await this.enviarDonacionFisica('DOMICILIO');
  }

  private telefonoValido(telefono: string): boolean {
    return /^9\d{8}$/.test(telefono.trim());
  }

  private async enviarDonacionFisica(metodoEntrega: MetodoEntrega) {
    const usuario = this.authService.usuarioActual;
    if (!usuario) {
      await this.showToast('Debes iniciar sesión para donar.', 'danger');
      this.goTo('/login');
      return;
    }

    const items = this.itemsActuales.filter(i => i.cantidad > 0);
    if (items.length === 0) {
      await this.showToast('Selecciona al menos un ítem para donar.', 'danger');
      return;
    }

    const peticiones = items.map(item =>
      this.http.post(this.apiUrl, this.construirPayload(item, metodoEntrega, usuario.nombre))
    );

    forkJoin(peticiones).subscribe({
      next: async () => {
        await this.showToast('¡Donación registrada con éxito! Gracias por tu aporte.', 'success');
        items.forEach(i => i.cantidad = 0);
      },
      error: async () => {
        await this.showToast('Ocurrió un error al registrar la donación.', 'danger');
      }
    });
  }

  private construirPayload(item: ItemDonacion, metodoEntrega: MetodoEntrega, nombreSolicitante: string) {
    const base = {
      metodoEntrega,
      direccionRetiro: this.domicilioForm.direccion,
      comuna: this.domicilioForm.comuna,
      telefonoContacto: this.domicilioForm.telefono ? `+56${this.domicilioForm.telefono}` : '',
      nombreSolicitante,
      acopioId: metodoEntrega === 'ACOPIO' ? this.acopioSeleccionado?.id : null,
    };

    switch (this.donationType) {
      case 'ROPA':
        return { ...base, tipoDonacion: 'ROPA', tipoRopa: item.nombre, cantidad: item.cantidad, talla: 'Única' };
      case 'ALIMENTOS':
        return { ...base, tipoDonacion: 'ALIMENTO', nombre: item.nombre, cantidad: item.cantidad, unidadMedida: 'unidades' };
      case 'HIGIENE':
        return { ...base, tipoDonacion: 'INSUMO_HIGIENE', nombre: item.nombre, cantidad: item.cantidad, categoria: 'hogar' };
      default:
        return base;
    }
  }
}
