package com.example.Donaton_Pago.dto;

public class PagoRequest {

    private int donacionId;
    private double monto;
    private String metodoPago;
    private String numeroTarjeta;

    public PagoRequest() {}

    public int getDonacionId() {
        return donacionId;
    }

    public void setDonacionId(int donacionId) {
        this.donacionId = donacionId;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
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

    
}
