package com.example.Donaton_Acopio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Acopio {

    @Id
    private int id;
    private String nombre;
    private String direccion;
    private String comuna;

    // Contexto de la emergencia que este punto de acopio está atendiendo.
    // tipoEmergencia: "INCENDIO", "INUNDACION", "TERREMOTO", "SEQUIA", "OTRO"
    // El ícono se resuelve en el frontend a partir de este valor (evita problemas
    // de encoding de emojis en la base de datos).
    private String region;
    private String tipoEmergencia;
    private String titulo;
    private String descripcion;
    private boolean urgente;

    public Acopio() {}

    public Acopio(int id, String nombre, String direccion, String comuna) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.comuna = comuna;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getComuna() { return comuna; }
    public void setComuna(String comuna) { this.comuna = comuna; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getTipoEmergencia() { return tipoEmergencia; }
    public void setTipoEmergencia(String tipoEmergencia) { this.tipoEmergencia = tipoEmergencia; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isUrgente() { return urgente; }
    public void setUrgente(boolean urgente) { this.urgente = urgente; }
}
