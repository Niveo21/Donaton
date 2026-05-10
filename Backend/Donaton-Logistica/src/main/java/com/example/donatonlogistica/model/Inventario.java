package com.example.donatonlogistica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Inventario {

    @Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;
    private String recurso;
    private Integer stockActual;
    private String estado;

    public Inventario() {
    }

    public Inventario(Integer id, String recurso, Integer stockActual, String estado) {
        this.id = id;
        this.recurso = recurso;
        this.stockActual = stockActual;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    

}
