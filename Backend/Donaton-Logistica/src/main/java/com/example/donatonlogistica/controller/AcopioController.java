package com.example.donatonlogistica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.donatonlogistica.model.Acopio;
import com.example.donatonlogistica.service.AcopioService;

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


}
