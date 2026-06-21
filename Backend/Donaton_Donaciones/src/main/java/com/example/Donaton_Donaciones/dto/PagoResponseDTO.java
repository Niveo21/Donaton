package com.example.Donaton_Donaciones.dto;

public class PagoResponseDTO {

    private Long id;
    private int donacionId;
    private double monto;
    private String metodoPago;
    private String estado;

    public PagoResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getDonacionId() { return donacionId; }
    public void setDonacionId(int donacionId) { this.donacionId = donacionId; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
