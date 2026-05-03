package com.example.Donaton_Donaciones.factory;

import com.example.Donaton_Donaciones.dto.DonacionRequest;

/**
 * Factory Method: crea el tipo correcto de donación según el campo tipoDonacion del request.
 */
public class DonacionFactory {

    /**
     * Crea una instancia del producto concreto correspondiente al tipo de donación.
     *
     * @param request DTO con los datos de la donación
     * @return instancia de DonacionBase del tipo correcto
     * @throws IllegalArgumentException si el tipo no es soportado
     */
    public static DonacionBase crearDonacion(DonacionRequest request) {

        switch (request.getTipoDonacion().toUpperCase()) {

            case "ALIMENTO":
                return new DonacionAlimento(
                    request.getNombre(),
                    request.getCantidad(),
                    request.getUnidadMedida(),
                    request.getFechaVencimiento(),
                    request.isRequiereRefrigeracion()
                );

            case "ROPA":
                return new DonacionRopa(
                    request.getTipoRopa(),
                    request.getCantidad(),
                    request.getTalla(),
                    request.getEstadoArticulo()
                );

            case "DINERO":
                return new DonacionDinero(
                    request.getCantidad(),
                    request.getMoneda(),
                    request.getMetodoPago()
                );

            case "INSUMO_HIGIENE":
                return new DonacionInsumoHigiene(
                    request.getNombre(),
                    request.getCantidad(),
                    request.getCategoria()
                );

            default:
                throw new IllegalArgumentException("Tipo de donación no soportado: " + request.getTipoDonacion());
        }
    }
}
