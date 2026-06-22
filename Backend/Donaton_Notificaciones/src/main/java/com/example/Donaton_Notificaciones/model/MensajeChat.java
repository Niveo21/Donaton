package com.example.Donaton_Notificaciones.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MensajeChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String rut;
    private String nombre;
    // null = mensaje del chat general; con valor = mensaje privado dirigido a ese rut
    private String destinatarioRut;
    private String mensaje;
    private LocalDateTime fechaEnvio;

    public MensajeChat() {}

    public MensajeChat(String rut, String nombre, String mensaje) {
        this.rut = rut;
        this.nombre = nombre;
        this.mensaje = mensaje;
        this.fechaEnvio = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDestinatarioRut() { return destinatarioRut; }
    public void setDestinatarioRut(String destinatarioRut) { this.destinatarioRut = destinatarioRut; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }
}
