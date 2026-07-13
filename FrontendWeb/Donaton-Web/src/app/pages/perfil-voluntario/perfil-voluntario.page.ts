import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NavController } from '@ionic/angular';
import { AuthService } from '../../services/auth.service';
import { environment } from '../../../environments/environment';

interface TransporteDTO { id: number; tipo: string; modelo: string; placa: string; }
interface AcopioDTO { id: number; nombre: string; direccion: string; comuna: string; }
interface VoluntarioDTO {
  rut: string;
  nombre: string;
  email: string;
  rol: string;
  transporteId?: number;
  transporte?: TransporteDTO;
  acopioId?: number;
  acopio?: AcopioDTO;
}

@Component({
  selector: 'app-perfil-voluntario',
  templateUrl: './perfil-voluntario.page.html',
  styleUrls: ['./perfil-voluntario.page.scss'],
  standalone: false,
})
export class PerfilVoluntarioPage implements OnInit {
  voluntario: VoluntarioDTO | null = null;
  cargando = true;

  private voluntarioUrl = `${environment.apiUrl}/logistica/voluntario`;

  constructor(
    private http: HttpClient,
    private navCtrl: NavController,
    private authService: AuthService
  ) {}

  ngOnInit() {
    const usuario = this.authService.usuarioActual;
    if (!usuario) {
      this.goTo('/login');
      return;
    }

    this.http.get<VoluntarioDTO[]>(this.voluntarioUrl).subscribe({
      next: (lista) => {
        this.voluntario = lista.find(v => v.rut === usuario.rut) ?? null;
        this.cargando = false;
      },
      error: () => {
        this.voluntario = null;
        this.cargando = false;
      }
    });
  }

  goTo(path: string) { this.navCtrl.navigateRoot(path); }
}
