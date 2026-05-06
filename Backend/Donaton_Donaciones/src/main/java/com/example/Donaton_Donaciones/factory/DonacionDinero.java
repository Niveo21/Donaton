package com.example.Donaton_Donaciones.factory;


public class DonacionDinero implements DonacionBase {

    private int cantidad;
    private String moneda;
    private String metodoPago;

    public DonacionDinero(int cantidad, String moneda, String metodoPago) {
        this.cantidad = cantidad;
        this.moneda = moneda;
        this.metodoPago = metodoPago;
    }

    @Override
    public String getTipoDonacion() {
        return "DINERO";
    }

    @Override
    public String getDescripcion() {
        return "Donación monetaria: $" + String.format("%.2f", cantidad) + " " + moneda + " vía " + metodoPago;
    }

    @Override
    public boolean requiereLogistica() {
        return false; // El dinero NO va al inventario físico
    }

    @Override
    public void validar() {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        if (moneda == null || moneda.isEmpty()) {
            throw new IllegalArgumentException("La moneda es obligatoria");
        }
        if (metodoPago == null || metodoPago.isEmpty()) {
            throw new IllegalArgumentException("El método de pago es obligatorio");
        }
    }

    @Override
    public int getCantidad() {
        return cantidad; // error por double
    }

    @Override
    public String getRecurso() {
        return "Dinero";
    }

    @Override
    public String getUnidadMedida() {
        return moneda;
    }

    // Getters específicos

    public String getMoneda() {
        return moneda;
    }

    public String getMetodoPago() {
        return metodoPago;
    }
}
