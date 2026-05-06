package com.example.Donaton_Donaciones.factory;


public interface DonacionBase {

    
    String getTipoDonacion();

    
    String getDescripcion();

    
    boolean requiereLogistica();

    
    void validar();

    
    int getCantidad();

    
    String getRecurso();

    String getUnidadMedida();
}
