package com.example.donatonlogistica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "transporte_id", nullable = false)
    private Transporte transporte;

    @ManyToOne
    @JoinColumn(name = "acopio_destino_id", nullable = false)
    private Acopio acopioDestino;

    private String tipoRecurso;
    private int cantidad;
    private LocalDateTime fechaEnvio;
    private String estado;

    public Envio() {}

    public Envio(Transporte transporte, Acopio acopioDestino, String tipoRecurso, int cantidad, LocalDateTime fechaEnvio, String estado) {
        this.transporte = transporte;
        this.acopioDestino = acopioDestino;
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

    public Transporte getTransporte() {
        return transporte;
    }

    public void setTransporte(Transporte transporte) {
        this.transporte = transporte;
    }

    public Acopio getAcopioDestino() {
        return acopioDestino;
    }

    public void setAcopioDestino(Acopio acopioDestino) {
        this.acopioDestino = acopioDestino;
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
