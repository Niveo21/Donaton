package com.example.Donaton_Donaciones.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Donaton_Donaciones.dto.DonacionRequest;
import com.example.Donaton_Donaciones.dto.PagoResponseDTO;

@Service
public class PagoClientService {

    @Autowired
    private RestTemplate restTemplate;

    public boolean procesarPago(int donacionId, DonacionRequest request) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("donacionId", donacionId);
            body.put("monto", request.getCantidad());
            body.put("metodoPago", request.getMetodoPago());
            body.put("numeroTarjeta", request.getNumeroTarjeta());

            String url = "http://DONATON-PAGO/pagos";
            PagoResponseDTO respuesta = restTemplate.postForObject(url, body, PagoResponseDTO.class);

            return respuesta != null && "APROBADO".equals(respuesta.getEstado());
        } catch (Exception e) {
            System.out.println("Aviso: no se pudo procesar el pago de la donación " + donacionId + ": " + e.getMessage());
            return false;
        }
    }
}
