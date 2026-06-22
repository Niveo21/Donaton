import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NavController } from '@ionic/angular';
import { Client } from '@stomp/stompjs';
import { AuthService } from '../../services/auth.service';

interface MensajeChat {
  id?: number;
  rut: string;
  nombre: string;
  destinatarioRut?: string | null;
  mensaje: string;
  fechaEnvio?: string;
}

interface Contacto {
  rut: string;
  nombre: string;
}

@Component({
  selector: 'app-chat-voluntarios',
  templateUrl: './chat-voluntarios.page.html',
  styleUrls: ['./chat-voluntarios.page.scss'],
  standalone: false,
})
export class ChatVoluntariosPage implements OnInit, OnDestroy {
  todosMensajes: MensajeChat[] = [];
  contactos: Contacto[] = [];
  canalActivo: { rut: string | null; nombre: string } = { rut: null, nombre: 'General' };
  texto = '';

  private chatHistorialUrl = 'http://localhost:8085/chat';
  private voluntarioUrl = 'http://localhost:8085/logistica/voluntario';
  private stompClient: Client | undefined;

  constructor(
    private http: HttpClient,
    private navCtrl: NavController,
    private authService: AuthService
  ) {}

  get usuario() {
    return this.authService.usuarioActual;
  }

  get mensajesCanal(): MensajeChat[] {
    const usuario = this.usuario;
    if (!usuario) return [];

    if (this.canalActivo.rut === null) {
      return this.todosMensajes.filter(m => !m.destinatarioRut);
    }

    const otro = this.canalActivo.rut;
    return this.todosMensajes.filter(m =>
      (m.rut === usuario.rut && m.destinatarioRut === otro) ||
      (m.rut === otro && m.destinatarioRut === usuario.rut)
    );
  }

  ngOnInit() {
    const usuario = this.authService.usuarioActual;
    if (!usuario || usuario.rol !== 'VOLUNTARIO') {
      this.goTo('/home');
      return;
    }

    this.cargarHistorial();
    this.cargarContactos();
    this.conectarWebSocket();
  }

  ngOnDestroy() {
    this.stompClient?.deactivate();
  }

  goTo(path: string) { this.navCtrl.navigateRoot(path); }

  seleccionarCanal(rut: string | null, nombre: string) {
    this.canalActivo = { rut, nombre };
  }

  formatearHora(fechaEnvio?: string): string {
    if (!fechaEnvio) return '';
    const fecha = new Date(fechaEnvio);
    return fecha.toLocaleTimeString('es-CL', { hour: '2-digit', minute: '2-digit' });
  }

  private cargarHistorial() {
    this.http.get<MensajeChat[]>(this.chatHistorialUrl).subscribe({
      next: (data) => this.todosMensajes = data,
      error: () => this.todosMensajes = []
    });
  }

  private cargarContactos() {
    const usuario = this.usuario;
    this.http.get<any[]>(this.voluntarioUrl).subscribe({
      next: (lista) => {
        this.contactos = lista
          .filter(v => v.rut !== usuario?.rut)
          .map(v => ({ rut: v.rut, nombre: v.nombre }));
      },
      error: () => this.contactos = []
    });
  }

  private conectarWebSocket() {
    this.stompClient = new Client({
      brokerURL: 'ws://localhost:8083/ws-notificaciones',
      reconnectDelay: 5000,
      onConnect: () => {
        this.stompClient?.subscribe('/topic/chat', (mensaje) => {
          const nuevo: MensajeChat = JSON.parse(mensaje.body);
          this.todosMensajes = [...this.todosMensajes, nuevo];
        });
      },
    });
    this.stompClient.activate();
  }

  enviarMensaje() {
    const usuario = this.authService.usuarioActual;
    if (!usuario || !this.texto.trim() || !this.stompClient?.active) return;

    this.stompClient.publish({
      destination: '/app/chat.enviar',
      body: JSON.stringify({
        rut: usuario.rut,
        nombre: usuario.nombre,
        destinatarioRut: this.canalActivo.rut,
        mensaje: this.texto.trim()
      })
    });

    this.texto = '';
  }
}
