package com.example.Donaton_Necesidad.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Necesidad {

    @Id
    private long id;
    private int acopioId;
    private String tipo;       // "ALIMENTOS", "HIGIENE", "ROPA"
    private String descripcion;
    private boolean urgente;

    public Necesidad() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getAcopioId() { return acopioId; }
    public void setAcopioId(int acopioId) { this.acopioId = acopioId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isUrgente() { return urgente; }
    public void setUrgente(boolean urgente) { this.urgente = urgente; }
}
