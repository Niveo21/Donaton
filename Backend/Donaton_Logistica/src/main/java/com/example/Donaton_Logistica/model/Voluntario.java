package com.example.Donaton_Logistica.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Voluntario {

    @Id
    private String rut;
    private String nombre;
    private String email;
    private String rol;

    @ManyToOne
    @JoinColumn(name = "transporte_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Transporte transporte;

    @ManyToOne
    @JoinColumn(name = "acopio_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Acopio acopio;

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

    public Transporte getTransporte() {
        return transporte;
    }

    public void setTransporte(Transporte transporte) {
        this.transporte = transporte;
    }

    public Acopio getAcopio() {
        return acopio;
    }

    public void setAcopio(Acopio acopio) {
        this.acopio = acopio;
    }
}
