package com.example.Donaton_Pago.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Donaton_Pago.dto.PagoRequest;
import com.example.Donaton_Pago.model.Pago;
import com.example.Donaton_Pago.repository.PagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public Pago procesarPago(PagoRequest request) {
        Pago pago = new Pago();
        pago.setDonacionId(request.getDonacionId());
        pago.setMonto(request.getMonto());
        pago.setMetodoPago(request.getMetodoPago());
        pago.setTarjetaNumero(request.getNumeroTarjeta());
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("APROBADO");

        return pagoRepository.save(pago);
    }

    public List<Pago> obtenerPagos() {
        return pagoRepository.findAll();
    }
}
