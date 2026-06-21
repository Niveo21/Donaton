package com.example.Donaton_Registro.dto;

public class LoginResponse {

    private String rut;
    private String nombre;
    private String email;
    private String rol;
    private String token;

    public LoginResponse() {}

    public LoginResponse(String rut, String nombre, String email, String rol, String token) {
        this.rut = rut;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.token = token;
    }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
