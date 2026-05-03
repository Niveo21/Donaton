package com.example.Donaton_Logistica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Donaton_Logistica.model.Transporte;
import com.example.Donaton_Logistica.service.TransporteService;

@RestController
@RequestMapping("/transporte")
public class TransporteController {



    @Autowired
    private TransporteService transporteService;

    @PostMapping
    public String almacenarTransporte(@RequestBody Transporte transporte) {
        transporteService.AlmacenarTransporte(transporte);
        return "Transporte almacenado correctamente";
    }

    @GetMapping
    public List<Transporte> obtenerTransportes() {
        return transporteService.obtenerTransportes();
    }
    

}
