package com.example.Donaton_Registro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Donaton_Registro.model.Registro;
import com.example.Donaton_Registro.service.RegistroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/registro")
public class RegistroController {


    @Autowired
    private RegistroService registroService;


    @PostMapping
    public String almacenarRegistro(@Valid @RequestBody Registro registro) {
        registroService.almacenarRegistro(registro);
        return "Registro almacenado correctamente";
    }

    @GetMapping
    public List<Registro> obtenerRegistros() {
        return registroService.obtenerRegistros();
    }

}
