package com.example.donatonlogistica.dto;

import com.example.donatonlogistica.model.Voluntario;

public class VoluntarioDTO {

    private String rut;
    private String nombre;
    private String email;
    private String rol;
    private Integer acopioId;
    private AcopioDTO acopio;
    private Integer transporteId;
    private TransporteDTO transporte;

    public VoluntarioDTO() {}

    public VoluntarioDTO(Voluntario voluntario) {
        this.rut = voluntario.getRut();
        this.nombre = voluntario.getNombre();
        this.email = voluntario.getEmail();
        this.rol = voluntario.getRol();
        this.acopioId = voluntario.getAcopioId();
        this.transporteId = voluntario.getTransporteId();
    }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Integer getAcopioId() { return acopioId; }
    public void setAcopioId(Integer acopioId) { this.acopioId = acopioId; }

    public AcopioDTO getAcopio() { return acopio; }
    public void setAcopio(AcopioDTO acopio) { this.acopio = acopio; }

    public Integer getTransporteId() { return transporteId; }
    public void setTransporteId(Integer transporteId) { this.transporteId = transporteId; }

    public TransporteDTO getTransporte() { return transporte; }
    public void setTransporte(TransporteDTO transporte) { this.transporte = transporte; }
}
