package com.example.Donaton_Logistica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Donaton_Logistica.model.Voluntarios;
import com.example.Donaton_Logistica.service.VoluntariosService;

@RestController
@RequestMapping("/voluntario")
public class VoluntariosController {

    @Autowired
    private VoluntariosService voluntariosService;


    @PostMapping
    public String almacenarVoluntarios(@RequestBody Voluntarios voluntario) {
        voluntariosService.almacenarVoluntarios(voluntario);
        return "Voluntarios almacenados correctamente";
    }

    @GetMapping
    public List<Voluntarios> obtenerVoluntarios() {
        return voluntariosService.obtenerVoluntarios();
    }

}
