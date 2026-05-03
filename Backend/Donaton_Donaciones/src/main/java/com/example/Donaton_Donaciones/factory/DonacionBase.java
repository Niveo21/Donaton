package com.example.Donaton_Donaciones.factory;

/**
 * Interfaz base del patrón Factory Method.
 * Define el contrato que todos los tipos de donación deben cumplir.
 */
public interface DonacionBase {

    /** Retorna el tipo de donación: "ALIMENTO", "ROPA", "DINERO", "INSUMO_HIGIENE" */
    String getTipoDonacion();

    /** Genera una descripción legible de la donación */
    String getDescripcion();

    /** Indica si esta donación debe notificar al microservicio de Logística */
    boolean requiereLogistica();

    /** Valida los datos de la donación. Lanza IllegalArgumentException si algo es inválido */
    void validar();

    /** Retorna la cantidad donada (0 para donaciones monetarias) */
    int getCantidad();

    /** Retorna el nombre del recurso para registrar en logística */
    String getRecurso();

    String getUnidadMedida();
}
