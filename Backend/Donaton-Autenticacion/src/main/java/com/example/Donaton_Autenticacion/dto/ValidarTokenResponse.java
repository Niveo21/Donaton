package com.example.Donaton_Autenticacion.dto;

public class ValidarTokenResponse {

    private boolean valido;
    private String rut;
    private String rol;

    public ValidarTokenResponse() {}

    public ValidarTokenResponse(boolean valido, String rut, String rol) {
        this.valido = valido;
        this.rut = rut;
        this.rol = rol;
    }

    public boolean isValido() { return valido; }
    public void setValido(boolean valido) { this.valido = valido; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
