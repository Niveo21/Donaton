package com.example.Donaton_Registro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Registro {

    @Id
    private String rut;
    private String nombre;
    private String email;
    private String password;
    private String rol;

    public Registro() {
    }

    public Registro(String rut, String nombre, String email, String password, String rol) {
        this.rut = rut;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    

}
