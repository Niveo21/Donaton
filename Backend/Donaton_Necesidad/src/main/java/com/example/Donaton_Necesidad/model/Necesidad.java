package com.example.Donaton_Necesidad.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Necesidad {

    @Id
    private int id;
    private String tipoRecursoRequerido;
    private int cantidadSolicitada;
    private String prioridad;


    public Necesidad() {
    }

    public Necesidad(int id, String tipoRecursoRequerido, int cantidadSolicitada, String prioridad) {
        this.id = id;
        this.tipoRecursoRequerido = tipoRecursoRequerido;
        this.cantidadSolicitada = cantidadSolicitada;
        this.prioridad = prioridad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoRecursoRequerido() {
        return tipoRecursoRequerido;
    }

    public void setTipoRecursoRequerido(String tipoRecursoRequerido) {
        this.tipoRecursoRequerido = tipoRecursoRequerido;
    }

    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(int cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    

}
