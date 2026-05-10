package com.example.donatonlogistica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.donatonlogistica.dto.DonacionDTO;
import com.example.donatonlogistica.model.Inventario;
import com.example.donatonlogistica.service.InventarioService;

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
