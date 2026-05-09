package com.example.Donaton_Donaciones.factory;

/**
 * Producto concreto: Donación de insumos de higiene.
 * Incluye tipo de insumo (jabón, shampoo, pasta dental, etc.) y categoría.
 */
public class DonacionInsumoHigiene implements DonacionBase {

    private String nombre;
    private int cantidad;
    private String categoria; // "personal" o "hogar"

    public DonacionInsumoHigiene(String nombre, int cantidad, String categoria) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.categoria = categoria;
    }

    @Override
    public String getTipoDonacion() {
        return "INSUMO_HIGIENE";
    }
    
    @Override
    public String getDescripcion() {
        return "Donación de insumo de higiene: " + cantidad + " unidades de " + nombre + " (categoría: " + categoria + ")";
    }

    @Override
    public boolean requiereLogistica() {
        return true;
    }

    @Override
    public void validar() {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del insumo es obligatorio");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (categoria == null || categoria.isEmpty()) {
            throw new IllegalArgumentException("La categoría es obligatoria (personal o hogar)");
        }
    }

    @Override
    public int getCantidad() {
        return cantidad;
    }

    @Override
    public String getRecurso() {
        return nombre;
    }

    @Override
    public String getUnidadMedida() {
        return "unidades";
    }

    // Getters específicos

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }
}
