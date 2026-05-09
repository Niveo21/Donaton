package com.example.Donaton_Donaciones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Donaton_Donaciones.dto.DonacionRequest;
import com.example.Donaton_Donaciones.model.Donacion;
import com.example.Donaton_Donaciones.service.DonacionService;

@RestController
@RequestMapping("/donacion")
public class DonacionesController {

    @Autowired
    private DonacionService donacionService;

    /**
     * Endpoint para crear una donación usando Factory Method.
     * Recibe un DonacionRequest con el tipoDonacion y los campos correspondientes.
     * La factory se encarga de crear, validar y procesar el tipo correcto.
     */
    @PostMapping
    public ResponseEntity<?> almacenarDonacion(@RequestBody DonacionRequest request) {
        try {
            Donacion donacion = donacionService.procesarDonacion(request);
            return ResponseEntity.ok(donacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<Donacion> obtenerDonaciones() {
        return donacionService.obtenerDonaciones();
    }
}
