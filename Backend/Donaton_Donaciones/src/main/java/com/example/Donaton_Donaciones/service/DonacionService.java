package com.example.Donaton_Donaciones.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Donaton_Donaciones.dto.DonacionRequest;
import com.example.Donaton_Donaciones.factory.DonacionBase;
import com.example.Donaton_Donaciones.factory.DonacionFactory;
import com.example.Donaton_Donaciones.model.Donacion;
import com.example.Donaton_Donaciones.repository.DonacionRepository;

@Service
public class DonacionService {

    @Autowired
    private DonacionRepository donacionRepository;

    @Autowired
    private LogisticaBreakerService logisticaClient;

    @Autowired
    private PagoClientService pagoClient;

    public List<Donacion> obtenerDonaciones() {
        return donacionRepository.findAll();
    }

    /**
     * Procesa una donación usando el patrón Factory Method.
     * 1. La factoria crea el tipo correcto de donación
     * 2. Se validan los datos según las reglas del tipo
     * 3. Se persiste la entidad en la BD (una sola vez)
     * 4. Solo si requiere logística, se notifica vía LogisticaClient (con Circuit Breaker)
     */
    public Donacion procesarDonacion(DonacionRequest request) {

        // 1. Factory Method: crea el producto concreto según el tipo
        DonacionBase donacionBase = DonacionFactory.crearDonacion(request);

        // 2. Cada tipo valida sus propios datos
        donacionBase.validar();

        // 3. Construir la entidad con los datos del producto
        Donacion entidad = new Donacion();
        entidad.setTipoDonacion(donacionBase.getTipoDonacion());
        entidad.setTipoRecurso(donacionBase.getRecurso());
        entidad.setCantidad(donacionBase.getCantidad());
        entidad.setUnidadMedida(donacionBase.getUnidadMedida());
        entidad.setFechaDonacion(LocalDateTime.now());
        entidad.setEstado("Recibida");
        entidad.setNombreSolicitante(request.getNombreSolicitante());
        entidad.setMetodoEntrega(request.getMetodoEntrega());
        entidad.setDireccionRetiro(request.getDireccionRetiro());
        entidad.setComuna(request.getComuna());
        entidad.setTelefonoContacto(request.getTelefonoContacto());

        // Guardar UNA SOLA VEZ en la BD
        Donacion donacionGuardada = donacionRepository.save(entidad);

        // 4. Si es dinero, se procesa el pago y se actualiza el estado según el resultado
        if ("DINERO".equals(donacionBase.getTipoDonacion())) {
            boolean aprobado = pagoClient.procesarPago(donacionGuardada.getId(), request);
            donacionGuardada.setEstado(aprobado ? "Pago aprobado" : "Pago rechazado");
            donacionGuardada = donacionRepository.save(donacionGuardada);
        }
        // 5. Solo notificar a Logística si es un recurso físico
        //    El Circuit Breaker funciona porque LogisticaClient es otro @Service (otro proxy de Spring) basicamente la informacion no terminaba de llegar correctamente al circuitbreaker
        else if (donacionBase.requiereLogistica()) {
            logisticaClient.notificarLogistica(donacionGuardada);
        }

        return donacionGuardada;
    }
}
