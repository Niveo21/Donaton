package com.example.donatonlogistica.service;

import com.example.donatonlogistica.dto.AcopioDTO;
import com.example.donatonlogistica.dto.EnvioDTO;
import com.example.donatonlogistica.dto.TransporteDTO;
import com.example.donatonlogistica.model.*;
import com.example.donatonlogistica.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoRepository;

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public EnvioDTO crearEnvio(int transporteId, int acopioId, String tipoRecurso, int cantidad) {
        TransporteDTO transporte = resolverTransporte(transporteId);
        if (transporte == null) {
            throw new IllegalArgumentException("Transporte no encontrado: id=" + transporteId);
        }

        AcopioDTO acopio = resolverAcopio(acopioId);
        if (acopio == null) {
            throw new IllegalArgumentException("Acopio no encontrado: id=" + acopioId);
        }

        // Resta del inventario central (lanza excepcion si no hay stock suficiente)
        inventarioService.restarStock(tipoRecurso, cantidad);

        LocalDateTime ahora = LocalDateTime.now();

        Envio envio = new Envio(transporteId, acopioId, tipoRecurso, cantidad, ahora, "EN_TRANSITO");
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

        EnvioDTO dto = new EnvioDTO(envio);
        dto.setTransporte(transporte);
        dto.setAcopioDestino(acopio);
        return dto;
    }

    private TransporteDTO resolverTransporte(int transporteId) {
        try {
            String url = "http://DONATON-TRANSPORTE/transporte/" + transporteId;
            return restTemplate.getForObject(url, TransporteDTO.class);
        } catch (Exception e) {
            System.out.println("Aviso: no se pudo obtener el transporte " + transporteId + ": " + e.getMessage());
            return null;
        }
    }

    private AcopioDTO resolverAcopio(int acopioId) {
        try {
            String url = "http://DONATON-ACOPIO/acopio/" + acopioId;
            return restTemplate.getForObject(url, AcopioDTO.class);
        } catch (Exception e) {
            System.out.println("Aviso: no se pudo obtener el acopio " + acopioId + ": " + e.getMessage());
            return null;
        }
    }

    private EnvioDTO enriquecer(Envio envio) {
        EnvioDTO dto = new EnvioDTO(envio);
        dto.setTransporte(resolverTransporte(envio.getTransporteId()));
        dto.setAcopioDestino(resolverAcopio(envio.getAcopioDestinoId()));
        return dto;
    }

    public List<EnvioDTO> obtenerEnvios() {
        return envioRepository.findAll().stream()
                .map(this::enriquecer)
                .toList();
    }

    public List<MovimientoInventario> obtenerMovimientosPorEnvio(int envioId) {
        return movimientoRepository.findByEnvioId(envioId);
    }

    public List<MovimientoInventario> obtenerTodosLosMovimientos() {
        return movimientoRepository.findAll();
    }
}
