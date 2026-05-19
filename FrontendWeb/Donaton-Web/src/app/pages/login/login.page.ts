import { Component } from '@angular/core';



import { Router } from '@angular/router';
import { NavController } from '@ionic/angular';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: false,


})
export class LoginPage {
  email = ''; password = '';
  private apiUrl = 'http://localhost:8085/usuario/login';


  constructor(private router: Router, private navCtrl: NavController, private http: HttpClient) { }


  goTo(p: string) { this.navCtrl.navigateRoot(p); }
  onSubmit() {
  if (this.email === '' || this.password === '') {
    console.log("Por favor complete todos los campos");
    return;
  }

  const credentials = {
    email: this.email,
    password: this.password
  };

  this.http.post(this.apiUrl, credentials).subscribe({
    next: (response: any) => {
      
      if (response === true) {
        console.log('Login exitoso:', response);
        
     
        
        this.goTo('/home');
      } else {
        
        console.warn('Credenciales incorrectas según el servidor');
        alert("Usuario o contraseña incorrectos");
      }
    },
    error: (error) => {
      
      console.error('Error de conexión o error 4xx/5xx:', error);
      alert("Error al conectar con el servidor. Intente más tarde.");
    }
  });
}
}

