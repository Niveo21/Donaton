package com.example.donatonlogistica.dto;

import java.time.LocalDateTime;

import com.example.donatonlogistica.model.Envio;

public class EnvioDTO {

    private int id;
    private Integer transporteId;
    private TransporteDTO transporte;
    private Integer acopioDestinoId;
    private AcopioDTO acopioDestino;
    private String tipoRecurso;
    private int cantidad;
    private LocalDateTime fechaEnvio;
    private String estado;

    public EnvioDTO() {}

    public EnvioDTO(Envio envio) {
        this.id = envio.getId();
        this.transporteId = envio.getTransporteId();
        this.acopioDestinoId = envio.getAcopioDestinoId();
        this.tipoRecurso = envio.getTipoRecurso();
        this.cantidad = envio.getCantidad();
        this.fechaEnvio = envio.getFechaEnvio();
        this.estado = envio.getEstado();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getTransporteId() { return transporteId; }
    public void setTransporteId(Integer transporteId) { this.transporteId = transporteId; }

    public TransporteDTO getTransporte() { return transporte; }
    public void setTransporte(TransporteDTO transporte) { this.transporte = transporte; }

    public Integer getAcopioDestinoId() { return acopioDestinoId; }
    public void setAcopioDestinoId(Integer acopioDestinoId) { this.acopioDestinoId = acopioDestinoId; }

    public AcopioDTO getAcopioDestino() { return acopioDestino; }
    public void setAcopioDestino(AcopioDTO acopioDestino) { this.acopioDestino = acopioDestino; }

    public String getTipoRecurso() { return tipoRecurso; }
    public void setTipoRecurso(String tipoRecurso) { this.tipoRecurso = tipoRecurso; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
