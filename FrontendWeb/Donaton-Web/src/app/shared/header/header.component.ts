import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit } from '@angular/core';
import { NavController, ToastController } from '@ionic/angular';
import { HttpClient } from '@angular/common/http';
import { Client } from '@stomp/stompjs';
import { AuthService } from '../../services/auth.service';
import { environment } from '../../../environments/environment';

interface Notificacion {
  id: number;
  mensaje: string;
  fechaEnvio: string;
  leido: boolean;
}

interface MensajeChat {
  rut: string;
  nombre: string;
  destinatarioRut?: string | null;
  mensaje: string;
}

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  standalone: false,
})
export class HeaderComponent implements OnInit, OnDestroy {
  @Input() logoIcon = 'D';
  @Input() logoPrefix = 'Chile';
  @Input() logoText = 'Donaton';

  menuAbierto = false;
  popoverEvent: Event | undefined;

  notificaciones: Notificacion[] = [];
  notificacionesAbierto = false;
  notifEvent: Event | undefined;

  private notificacionUrl = `${environment.apiUrl}/notificacion`;
  private stompClient: Client | undefined;

  constructor(
    private navCtrl: NavController,
    private authService: AuthService,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private toastCtrl: ToastController
  ) {}

  ngOnInit() {
    this.usuario$.subscribe(usuario => {
      if (usuario) {
        this.cargarNotificaciones();
        this.conectarWebSocket();
      } else {
        this.desconectarWebSocket();
      }
    });
  }

  ngOnDestroy() {
    this.desconectarWebSocket();
  }

  get usuario$() {
    return this.authService.usuario$;
  }

  get noLeidas(): number {
    return this.notificaciones.filter(n => !n.leido).length;
  }

  goTo(p: string) {
    this.menuAbierto = false;
    this.navCtrl.navigateRoot(p);
  }

  toggleMenu(ev: Event) {
    this.popoverEvent = ev;
    this.menuAbierto = !this.menuAbierto;
  }

  abrirNotificaciones(ev: Event) {
    this.notifEvent = ev;
    this.notificacionesAbierto = !this.notificacionesAbierto;
    if (this.notificacionesAbierto) {
      this.cargarNotificaciones(() => this.marcarComoLeidas());
    }
  }
  
  eliminarNotificacion(id: number, event: Event) {
    event.stopPropagation();
    this.http.delete(`${this.notificacionUrl}/${id}`, { responseType: 'text' }).subscribe({
      next: () => {
        this.notificaciones = this.notificaciones.filter(n => n.id !== id);
        this.cdr.detectChanges();
      },
      error: (err) => console.error('No se pudo eliminar la notificación:', err)
    });
  }

  private cargarNotificaciones(alTerminar?: () => void) {
    this.http.get<Notificacion[]>(this.notificacionUrl).subscribe({
      next: (data) => {
        this.notificaciones = data;
        this.cdr.detectChanges();
        alTerminar?.();
      },
      error: () => {
        this.notificaciones = [];
        this.cdr.detectChanges();
      }
    });
  }

  private marcarComoLeidas() {
    if (this.noLeidas === 0) return;
    this.http.put(`${this.notificacionUrl}/marcar-leidas`, {}, { responseType: 'text' }).subscribe({
      next: () => {
        this.notificaciones.forEach(n => n.leido = true);
        this.cdr.detectChanges();
      },
      error: (err) => console.error('No se pudo marcar como leídas:', err)
    });
  }

  private conectarWebSocket() {
    if (this.stompClient?.active) return;

    this.stompClient = new Client({
      brokerURL: `${environment.wsUrl}/ws-notificaciones`,
      reconnectDelay: 5000,
      onConnect: () => {
        this.stompClient?.subscribe('/topic/notificaciones', (mensaje) => {
          const nueva: Notificacion = JSON.parse(mensaje.body);
          this.notificaciones = [nueva, ...this.notificaciones];
          this.cdr.detectChanges();
        });

        if (this.authService.usuarioActual?.rol === 'VOLUNTARIO') {
          this.stompClient?.subscribe('/topic/chat', (mensaje) => {
            const recibido: MensajeChat = JSON.parse(mensaje.body);
            this.avisarMensajeChat(recibido);
          });
        }
      },
    });

    this.stompClient.activate();
  }

  private avisarMensajeChat(mensaje: MensajeChat) {
    const usuario = this.authService.usuarioActual;
    if (!usuario || mensaje.rut === usuario.rut) return;

    const esPrivado = !!mensaje.destinatarioRut;
    if (esPrivado && mensaje.destinatarioRut !== usuario.rut) return;

    const texto = esPrivado
      ? `${mensaje.nombre} te escribió: ${mensaje.mensaje}`
      : `${mensaje.nombre} (chat general): ${mensaje.mensaje}`;

    this.toastCtrl.create({
      message: texto,
      duration: 4000,
      position: 'bottom',
      cssClass: 'toast-bottom-right toast-success',
    }).then(toast => toast.present());
  }

  private desconectarWebSocket() {
    this.stompClient?.deactivate();
    this.stompClient = undefined;
  }

  cerrarSesion() {
    this.menuAbierto = false;
    this.authService.logout();
    this.navCtrl.navigateRoot('/home');
  }
}
