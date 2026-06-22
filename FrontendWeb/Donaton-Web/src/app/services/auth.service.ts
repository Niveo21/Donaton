import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Usuario {
  rut: string;
  nombre: string;
  email: string;
  rol: string;
  token: string;
}

const STORAGE_KEY = 'donaton_usuario';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private usuarioSubject = new BehaviorSubject<Usuario | null>(this.leerDeStorage());
  usuario$ = this.usuarioSubject.asObservable();

  private leerDeStorage(): Usuario | null {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : null;
  }

  login(usuario: Usuario) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(usuario));
    this.usuarioSubject.next(usuario);
  }

  logout() {
    localStorage.removeItem(STORAGE_KEY);
    this.usuarioSubject.next(null);
  }

  get usuarioActual(): Usuario | null {
    return this.usuarioSubject.value;
  }

  estaLogueado(): boolean {
    return this.usuarioActual !== null;
  }
}
