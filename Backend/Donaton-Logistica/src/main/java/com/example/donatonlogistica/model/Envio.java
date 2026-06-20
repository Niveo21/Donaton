package com.example.donatonlogistica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Transporte y Acopio viven en sus propios microservicios, por eso
    // solo se guarda el id en vez de una relación JPA entre bases distintas.
    private int transporteId;
    private int acopioDestinoId;

    private String tipoRecurso;
    private int cantidad;
    private LocalDateTime fechaEnvio;
    private String estado;

    public Envio() {}

    public Envio(int transporteId, int acopioDestinoId, String tipoRecurso, int cantidad, LocalDateTime fechaEnvio, String estado) {
        this.transporteId = transporteId;
        this.acopioDestinoId = acopioDestinoId;
        this.tipoRecurso = tipoRecurso;
        this.cantidad = cantidad;
        this.fechaEnvio = fechaEnvio;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransporteId() {
        return transporteId;
    }

    public void setTransporteId(int transporteId) {
        this.transporteId = transporteId;
    }

    public int getAcopioDestinoId() {
        return acopioDestinoId;
    }

    public void setAcopioDestinoId(int acopioDestinoId) {
        this.acopioDestinoId = acopioDestinoId;
    }

    public String getTipoRecurso() {
        return tipoRecurso;
    }

    public void setTipoRecurso(String tipoRecurso) {
        this.tipoRecurso = tipoRecurso;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

   
}
