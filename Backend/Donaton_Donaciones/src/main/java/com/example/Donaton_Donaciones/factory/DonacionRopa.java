package com.example.Donaton_Donaciones.factory;

/**
 * Producto concreto: Donación de ropa.
 * Incluye tipo de prenda, talla y estado del artículo.
 */
public class DonacionRopa implements DonacionBase {

    private String tipoRopa;
    private int cantidad;
    private String talla;
    private String estadoArticulo; // "nuevo" o "usado"

    public DonacionRopa(String tipoRopa, int cantidad, String talla, String estadoArticulo) {
        this.tipoRopa = tipoRopa;
        this.cantidad = cantidad;
        this.talla = talla;
        this.estadoArticulo = estadoArticulo;
    }

    @Override
    public String getTipoDonacion() {
        return "ROPA";
    }

    @Override
    public String getDescripcion() {
        return "Donación de ropa: " + cantidad + " " + tipoRopa + ", talla " + talla + ", estado: " + estadoArticulo;
    }

    @Override
    public boolean requiereLogistica() {
        return true;
    }

    @Override
    public void validar() {
        if (tipoRopa == null || tipoRopa.isEmpty()) {
            throw new IllegalArgumentException("El tipo de ropa es obligatorio");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (talla == null || talla.isEmpty()) {
            throw new IllegalArgumentException("La talla es obligatoria");
        }
    }

    @Override
    public int getCantidad() {
        return cantidad;
    }

    @Override
    public String getRecurso() {
        return tipoRopa;
    }

    @Override
    public String getUnidadMedida() {
        return "prendas";
    }

    // Getters específicos

    public String getTipoRopa() {
        return tipoRopa;
    }

    public String getTalla() {
        return talla;
    }

    public String getEstadoArticulo() {
        return estadoArticulo;
    }
}
