package com.example.donatonlogistica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.donatonlogistica.model.Transporte;
import com.example.donatonlogistica.service.TransporteService;

@RestController
@RequestMapping("/transporte")
public class TransporteController {



    @Autowired
    private TransporteService transporteService;

    @PostMapping
    public String almacenarTransporte(@RequestBody Transporte transporte) {
        return transporteService.AlmacenarTransporte(transporte);
    }

    @GetMapping
    public List<Transporte> obtenerTransportes() {
        return transporteService.obtenerTransportes();
    }
    

}
