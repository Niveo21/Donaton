package com.example.donatonlogistica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class Acopio {

    @Id
    private int id;
    private String nombre;
    private String direccion;
    private String comuna;
    

    public Acopio() {
        
    }

    public Acopio(int id, String nombre, String direccion, String comuna) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.comuna = comuna;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getDireccion() {
        return direccion;
    }


    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    public String getComuna() {
        return comuna;
    }


    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

}


