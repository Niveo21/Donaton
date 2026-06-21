package com.example.Donaton_Donaciones.dto;

import java.time.LocalDate;

/**
 * DTO que recibe todos los campos posibles de una donación desde el frontend.
 * Solo los campos del tipo correspondiente necesitan tener valor; los demás pueden ser null.
 */
public class DonacionRequest {

    // Campo obligatorio: tipo de donación
    private String tipoDonacion; // "ALIMENTO", "ROPA", "DINERO", "INSUMO_HIGIENE"

    // Campos comunes
    private int cantidad;

    // Campos para ALIMENTO
    private String nombre;
    private LocalDate fechaVencimiento;
    private String unidadMedida;
    private boolean requiereRefrigeracion;

    // Campos para ROPA
    private String tipoRopa;
    private String talla;
    private String estadoArticulo; // "nuevo" o "usado"

    // Campos para DINERO
    private double monto;
    private String moneda;
    private String metodoPago;
    private String numeroTarjeta;

    // Campos para INSUMO_HIGIENE
    private String categoria; // "personal" o "hogar"
    

    // Constructor vacío no tocar ultima advertencia
    public DonacionRequest() {
    }

    // --- Getters y Setters ---

    public String getTipoDonacion() {
        return tipoDonacion;
    }

    public void setTipoDonacion(String tipoDonacion) {
        this.tipoDonacion = tipoDonacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public boolean isRequiereRefrigeracion() {
        return requiereRefrigeracion;
    }

    public void setRequiereRefrigeracion(boolean requiereRefrigeracion) {
        this.requiereRefrigeracion = requiereRefrigeracion;
    }

    public String getTipoRopa() {
        return tipoRopa;
    }

    public void setTipoRopa(String tipoRopa) {
        this.tipoRopa = tipoRopa;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getEstadoArticulo() {
        return estadoArticulo;
    }

    public void setEstadoArticulo(String estadoArticulo) {
        this.estadoArticulo = estadoArticulo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getUnidadMedida() {
        return unidadMedida;
    }
    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
}
