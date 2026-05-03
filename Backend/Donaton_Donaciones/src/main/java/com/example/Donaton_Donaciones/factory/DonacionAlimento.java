package com.example.Donaton_Donaciones.factory;

import java.time.LocalDate;

/**
 * Producto concreto: Donación de alimentos.
 * Tiene validación de fecha de vencimiento y control de refrigeración.
 */
public class DonacionAlimento implements DonacionBase {

    private String nombre;
    private int cantidad;
    private String unidadMedida;
    private LocalDate fechaVencimiento;
    private boolean requiereRefrigeracion;

    public DonacionAlimento(String nombre, int cantidad, String unidadMedida, LocalDate fechaVencimiento, boolean requiereRefrigeracion) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.unidadMedida = unidadMedida;
        this.fechaVencimiento = fechaVencimiento;
        this.requiereRefrigeracion = requiereRefrigeracion;
    }

    @Override
    public String getTipoDonacion() {
        return "ALIMENTO";
    }

    @Override
    public String getDescripcion() {
        String refrigeracion = requiereRefrigeracion ? " (requiere refrigeración)" : "";
        return "Donación de alimento: " + cantidad + " unidades de " + nombre + refrigeracion;
    }

    @Override
    public boolean requiereLogistica() {
        return true;
    }

    @Override
    public void validar() {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del alimento es obligatorio");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se aceptan alimentos vencidos");
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

    // Getters específicos

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public boolean isRequiereRefrigeracion() {
        return requiereRefrigeracion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }
}
