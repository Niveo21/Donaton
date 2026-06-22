package com.example.Donaton_Transporte.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Donaton_Transporte.model.Transporte;
import com.example.Donaton_Transporte.service.TransporteService;

@RestController
@RequestMapping("/transporte")
public class TransporteController {

    @Autowired
    private TransporteService transporteService;

    @PostMapping
    public String almacenarTransporte(@RequestBody Transporte transporte) {
        return transporteService.almacenarTransporte(transporte);
    }

    @GetMapping
    public List<Transporte> obtenerTransportes() {
        return transporteService.obtenerTransportes();
    }

    @GetMapping("/{id}")
    public Transporte obtenerPorId(@PathVariable int id) {
        return transporteService.obtenerPorId(id);
    }

    @DeleteMapping("/{id}")
    public String eliminarTransporte(@PathVariable int id) {
        transporteService.eliminarTransporte(id);
        return "Transporte eliminado correctamente";
    }
}
