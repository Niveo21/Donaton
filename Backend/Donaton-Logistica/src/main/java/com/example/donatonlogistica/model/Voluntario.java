package com.example.donatonlogistica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Voluntario {

    @Id
    private String rut;
    private String nombre;
    private String email;
    private String rol;

    // Transporte y Acopio viven en sus propios microservicios, por eso
    // solo se guarda el id en vez de una relación JPA entre bases distintas.
    private Integer transporteId;
    private Integer acopioId;

    public Voluntario() {
    }

    public Voluntario(String rut, String nombre, String email, String rol) {
        this.rut = rut;
        this.nombre = nombre;
        this.email = email;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Integer getTransporteId() {
        return transporteId;
    }

    public void setTransporteId(Integer transporteId) {
        this.transporteId = transporteId;
    }

    public Integer getAcopioId() {
        return acopioId;
    }

    public void setAcopioId(Integer acopioId) {
        this.acopioId = acopioId;
    }
}
