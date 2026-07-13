package com.example.donatonlogistica.dto;

public class DonacionDTO {

    private String tipoRecurso;
    private int cantidad;
    private Integer acopioId;

    public DonacionDTO() {}

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

    public Integer getAcopioId() {
        return acopioId;
    }

    public void setAcopioId(Integer acopioId) {
        this.acopioId = acopioId;
    }

   
}
