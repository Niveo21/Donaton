package com.example.Donaton_Autenticacion.dto;

public class TokenRequest {

    private String rut;
    private String rol;

    public TokenRequest() {}

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
