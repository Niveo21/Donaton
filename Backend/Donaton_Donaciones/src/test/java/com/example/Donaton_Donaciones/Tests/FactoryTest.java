package com.example.Donaton_Donaciones.Tests;

import com.example.Donaton_Donaciones.dto.DonacionRequest;
import com.example.Donaton_Donaciones.factory.DonacionAlimento;
import com.example.Donaton_Donaciones.factory.DonacionBase;
import com.example.Donaton_Donaciones.factory.DonacionDinero;
import com.example.Donaton_Donaciones.factory.DonacionFactory;
import com.example.Donaton_Donaciones.factory.DonacionRopa;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FactoryTest {

    @Test
    void crearDonacion_conTipoRopa_devuelveDonacionRopaConDatosCorrectos() {
        DonacionRequest request = new DonacionRequest();
        request.setTipoDonacion("ROPA");
        request.setTipoRopa("Abrigo");
        request.setCantidad(2);
        request.setTalla("M");
        request.setEstadoArticulo("nuevo");

        DonacionBase donacion = DonacionFactory.crearDonacion(request);

        assertInstanceOf(DonacionRopa.class, donacion);
        assertEquals("ROPA", donacion.getTipoDonacion());
        assertEquals("Abrigo", donacion.getRecurso());
        assertEquals(2, donacion.getCantidad());
        assertDoesNotThrow(donacion::validar);
    }

    @Test
    void crearDonacion_conTipoAlimento_devuelveDonacionAlimentoQueRequiereLogistica() {
        DonacionRequest request = new DonacionRequest();
        request.setTipoDonacion("ALIMENTO");
        request.setNombre("Arroz");
        request.setCantidad(5);
        request.setUnidadMedida("kg");

        DonacionBase donacion = DonacionFactory.crearDonacion(request);

        assertInstanceOf(DonacionAlimento.class, donacion);
        assertEquals("Arroz", donacion.getRecurso());
        assertTrue(donacion.requiereLogistica());
    }

    @Test
    void crearDonacion_conTipoDinero_noRequiereLogistica() {
        
        DonacionRequest request = new DonacionRequest();
        request.setTipoDonacion("DINERO");
        request.setCantidad(10000);
        request.setMoneda("CLP");
        request.setMetodoPago("Webpay");

        DonacionBase donacion = DonacionFactory.crearDonacion(request);

        assertInstanceOf(DonacionDinero.class, donacion);
        assertFalse(donacion.requiereLogistica());
    }

    @Test
    void crearDonacion_conTipoNoSoportado_lanzaIllegalArgumentException() {
        DonacionRequest request = new DonacionRequest();
        request.setTipoDonacion("CRIPTOMONEDA");

        assertThrows(IllegalArgumentException.class, () -> DonacionFactory.crearDonacion(request));
    }

    @Test
    void validar_conAlimentoVencido_lanzaIllegalArgumentException() {
        DonacionRequest request = new DonacionRequest();
        request.setTipoDonacion("ALIMENTO");
        request.setNombre("Leche");
        request.setCantidad(3);
        request.setUnidadMedida("litros");
        request.setFechaVencimiento(LocalDate.now().minusDays(1));

        DonacionBase donacion = DonacionFactory.crearDonacion(request);

        assertThrows(IllegalArgumentException.class, donacion::validar);
    }
}
