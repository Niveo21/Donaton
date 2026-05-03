package com.example.Donaton_Logistica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Voluntarios {

    @Id
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String rut;

    public Voluntarios() {
    }

    public Voluntarios(int id, String nombre, String apellido, String email, String telefono, String rut) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.rut = rut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

}
