package com.example.Donaton_Notificaciones.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Notificacion {

    @Id
    private int id;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private Boolean leido;


    public Notificacion() {
    }

    public Notificacion(int id, String mensaje, LocalDateTime fechaEnvio, Boolean leido) {
        this.id = id;
        this.mensaje = mensaje;
        this.fechaEnvio = LocalDateTime.now();
        this.leido = leido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

}
