package com.example.donatonlogistica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.donatonlogistica.dto.VoluntarioDTO;
import com.example.donatonlogistica.model.Voluntario;
import com.example.donatonlogistica.service.VoluntarioService;


@RestController
@RequestMapping("/voluntario")
public class VoluntarioController {

    @Autowired
    private VoluntarioService voluntarioService;

    @GetMapping
    public List<VoluntarioDTO> obtenerVoluntarios(){
        return voluntarioService.obtenerVoluntarios();
    }

    @PostMapping
    public String almacenarVoluntario(@RequestBody Voluntario voluntario) {
        voluntarioService.almacenarVoluntario(voluntario);
        return "Voluntario almacenado correctamente";
    }

    @PostMapping("/actualizar")
    public void actualizarVoluntario(@RequestBody Voluntario voluntario) {
        voluntarioService.almacenarVoluntario(voluntario);
    }

    @PutMapping("/{rut}/transporte/{transporteId}")
    public VoluntarioDTO asignarTransporte(@PathVariable String rut, @PathVariable int transporteId) {
        return voluntarioService.asignarTransporte(rut, transporteId);
    }

    @PutMapping("/{rut}/acopio/{acopioId}")
    public VoluntarioDTO asignarAcopio(@PathVariable String rut, @PathVariable int acopioId) {
        return voluntarioService.asignarAcopio(rut, acopioId);
    }

    @GetMapping("/transporte/{transporteId}")
    public List<VoluntarioDTO> obtenerPorTransporte(@PathVariable int transporteId) {
        return voluntarioService.obtenerPorTransporte(transporteId);
    }

    @GetMapping("/acopio/{acopioId}")
    public List<VoluntarioDTO> obtenerPorAcopio(@PathVariable int acopioId) {
        return voluntarioService.obtenerPorAcopio(acopioId);
    }
}
