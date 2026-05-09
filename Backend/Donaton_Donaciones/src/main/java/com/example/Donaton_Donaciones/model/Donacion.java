package com.example.Donaton_Donaciones.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Donacion {

     @Id
     @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
     private int id;
     private String tipoRecurso;
     private int cantidad;
     private String unidadMedida;
     private LocalDateTime fechaDonacion;
     private String estado;
     private String tipoDonacion;
     

        public Donacion() {
        }

        public Donacion(int id, String tipoRecurso, int cantidad, String unidadMedida, LocalDateTime fechaDonacion, String estado) {
            this.id = id;
            this.tipoRecurso = tipoRecurso;
            this.cantidad = cantidad;
            this.unidadMedida = unidadMedida;
            this.fechaDonacion = LocalDateTime.now();
            this.estado = estado;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getUnidadMedida() {
            return unidadMedida;
        }

        public void setUnidadMedida(String unidadMedida) {
            this.unidadMedida = unidadMedida;
        }

        public LocalDateTime getFechaDonacion() {
            return fechaDonacion;
        }

        public void setFechaDonacion(LocalDateTime fechaDonacion) {
            this.fechaDonacion = fechaDonacion;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public String getTipoDonacion() {
            return tipoDonacion;
        }

        public void setTipoDonacion(String tipoDonacion) {
            this.tipoDonacion = tipoDonacion;
        }

}
