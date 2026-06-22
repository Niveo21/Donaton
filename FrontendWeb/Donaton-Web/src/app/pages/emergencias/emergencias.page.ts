import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NavController } from '@ionic/angular';
import { iconoDeEmergencia, imagenDeEmergencia } from '../../shared/emergencia-utils';

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

@Component({
  selector: 'app-emergencias',
  templateUrl: './emergencias.page.html',
  styleUrls: ['./emergencias.page.scss'],
  standalone: false,
})
export class EmergenciasPage implements OnInit {
  emergencies: Acopio[] = [];

  private acopioUrl = 'http://localhost:8085/logistica/acopio';

  constructor(private navCtrl: NavController, private http: HttpClient) {}

  ngOnInit() {
    this.http.get<Acopio[]>(this.acopioUrl).subscribe({
      next: (puntos) => this.emergencies = puntos,
      error: () => this.emergencies = []
    });
  }

  iconoDe(tipoEmergencia?: string): string { return iconoDeEmergencia(tipoEmergencia); }
  imagenDe(tipoEmergencia?: string): string { return imagenDeEmergencia(tipoEmergencia); }

  goTo(p: string) { this.navCtrl.navigateRoot(p); }
}
