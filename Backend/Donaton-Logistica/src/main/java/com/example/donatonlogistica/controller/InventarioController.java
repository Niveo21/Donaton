package com.example.donatonlogistica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        inventarioService.sumarStock(donacionInfo.getTipoRecurso(), donacionInfo.getCantidad(), donacionInfo.getAcopioId());
    }

    @GetMapping
    public List<Inventario> obtenerInventarios() {
        return inventarioService.obtenerInventarios();
    }

    @GetMapping("/acopio/{acopioId}")
    public List<Inventario> obtenerInventarioPorAcopio(@PathVariable Integer acopioId) {
        return inventarioService.obtenerInventarioPorAcopio(acopioId);
    }

    @PostMapping("/acopio/{acopioId}")
    public String agregarItemAcopio(@PathVariable Integer acopioId, @RequestBody Inventario item) {
        inventarioService.agregarItemAcopio(acopioId, item.getRecurso(), item.getStockActual());
        return "Item de inventario agregado correctamente";
    }

    @PutMapping("/{id}")
    public String actualizarItem(@PathVariable Integer id, @RequestBody Inventario datos) {
        inventarioService.actualizarItem(id, datos);
        return "Item de inventario actualizado correctamente";
    }

    @DeleteMapping("/{id}")
    public String eliminarItem(@PathVariable Integer id) {
        inventarioService.eliminarItem(id);
        return "Item de inventario eliminado correctamente";
    }

}
