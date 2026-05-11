import { Component } from '@angular/core';



import { Router } from '@angular/router';
import { NavController } from '@ionic/angular';

import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-registro-donante',
  templateUrl: './registro-donante.page.html',
  styleUrls: ['./registro-donante.page.scss'],
  standalone: false,


})
export class RegistroDonantePage {
  //telefono y apellido eliminaods por el momento
  form = { nombre: '', rut: '', email: '', password: '', confirmar: '' };

  private apiUrl = 'http://localhost:8085/usuario/registro';

  constructor(private router: Router, private navCtrl: NavController, private http: HttpClient) { }
  goTo(p: string) { this.navCtrl.navigateRoot(p); }
  onSubmit() {

    if (this.form.password !== this.form.confirmar) {
      console.error('Las contraseñas no coinciden');
      return;
    }
    // Preparamos el objeto con los datos a enviar. 
    const data = {
      nombre: this.form.nombre,
      //apellido: this.form.apellido,
      rut: this.form.rut,
      email: this.form.email,
      //telefono: this.form.telefono,
      password: this.form.password,
      rol: 'DONANTE'
    };
    // Hacemos la petición POST al API Gateway
    this.http.post(this.apiUrl, data).subscribe({
      next: (response: any) => {
        console.log('Registro exitoso:', response);
        // Redirigir al login después de un registro exitoso
        this.goTo('/login');
      },
      error: (error) => {
        console.error('Error al registrar donante:', error);
      }
    });
  }
}

