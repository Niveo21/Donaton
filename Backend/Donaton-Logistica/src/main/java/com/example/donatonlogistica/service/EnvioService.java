package com.example.donatonlogistica.service;

import com.example.donatonlogistica.model.*;
import com.example.donatonlogistica.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoRepository;

    @Autowired
    private TransporteRepository transporteRepository;

    @Autowired
    private AcopioRepository acopioRepository;

    @Autowired
    private InventarioService inventarioService;

    @Transactional
    public Envio crearEnvio(int transporteId, int acopioId, String tipoRecurso, int cantidad) {
        Transporte transporte = transporteRepository.findById(transporteId)
            .orElseThrow(() -> new IllegalArgumentException("Transporte no encontrado: id=" + transporteId));

        Acopio acopio = acopioRepository.findById(acopioId)
            .orElseThrow(() -> new IllegalArgumentException("Acopio no encontrado: id=" + acopioId));

        // Resta del inventario central (lanza excepcion si no hay stock suficiente)
        inventarioService.restarStock(tipoRecurso, cantidad);

        LocalDateTime ahora = LocalDateTime.now();

        Envio envio = new Envio(transporte, acopio, tipoRecurso, cantidad, ahora, "EN_TRANSITO");
        envio = envioRepository.save(envio);

        // Movimiento SALIDA: recursos salen del inventario central
        movimientoRepository.save(new MovimientoInventario(
            tipoRecurso, cantidad, "SALIDA", ahora,
            "Salida de inventario central hacia acopio '" + acopio.getNombre() + "' via transporte " + transporte.getPlaca(),
            envio
        ));

        // Movimiento ENTRADA: recursos ingresan al acopio de destino
        movimientoRepository.save(new MovimientoInventario(
            tipoRecurso, cantidad, "ENTRADA", ahora,
            "Entrada al acopio '" + acopio.getNombre() + "' (" + acopio.getDireccion() + ") via transporte " + transporte.getPlaca(),
            envio
        ));

        return envio;
    }

    public List<Envio> obtenerEnvios() {
        return envioRepository.findAll();
    }

    public List<MovimientoInventario> obtenerMovimientosPorEnvio(int envioId) {
        return movimientoRepository.findByEnvioId(envioId);
    }

    public List<MovimientoInventario> obtenerTodosLosMovimientos() {
        return movimientoRepository.findAll();
    }
}
