package com.example.Donaton_Acopio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Donaton_Acopio.model.Acopio;
import com.example.Donaton_Acopio.service.AcopioService;

@RestController
@RequestMapping("/acopio")
public class AcopioController {

    @Autowired
    private AcopioService acopioService;

    @PostMapping
    public String almacenarAcopio(@RequestBody Acopio acopio) {
        acopioService.almacenarAcopio(acopio);
        return "Acopio almacenado correctamente";
    }

    @GetMapping
    public List<Acopio> obtenerAcopios() {
        return acopioService.obtenerAcopios();
    }

    @GetMapping("/{id}")
    public Acopio obtenerPorId(@PathVariable int id) {
        return acopioService.obtenerPorId(id);
    }

    @DeleteMapping("/{id}")
    public String eliminarAcopio(@PathVariable int id) {
        acopioService.eliminarAcopio(id);
        return "Acopio eliminado correctamente";
    }
}
