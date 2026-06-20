package com.example.Donaton_Transporte.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Transporte {

    @Id
    private int id;
    private String tipo;
    private String modelo;
    private String placa;

    public Transporte() {}

    public Transporte(int id, String tipo, String modelo, String placa) {
        this.id = id;
        this.tipo = tipo;
        this.modelo = modelo;
        this.placa = placa;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
}
