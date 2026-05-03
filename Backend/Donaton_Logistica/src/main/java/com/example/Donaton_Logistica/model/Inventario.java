package com.example.Donaton_Logistica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Inventario {

    @Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String recurso;
    private int stockActual;
    private String estado;

    public Inventario() {
    }

    public Inventario(int id, String recurso, int stockActual, String estado) {
        this.id = id;
        this.recurso = recurso;
        this.stockActual = stockActual;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    

}
