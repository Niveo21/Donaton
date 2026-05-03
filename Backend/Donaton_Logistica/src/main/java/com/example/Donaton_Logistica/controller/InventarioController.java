package com.example.Donaton_Logistica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Donaton_Logistica.dto.DonacionDTO;
import com.example.Donaton_Logistica.model.Inventario;
import com.example.Donaton_Logistica.service.InventarioService;

@RestController
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @PostMapping
    public String almacenarInventario(@RequestBody Inventario inventario) {
        inventarioService.almacenarInventario(inventario);

        return "Inventario almacenado correctamente";
    }

    @PostMapping("/actualizar")
    public void actualizarInventario(@RequestBody DonacionDTO donacionInfo) {
        inventarioService.sumarStock(donacionInfo.getTipoRecurso(), donacionInfo.getCantidad());
    }

    @GetMapping
    public List<Inventario> obtenerInventarios() {
        return inventarioService.obtenerInventarios();
    }

}
