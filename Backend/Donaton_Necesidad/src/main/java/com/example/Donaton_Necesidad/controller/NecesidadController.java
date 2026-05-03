package com.example.Donaton_Necesidad.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Donaton_Necesidad.model.Necesidad;
import com.example.Donaton_Necesidad.service.NecesidadService;

@RestController
@RequestMapping("/necesidad")
public class NecesidadController {



    @Autowired
    private NecesidadService necesidadService;

    @PostMapping
    public String almacenarNecesidad(@RequestBody Necesidad necesidad) {
        necesidadService.almacenarNecesidad(necesidad);
        return "Necesidad almacenada correctamente";
    }

    @GetMapping
    public List<Necesidad> obtenerNecesidades() {
        return necesidadService.obtenerNecesidades();
    }

    
}
